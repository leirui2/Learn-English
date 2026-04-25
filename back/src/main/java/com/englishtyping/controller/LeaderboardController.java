package com.englishtyping.controller;

import com.englishtyping.dto.LeaderboardResponse;
import com.englishtyping.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 排行榜控制器
 */
@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    /**
     * 获取排行榜
     * @param type 排行榜类型（score/streak）
     * @param period 时间维度（week/all）
     * @param category 难度分类 ID（可选）
     * @param authentication 认证信息
     * @return 排行榜响应
     */
    @GetMapping
    public ResponseEntity<LeaderboardResponse> getLeaderboard(
            @RequestParam(defaultValue = "score") String type,
            @RequestParam(defaultValue = "all") String period,
            @RequestParam(required = false) Integer category,
            Authentication authentication) {
        
        String currentUserId = authentication.getName();
        LeaderboardResponse response = leaderboardService.getLeaderboard(type, period, category, currentUserId);
        return ResponseEntity.ok(response);
    }
}
