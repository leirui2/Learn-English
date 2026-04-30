package com.englishtyping.controller;

import com.englishtyping.dto.*;
import com.englishtyping.entity.ChallengeRecord;
import com.englishtyping.entity.DailyCheckin;
import com.englishtyping.entity.Exercise;
import com.englishtyping.entity.PointsRecord;
import com.englishtyping.entity.PointsType;
import com.englishtyping.entity.User;
import com.englishtyping.repository.ChallengeRecordRepository;
import com.englishtyping.repository.DailyCheckinRepository;
import com.englishtyping.repository.ExerciseRepository;
import com.englishtyping.repository.PointsRecordRepository;
import com.englishtyping.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ExerciseRepository exerciseRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final UserRepository userRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final PointsRecordRepository pointsRecordRepository;

    /**
     * 获取随机练习内容
     * GET /challenge/exercises?type=WORD&count=20
     */
    @GetMapping("/exercises")
    public ResponseEntity<ApiResult<List<ChallengeExerciseDto>>> getRandomExercises(
            @RequestParam(defaultValue = "WORD") String type,
            @RequestParam(defaultValue = "20") int count) {

        List<Exercise> all;
        if ("WORD".equalsIgnoreCase(type)) {
            // 单词：英文长度 <= 20 且不含空格
            all = exerciseRepository.findAll().stream()
                    .filter(e -> e.getContentEn().length() <= 20 && !e.getContentEn().contains(" "))
                    .collect(Collectors.toList());
        } else {
            // 短句：英文长度 > 20 或含空格
            all = exerciseRepository.findAll().stream()
                    .filter(e -> e.getContentEn().length() > 20 || e.getContentEn().contains(" "))
                    .collect(Collectors.toList());
        }

        Collections.shuffle(all);
        List<ChallengeExerciseDto> result = all.stream()
                .limit(count)
                .map(e -> ChallengeExerciseDto.builder()
                        .id(e.getId())
                        .contentEn(e.getContentEn())
                        .contentZh(e.getContentZh())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResult.success(result));
    }

    /**
     * 提交挑战结果
     * POST /challenge/submit
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResult<SubmitChallengeResponse>> submitChallenge(
            @Valid @RequestBody SubmitChallengeRequest request,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();

        ChallengeRecord record = ChallengeRecord.builder()
                .userId(userId)
                .contentType(request.getContentType().toUpperCase())
                .timeMode(request.getTimeMode().toUpperCase())
                .timeLimit(request.getTimeLimit())
                .passedCount(request.getPassedCount())
                .accuracy(request.getAccuracy())
                .timeMs(request.getTimeMs())
                .wpm(request.getWpm())
                .build();

        challengeRecordRepository.save(record);
        
        // 计算天梯挑战积分：基础分 = passedCount * 10，正确率加成 = accuracy * 2
        int challengeScore = (int) (request.getPassedCount() * 10 + request.getAccuracy() * 2);
        
        // 获取用户并更新积分
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setTotalScore(user.getTotalScore() + challengeScore);
        userRepository.save(user);
        
        // 记录积分变动（天梯挑战）
        PointsRecord challengeRecord2 = PointsRecord.builder()
                .userId(userId)
                .points(challengeScore)
                .type(PointsType.CHALLENGE)
                .description("天梯挑战: " + request.getContentType() + " - " + request.getTimeMode())
                .relatedId(record.getId())
                .balanceAfter(user.getTotalScore())
                .build();
        pointsRecordRepository.save(challengeRecord2);
        
        // 处理每日打卡逻辑（参加天梯赛也算打卡）
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
                userRepository.save(user);
                
                // 记录打卡奖励积分变动
                PointsRecord checkinBonusRecord = PointsRecord.builder()
                        .userId(userId)
                        .points(checkinBonus)
                        .type(PointsType.CHECKIN_BONUS)
                        .description("连续打卡 " + user.getStreak() + " 天奖励")
                        .relatedId(null)
                        .balanceAfter(user.getTotalScore())
                        .build();
                pointsRecordRepository.save(checkinBonusRecord);
            } else {
                userRepository.save(user);
            }
        }
        
        return ResponseEntity.ok(ApiResult.success(new SubmitChallengeResponse(checkedIn, checkinBonus)));
    }

    /**
     * 获取天梯排行榜
     * GET /challenge/leaderboard?type=WORD&mode=TIMED&timeLimit=60
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResult<List<ChallengeLeaderboardEntryDto>>> getLeaderboard(
            @RequestParam(defaultValue = "WORD") String type,
            @RequestParam(defaultValue = "TIMED") String mode,
            @RequestParam(required = false) Integer timeLimit,
            Authentication authentication) {

        // 支持游客访问：如果未认证，currentUserId为null
        String currentUserId = authentication != null ? (String) authentication.getPrincipal() : null;
        List<ChallengeRecord> records;

        if ("TIMED".equalsIgnoreCase(mode)) {
            records = challengeRecordRepository.findTimedLeaderboard(
                    type.toUpperCase(), timeLimit, PageRequest.of(0, 100));
        } else {
            records = challengeRecordRepository.findInfiniteLeaderboard(
                    type.toUpperCase(), PageRequest.of(0, 100));
        }

        // 每个用户只取最佳成绩
        List<ChallengeRecord> best = records.stream()
                .collect(Collectors.toMap(
                        ChallengeRecord::getUserId,
                        r -> r,
                        (a, b) -> a  // 已按排序取第一个
                ))
                .values().stream()
                .sorted((a, b) -> {
                    if (!a.getPassedCount().equals(b.getPassedCount()))
                        return b.getPassedCount() - a.getPassedCount();
                    if ("TIMED".equalsIgnoreCase(mode))
                        return Double.compare(b.getAccuracy(), a.getAccuracy());
                    else
                        return Long.compare(a.getTimeMs() != null ? a.getTimeMs() : Long.MAX_VALUE,
                                b.getTimeMs() != null ? b.getTimeMs() : Long.MAX_VALUE);
                })
                .limit(100)
                .collect(Collectors.toList());

        List<ChallengeLeaderboardEntryDto> result = new ArrayList<>();
        for (int i = 0; i < best.size(); i++) {
            ChallengeRecord r = best.get(i);
            String username = userRepository.findById(r.getUserId())
                    .map(User::getUsername).orElse("Unknown");
            result.add(ChallengeLeaderboardEntryDto.builder()
                    .rank(i + 1)
                    .userId(r.getUserId())
                    .username(username)
                    .passedCount(r.getPassedCount())
                    .accuracy(r.getAccuracy())
                    .timeMs(r.getTimeMs())
                    .wpm(r.getWpm())
                    .createdAt(r.getCreatedAt())
                    .isCurrentUser(currentUserId != null && r.getUserId().equals(currentUserId))
                    .build());
        }

        return ResponseEntity.ok(ApiResult.success(result));
    }

    /**
     * 获取历史记录
     * GET /challenge/history?page=0&size=10
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResult<List<ChallengeRecord>>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        List<ChallengeRecord> history = challengeRecordRepository
                .findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResult.success(history));
    }

    public record ApiResult<T>(int code, String message, T data) {
        public static <T> ApiResult<T> success(T data) {
            return new ApiResult<>(200, "success", data);
        }
    }
}
