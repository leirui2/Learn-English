package com.englishtyping.service.impl;

import com.englishtyping.dto.CategoryDto;
import com.englishtyping.dto.CompleteLevelRequest;
import com.englishtyping.dto.CompleteLevelResponse;
import com.englishtyping.dto.LevelDto;
import com.englishtyping.entity.*;
import com.englishtyping.repository.*;
import com.englishtyping.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 关卡服务实现类
 * @author lei
 */
@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final CategoryRepository categoryRepository;
    private final LevelRepository levelRepository;
    private final UserLevelProgressRepository userLevelProgressRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final TypingSessionRepository typingSessionRepository;
    private final WordErrorStatRepository wordErrorStatRepository;
    private final DailyCheckinRepository dailyCheckinRepository;

    @Override
    public List<CategoryDto> getLevelList(String userId) {
        // 获取所有分类
        List<Category> categories = categoryRepository.findAll();

        // 获取用户的关卡进度
        List<UserLevelProgress> progressList = userLevelProgressRepository.findByUserId(userId);
        Map<Integer, UserLevelProgress> progressMap = progressList.stream()
                .collect(Collectors.toMap(p -> p.getLevel().getId(), p -> p));

        // 构建分类DTO列表
        return categories.stream().map(category -> {
            // 获取分类下的所有关卡
            List<Level> levels = levelRepository.findByCategoryIdOrderByLevelOrderAsc(category.getId());

            // 构建关卡DTO列表
            List<LevelDto> levelDtos = levels.stream().map(level -> {
                UserLevelProgress progress = progressMap.get(level.getId());
                return LevelDto.builder()
                        .id(level.getId())
                        .name(level.getName())
                        .description(level.getDescription())
                        .levelOrder(level.getLevelOrder())
                        .standardTimeMs(level.getStandardTimeMs())
                        .unlocked(progress != null && progress.getUnlocked())
                        .completed(progress != null && progress.getCompleted())
                        .bestAccuracy(progress != null ? progress.getBestAccuracy() : null)
                        .bestTimeMs(progress != null ? progress.getBestTimeMs() : null)
                        .build();
            }).collect(Collectors.toList());

            // 构建分类DTO
            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .difficulty(category.getDifficulty())
                    .levels(levelDtos)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public LevelDto getLevelDetail(Integer levelId, String userId) {
        // 获取关卡信息
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));

        // 检查用户是否有权限访问该关卡
        UserLevelProgress progress = userLevelProgressRepository.findByUserIdAndLevelId(userId, levelId);
        if (progress == null || !progress.getUnlocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "关卡未解锁");
        }

        // 获取练习内容
        List<Exercise> exercises = exerciseRepository.findByLevelIdOrderByExerciseOrderAsc(levelId);

        // 构建关卡DTO（包含练习内容）
        return LevelDto.builder()
                .id(level.getId())
                .name(level.getName())
                .description(level.getDescription())
                .levelOrder(level.getLevelOrder())
                .standardTimeMs(level.getStandardTimeMs())
                .unlocked(progress.getUnlocked())
                .completed(progress.getCompleted())
                .bestAccuracy(progress.getBestAccuracy())
                .bestTimeMs(progress.getBestTimeMs())
                .exercises(exercises)
                .build();
    }

    @Override
    public List<Exercise> getLevelExercises(Integer levelId, String userId) {
        // 检查用户是否有权限访问该关卡
        UserLevelProgress progress = userLevelProgressRepository.findByUserIdAndLevelId(userId, levelId);
        if (progress == null || !progress.getUnlocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "关卡未解锁");
        }

        // 获取关卡的所有练习内容
        return exerciseRepository.findByLevelIdOrderByExerciseOrderAsc(levelId);
    }

    @Override
    @Transactional
    public CompleteLevelResponse completeLevel(Integer levelId, String userId, CompleteLevelRequest request) {
        // 1. 验证关卡是否存在
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));

        // 2. 验证用户是否有权限访问该关卡
        UserLevelProgress progress = userLevelProgressRepository.findByUserIdAndLevelId(userId, levelId);
        if (progress == null || !progress.getUnlocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "关卡未解锁");
        }

        // 3. 获取用户实体
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        // 4. 计算积分
        int score = calculateScore(request.getAccuracy(), request.getTimeMs(), level.getStandardTimeMs());

        // 5. 更新用户总积分
        user.setTotalScore(user.getTotalScore() + score);
        userRepository.save(user);

        // 6. 更新关卡进度
        boolean isCompleted = request.getAccuracy() >= 80.0;
        progress.setCompleted(isCompleted);
        
        // 更新最佳正确率（只增不减）
        if (progress.getBestAccuracy() == null || request.getAccuracy() > progress.getBestAccuracy()) {
            progress.setBestAccuracy(request.getAccuracy());
        }
        
        // 更新最佳用时
        if (progress.getBestTimeMs() == null || request.getTimeMs() < progress.getBestTimeMs()) {
            progress.setBestTimeMs(request.getTimeMs());
        }
        
        userLevelProgressRepository.save(progress);

        // 7. 解锁下一关（如果正确率 >= 80%）
        Integer nextLevelId = null;
        boolean nextLevelUnlocked = false;
        if (isCompleted) {
            Level nextLevel = findNextLevel(level);
            if (nextLevel != null) {
                nextLevelId = nextLevel.getId();
                UserLevelProgress nextProgress = userLevelProgressRepository.findByUserIdAndLevelId(userId, nextLevelId);
                if (nextProgress == null) {
                    nextProgress = UserLevelProgress.builder()
                            .user(user)
                            .level(nextLevel)
                            .unlocked(true)
                            .completed(false)
                            .build();
                    userLevelProgressRepository.save(nextProgress);
                    nextLevelUnlocked = true;
                } else if (!nextProgress.getUnlocked()) {
                    nextProgress.setUnlocked(true);
                    userLevelProgressRepository.save(nextProgress);
                    nextLevelUnlocked = true;
                }
            }
        }

        // 8. 创建打字会话记录
        TypingSession session = TypingSession.builder()
                .user(user)
                .level(level)
                .accuracy(request.getAccuracy())
                .timeMs(request.getTimeMs())
                .score(score)
                .charactersTyped(request.getCharactersTyped())
                .build();
        typingSessionRepository.save(session);

        // 9. 批量 upsert 错误单词统计
        if (request.getErrorChars() != null && !request.getErrorChars().isEmpty()) {
            for (Map.Entry<String, Integer> entry : request.getErrorChars().entrySet()) {
                String word = entry.getKey();
                Integer errorCount = entry.getValue();
                
                WordErrorStat stat = wordErrorStatRepository.findByUserIdAndWord(userId, word)
                        .orElse(WordErrorStat.builder()
                                .user(user)
                                .word(word)
                                .errorCount(0)
                                .build());
                
                stat.setErrorCount(stat.getErrorCount() + errorCount);
                wordErrorStatRepository.save(stat);
            }
        }

        // 10. 处理每日打卡逻辑
        boolean checkedIn = false;
        int checkinBonus = 0;
        LocalDate today = LocalDate.now();
        
        if (dailyCheckinRepository.findByUserIdAndCheckinDate(userId, today).isEmpty()) {
            // 创建今日打卡记录
            DailyCheckin checkin = DailyCheckin.builder()
                    .user(user)
                    .checkinDate(today)
                    .build();
            dailyCheckinRepository.save(checkin);
            checkedIn = true;

            // 更新连续打卡天数
            LocalDate yesterday = today.minusDays(1);
            if (user.getLastCheckinDate() != null && 
                user.getLastCheckinDate().toLocalDate().equals(yesterday)) {
                // 昨天有打卡，连续天数 +1
                user.setStreak(user.getStreak() + 1);
            } else {
                // 断日，重置为 1
                user.setStreak(1);
            }
            
            user.setLastCheckinDate(today.atStartOfDay());

            // 检查是否达到 7 的整数倍，发放奖励
            if (user.getStreak() % 7 == 0) {
                checkinBonus = 50;
                user.setTotalScore(user.getTotalScore() + checkinBonus);
            }

            userRepository.save(user);
        }

        // 11. 构建响应
        return CompleteLevelResponse.builder()
                .accuracy(request.getAccuracy())
                .timeMs(request.getTimeMs())
                .score(score)
                .nextLevelUnlocked(nextLevelUnlocked)
                .nextLevelId(nextLevelId)
                .checkedIn(checkedIn)
                .checkinBonus(checkinBonus)
                .build();
    }

    /**
     * 计算积分
     * 基础分 100 分，正确率每超过 80% 一个百分点加 2 分，用时每低于关卡标准时间 10 秒加 5 分
     */
    private int calculateScore(double accuracy, long timeMs, long standardTimeMs) {
        int baseScore = 100;
        
        // 正确率加成：每超过 80% 一个百分点加 2 分
        int accuracyBonus = 0;
        if (accuracy > 80.0) {
            accuracyBonus = (int) ((accuracy - 80.0) * 2);
        }
        
        // 用时加成：每低于标准时间 10 秒加 5 分
        int timeBonus = 0;
        long timeDiffMs = standardTimeMs - timeMs;
        if (timeDiffMs > 0) {
            timeBonus = (int) (timeDiffMs / 10000) * 5; // 10000ms = 10秒
        }
        
        return baseScore + accuracyBonus + timeBonus;
    }

    /**
     * 查找下一关
     */
    private Level findNextLevel(Level currentLevel) {
        List<Level> levels = levelRepository.findByCategoryIdOrderByLevelOrderAsc(currentLevel.getCategory().getId());
        
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getId().equals(currentLevel.getId()) && i < levels.size() - 1) {
                return levels.get(i + 1);
            }
        }
        
        return null;
    }
}