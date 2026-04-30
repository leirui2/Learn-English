package com.englishtyping.service;

import com.englishtyping.dto.LeaderboardResponse;
import com.englishtyping.dto.LeaderboardTitleDto;

import java.util.List;

/**
 * 排行榜服务接口
 */
public interface LeaderboardService {

    /**
     * 获取排行榜
     * @param type 排行榜类型（score/streak）
     * @param period 时间维度（week/all）
     * @param category 难度分类 ID（可选）
     * @param currentUserId 当前用户 ID
     * @return 排行榜响应
     */
    LeaderboardResponse getLeaderboard(String type, String period, Integer category, String currentUserId);

    /**
     * 获取所有称号（按排序字段升序）
     * @return 称号列表
     */
    List<LeaderboardTitleDto> getAllTitles();

    /**
     * 根据排名获取对应的称号
     * @param rank 排名
     * @return 称号 DTO，如果没有匹配的称号则返回 null
     */
    LeaderboardTitleDto getTitleByRank(Integer rank);
}