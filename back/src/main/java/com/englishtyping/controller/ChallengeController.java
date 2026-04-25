package com.englishtyping.controller;

import com.englishtyping.dto.*;
import com.englishtyping.entity.ChallengeRecord;
import com.englishtyping.entity.Exercise;
import com.englishtyping.entity.User;
import com.englishtyping.repository.ChallengeRecordRepository;
import com.englishtyping.repository.ExerciseRepository;
import com.englishtyping.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResult<Void>> submitChallenge(
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
        return ResponseEntity.ok(ApiResult.success(null));
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

        String currentUserId = (String) authentication.getPrincipal();
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
                    .isCurrentUser(r.getUserId().equals(currentUserId))
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
