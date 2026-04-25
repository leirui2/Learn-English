package com.englishtyping.service;

import com.englishtyping.dto.CheckinCalendarResponse;
import com.englishtyping.dto.CheckinStreakResponse;

/**
 * 打卡服务接口
 */
public interface CheckinService {

    /**
     * 获取打卡日历（过去 30 天的打卡日期列表）
     */
    CheckinCalendarResponse getCheckinCalendar(String userId);

    /**
     * 获取当前连续打卡天数
     */
    CheckinStreakResponse getCheckinStreak(String userId);
}
