package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 排行榜条目 DTO
 */
@Data
@Builder
public class LeaderboardEntryDto {

    /** 排名 */
    private Integer rank;

    /** 用户 ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 积分（总积分榜使用） */
    private Integer score;

    /** 连续打卡天数（连续打卡榜使用） */
    private Integer streak;

    /** 是否为当前用户 */
    private Boolean isCurrentUser;
}
