package com.englishtyping.service.impl;

import com.englishtyping.dto.LeaderboardEntryDto;
import com.englishtyping.dto.LeaderboardResponse;
import com.englishtyping.dto.LeaderboardTitleDto;
import com.englishtyping.entity.LeaderboardTitle;
import com.englishtyping.entity.User;
import com.englishtyping.repository.LeaderboardTitleRepository;
import com.englishtyping.repository.TypingSessionRepository;
import com.englishtyping.repository.UserRepository;
import com.englishtyping.service.LeaderboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 排行榜服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardServiceImpl implements LeaderboardService {

    private final UserRepository userRepository;
    private final TypingSessionRepository typingSessionRepository;
    private final LeaderboardTitleRepository leaderboardTitleRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final int CACHE_TTL_SECONDS = 600; // 10 分钟
    private static final int TOP_N = 100; // 前 100 名

    @Override
    public LeaderboardResponse getLeaderboard(String type, String period, Integer categoryId, String currentUserId) {
        // 1. 构建缓存 key
        String cacheKey = buildCacheKey(type, period, categoryId);

        // 2. 尝试从 Redis 获取缓存
        LeaderboardResponse cachedResponse = getFromCache(cacheKey);
        if (cachedResponse != null) {
            log.debug("Leaderboard cache hit: {}", cacheKey);
            // 更新当前用户标记
            markCurrentUser(cachedResponse, currentUserId);
            return cachedResponse;
        }

        log.debug("Leaderboard cache miss: {}", cacheKey);

        // 3. 缓存未命中，从数据库查询
        LeaderboardResponse response = buildLeaderboardFromDatabase(type, period, categoryId, currentUserId);

        // 4. 写入缓存
        saveToCache(cacheKey, response);

        return response;
    }

    /**
     * 构建缓存 key
     */
    private String buildCacheKey(String type, String period, Integer categoryId) {
        StringBuilder key = new StringBuilder("leaderboard:");
        key.append(type).append(":");
        key.append(period);
        if (categoryId != null) {
            key.append(":category:").append(categoryId);
        }
        return key.toString();
    }

    /**
     * 从缓存获取排行榜
     */
    private LeaderboardResponse getFromCache(String cacheKey) {
        try {
            String cachedJson = redisTemplate.opsForValue().get(cacheKey);
            if (cachedJson != null) {
                return objectMapper.readValue(cachedJson, LeaderboardResponse.class);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize leaderboard from cache", e);
        }
        return null;
    }

    /**
     * 保存到缓存
     */
    private void saveToCache(String cacheKey, LeaderboardResponse response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, json, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize leaderboard to cache", e);
        }
    }

    /**
     * 从数据库构建排行榜
     */
    private LeaderboardResponse buildLeaderboardFromDatabase(String type, String period, Integer categoryId, String currentUserId) {
        List<LeaderboardEntryDto> entries;
        LeaderboardEntryDto currentUserRank = null;

        if ("score".equals(type)) {
            // 总积分榜
            entries = buildScoreLeaderboard(period, categoryId);
            currentUserRank = findCurrentUserRankInScoreLeaderboard(period, categoryId, currentUserId, entries);
        } else if ("streak".equals(type)) {
            // 连续打卡榜
            entries = buildStreakLeaderboard(period, categoryId);
            currentUserRank = findCurrentUserRankInStreakLeaderboard(period, categoryId, currentUserId, entries);
        } else {
            throw new IllegalArgumentException("Invalid leaderboard type: " + type);
        }

        return LeaderboardResponse.builder()
                .type(type)
                .period(period)
                .categoryId(categoryId)
                .entries(entries)
                .currentUserRank(currentUserRank)
                .build();
    }

    /**
     * 构建总积分榜
     */
    private List<LeaderboardEntryDto> buildScoreLeaderboard(String period, Integer categoryId) {
        List<User> users;

        if (categoryId != null) {
            // 按分类筛选：查询在该分类内有练习记录的用户
            List<String> userIds = typingSessionRepository.findAll().stream()
                    .filter(session -> session.getLevel().getCategory().getId().equals(categoryId))
                    .filter(session -> matchesPeriod(session.getCreatedAt(), period))
                    .map(session -> session.getUser().getId())
                    .distinct()
                    .collect(Collectors.toList());

            users = userRepository.findAllById(userIds);
        } else {
            // 全部用户
            users = userRepository.findAll();
        }

        // 按积分排序，取前 100 名
        return users.stream()
                .sorted(Comparator.comparing(User::getTotalScore).reversed())
                .limit(TOP_N)
                .map((user) -> {
                    int rank = users.stream()
                            .filter(u -> u.getTotalScore() > user.getTotalScore())
                            .collect(Collectors.toList())
                            .size() + 1;
                    
                    return LeaderboardEntryDto.builder()
                            .rank(rank)
                            .userId(user.getId())
                            .username(user.getUsername())
                            .score(user.getTotalScore())
                            .isCurrentUser(false)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建连续打卡榜
     */
    private List<LeaderboardEntryDto> buildStreakLeaderboard(String period, Integer categoryId) {
        List<User> users;

        if (categoryId != null) {
            // 按分类筛选：查询在该分类内有练习记录的用户
            List<String> userIds = typingSessionRepository.findAll().stream()
                    .filter(session -> session.getLevel().getCategory().getId().equals(categoryId))
                    .filter(session -> matchesPeriod(session.getCreatedAt(), period))
                    .map(session -> session.getUser().getId())
                    .distinct()
                    .collect(Collectors.toList());

            users = userRepository.findAllById(userIds);
        } else {
            // 全部用户
            users = userRepository.findAll();
        }

        // 按连续打卡天数排序，取前 100 名
        return users.stream()
                .sorted(Comparator.comparing(User::getStreak).reversed())
                .limit(TOP_N)
                .map((user) -> {
                    int rank = users.stream()
                            .filter(u -> u.getStreak() > user.getStreak())
                            .collect(Collectors.toList())
                            .size() + 1;
                    
                    return LeaderboardEntryDto.builder()
                            .rank(rank)
                            .userId(user.getId())
                            .username(user.getUsername())
                            .streak(user.getStreak())
                            .isCurrentUser(false)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 查找当前用户在总积分榜中的排名
     */
    private LeaderboardEntryDto findCurrentUserRankInScoreLeaderboard(String period, Integer categoryId, 
                                                                       String currentUserId, List<LeaderboardEntryDto> topEntries) {
        // 如果没有当前用户ID（游客访问），返回null
        if (currentUserId == null) {
            return null;
        }
        
        // 检查用户是否在前 100 名中
        Optional<LeaderboardEntryDto> inTop100 = topEntries.stream()
                .filter(entry -> entry.getUserId().equals(currentUserId))
                .findFirst();

        if (inTop100.isPresent()) {
            return inTop100.get();
        }

        // 不在前 100 名，需要计算实际排名
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        if (currentUser == null) {
            return null;
        }

        List<User> allUsers;
        if (categoryId != null) {
            List<String> userIds = typingSessionRepository.findAll().stream()
                    .filter(session -> session.getLevel().getCategory().getId().equals(categoryId))
                    .filter(session -> matchesPeriod(session.getCreatedAt(), period))
                    .map(session -> session.getUser().getId())
                    .distinct()
                    .collect(Collectors.toList());
            allUsers = userRepository.findAllById(userIds);
        } else {
            allUsers = userRepository.findAll();
        }

        int rank = (int) allUsers.stream()
                .filter(u -> u.getTotalScore() > currentUser.getTotalScore())
                .count() + 1;

        return LeaderboardEntryDto.builder()
                .rank(rank)
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .score(currentUser.getTotalScore())
                .isCurrentUser(true)
                .build();
    }

    /**
     * 查找当前用户在连续打卡榜中的排名
     */
    private LeaderboardEntryDto findCurrentUserRankInStreakLeaderboard(String period, Integer categoryId, 
                                                                        String currentUserId, List<LeaderboardEntryDto> topEntries) {
        // 如果没有当前用户ID（游客访问），返回null
        if (currentUserId == null) {
            return null;
        }
        
        // 检查用户是否在前 100 名中
        Optional<LeaderboardEntryDto> inTop100 = topEntries.stream()
                .filter(entry -> entry.getUserId().equals(currentUserId))
                .findFirst();

        if (inTop100.isPresent()) {
            return inTop100.get();
        }

        // 不在前 100 名，需要计算实际排名
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        if (currentUser == null) {
            return null;
        }

        List<User> allUsers;
        if (categoryId != null) {
            List<String> userIds = typingSessionRepository.findAll().stream()
                    .filter(session -> session.getLevel().getCategory().getId().equals(categoryId))
                    .filter(session -> matchesPeriod(session.getCreatedAt(), period))
                    .map(session -> session.getUser().getId())
                    .distinct()
                    .collect(Collectors.toList());
            allUsers = userRepository.findAllById(userIds);
        } else {
            allUsers = userRepository.findAll();
        }

        int rank = (int) allUsers.stream()
                .filter(u -> u.getStreak() > currentUser.getStreak())
                .count() + 1;

        return LeaderboardEntryDto.builder()
                .rank(rank)
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .streak(currentUser.getStreak())
                .isCurrentUser(true)
                .build();
    }

    /**
     * 判断时间是否匹配时间维度
     */
    private boolean matchesPeriod(LocalDateTime dateTime, String period) {
        if ("all".equals(period)) {
            return true;
        } else if ("week".equals(period)) {
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            return dateTime.isAfter(oneWeekAgo);
        }
        return true;
    }

    /**
     * 标记当前用户
     */
    private void markCurrentUser(LeaderboardResponse response, String currentUserId) {
        // 如果没有当前用户ID（游客访问），跳过标记
        if (currentUserId == null) {
            return;
        }
        
        // 在前 100 名中标记当前用户
        response.getEntries().forEach(entry -> {
            if (entry.getUserId().equals(currentUserId)) {
                entry.setIsCurrentUser(true);
            }
        });

        // 如果当前用户不在前 100 名，currentUserRank 已经包含了用户信息
        if (response.getCurrentUserRank() != null) {
            response.getCurrentUserRank().setIsCurrentUser(true);
        }
    }

    // ===== 称号管理 =====

    @Override
    public List<LeaderboardTitleDto> getAllTitles() {
        return leaderboardTitleRepository.findAllByOrderBySortOrderAsc().stream()
                .map(this::convertToTitleDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaderboardTitleDto getTitleByRank(Integer rank) {
        if (rank == null || rank <= 0) {
            return null;
        }

        List<LeaderboardTitle> titles = leaderboardTitleRepository.findAllByOrderBySortOrderAsc();
        
        for (LeaderboardTitle title : titles) {
            if (rank >= title.getMinRank() && rank <= title.getMaxRank()) {
                return convertToTitleDto(title);
            }
        }
        
        return null; // 没有匹配的称号
    }

    /**
     * 将 LeaderboardTitle 实体转换为 DTO
     */
    private LeaderboardTitleDto convertToTitleDto(LeaderboardTitle title) {
        return LeaderboardTitleDto.builder()
                .id(title.getId())
                .name(title.getName())
                .minRank(title.getMinRank())
                .maxRank(title.getMaxRank())
                .icon(title.getIcon())
                .color(title.getColor())
                .sortOrder(title.getSortOrder())
                .build();
    }
}
