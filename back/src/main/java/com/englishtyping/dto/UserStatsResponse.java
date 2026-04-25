package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 用户统计响应 DTO
 */
@Data
@Builder
public class UserStatsResponse {

    /** 累计练习时长（毫秒） */
    private Long totalPracticeTimeMs;

    /** 累计完成关卡数 */
    private Integer totalCompletedLevels;

    /** 累计输入字符数 */
    private Long totalCharactersTyped;

    /** 累计总积分 */
    private Integer totalScore;

    /** 连续打卡天数 */
    private Integer streak;

    /** 用户名 */
    private String username;
}
