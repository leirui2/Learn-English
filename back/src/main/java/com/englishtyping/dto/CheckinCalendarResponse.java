package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 打卡日历响应 DTO
 */
@Data
@Builder
public class CheckinCalendarResponse {

    /** 打卡日期列表（过去 30 天） */
    private List<LocalDate> checkinDates;

    /** 当前连续打卡天数 */
    private Integer streak;
}
