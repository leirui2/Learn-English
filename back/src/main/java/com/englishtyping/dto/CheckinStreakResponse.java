package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打卡连续天数响应 DTO
 */
@Data
@Builder
public class CheckinStreakResponse {

    /** 当前连续打卡天数 */
    private Integer streak;

    /** 最后打卡日期 */
    private LocalDateTime lastCheckinDate;
}
