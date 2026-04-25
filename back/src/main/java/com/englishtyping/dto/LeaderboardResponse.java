package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 排行榜响应 DTO
 */
@Data
@Builder
public class LeaderboardResponse {

    /** 排行榜类型（score/streak） */
    private String type;

    /** 时间维度（week/all） */
    private String period;

    /** 难度分类（可选） */
    private Integer categoryId;

    /** 排行榜条目列表（前 100 名） */
    private List<LeaderboardEntryDto> entries;

    /** 当前用户的排名信息 */
    private LeaderboardEntryDto currentUserRank;
}
