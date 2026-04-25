package com.englishtyping.service.impl;

import com.englishtyping.dto.UserStatsResponse;
import com.englishtyping.dto.WeakWordDto;
import com.englishtyping.entity.TypingSession;
import com.englishtyping.entity.User;
import com.englishtyping.entity.UserLevelProgress;
import com.englishtyping.entity.WordErrorStat;
import com.englishtyping.repository.TypingSessionRepository;
import com.englishtyping.repository.UserLevelProgressRepository;
import com.englishtyping.repository.UserRepository;
import com.englishtyping.repository.WordErrorStatRepository;
import com.englishtyping.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 */
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final UserRepository userRepository;
    private final TypingSessionRepository typingSessionRepository;
    private final UserLevelProgressRepository userLevelProgressRepository;
    private final WordErrorStatRepository wordErrorStatRepository;

    @Override
    public UserStatsResponse getUserStats(String userId) {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        // 查询用户的所有打字会话
        List<TypingSession> sessions = typingSessionRepository.findByUserId(userId);

        // 计算累计练习时长（毫秒）
        long totalPracticeTimeMs = sessions.stream()
                .mapToLong(TypingSession::getTimeMs)
                .sum();

        // 计算累计完成关卡数（已完成的关卡）
        int totalCompletedLevels = (int) userLevelProgressRepository.findByUserId(userId).stream()
                .filter(UserLevelProgress::getCompleted)
                .count();

        // 计算累计输入字符数
        long totalCharactersTyped = sessions.stream()
                .filter(session -> session.getCharactersTyped() != null)
                .mapToLong(TypingSession::getCharactersTyped)
                .sum();

        return UserStatsResponse.builder()
                .totalPracticeTimeMs(totalPracticeTimeMs)
                .totalCompletedLevels(totalCompletedLevels)
                .totalCharactersTyped(totalCharactersTyped)
                .totalScore(user.getTotalScore())
                .streak(user.getStreak())
                .username(user.getUsername())
                .build();
    }

    @Override
    public UserStatsResponse getUserStatsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        return getUserStats(user.getId());
    }

    @Override
    public List<WeakWordDto> getWeakWords(String userId) {
        // 验证用户是否存在
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在");
        }

        // 查询用户的错误单词统计，按错误次数降序排序，取前 10 条
        List<WordErrorStat> stats = wordErrorStatRepository.findByUserIdOrderByErrorCountDesc(userId).stream()
                .limit(10)
                .collect(Collectors.toList());

        return stats.stream()
                .map(stat -> WeakWordDto.builder()
                        .word(stat.getWord())
                        .errorCount(stat.getErrorCount())
                        .build())
                .collect(Collectors.toList());
    }
}
