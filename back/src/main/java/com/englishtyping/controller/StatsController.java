package com.englishtyping.controller;

import com.englishtyping.dto.UserStatsResponse;
import com.englishtyping.dto.WeakWordDto;
import com.englishtyping.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计控制器
 * @author lei
 */
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * 获取用户个人统计数据
     */
    @GetMapping("/me")
    public ResponseEntity<UserStatsResponse> getUserStats(Authentication authentication) {
        String userId = authentication.getName();
        UserStatsResponse response = statsService.getUserStats(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取指定用户的公开统计数据（通过用户名查询）
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<UserStatsResponse> getUserStatsByUsername(@PathVariable String username) {
        UserStatsResponse response = statsService.getUserStatsByUsername(username);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户易错词汇 Top 10
     */
    @GetMapping("/weak-words")
    public ResponseEntity<List<WeakWordDto>> getWeakWords(Authentication authentication) {
        String userId = authentication.getName();
        List<WeakWordDto> weakWords = statsService.getWeakWords(userId);
        return ResponseEntity.ok(weakWords);
    }
}
