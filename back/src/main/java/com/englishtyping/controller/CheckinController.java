package com.englishtyping.controller;

import com.englishtyping.dto.CheckinCalendarResponse;
import com.englishtyping.dto.CheckinStreakResponse;
import com.englishtyping.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 打卡控制器
 */
@RestController
@RequestMapping("/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    /**
     * 获取打卡日历（过去 30 天的打卡日期列表）
     */
    @GetMapping("/calendar")
    public ResponseEntity<CheckinCalendarResponse> getCheckinCalendar(Authentication authentication) {
        String userId = authentication.getName();
        CheckinCalendarResponse response = checkinService.getCheckinCalendar(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取当前连续打卡天数
     */
    @GetMapping("/streak")
    public ResponseEntity<CheckinStreakResponse> getCheckinStreak(Authentication authentication) {
        String userId = authentication.getName();
        CheckinStreakResponse response = checkinService.getCheckinStreak(userId);
        return ResponseEntity.ok(response);
    }
}
