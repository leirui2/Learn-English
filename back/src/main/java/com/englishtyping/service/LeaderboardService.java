package com.englishtyping.service;

import com.englishtyping.dto.LeaderboardResponse;

/**
 * 排行榜服务接口
 */
public interface LeaderboardService {

    /**
     * 获取排行榜
     * @param type 排行榜类型（score/streak）
     * @param period 时间维度（week/all）
     * @param categoryId 难度分类 ID（可选）
     * @param currentUserId 当前登录用户 ID
     * @return 排行榜响应
     */
    LeaderboardResponse getLeaderboard(String type, String period, Integer categoryId, String currentUserId);
}
