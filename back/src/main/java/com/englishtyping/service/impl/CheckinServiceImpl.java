package com.englishtyping.service.impl;

import com.englishtyping.dto.CheckinCalendarResponse;
import com.englishtyping.dto.CheckinStreakResponse;
import com.englishtyping.entity.DailyCheckin;
import com.englishtyping.entity.User;
import com.englishtyping.repository.DailyCheckinRepository;
import com.englishtyping.repository.UserRepository;
import com.englishtyping.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 打卡服务实现类
 */
@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {

    private final UserRepository userRepository;
    private final DailyCheckinRepository dailyCheckinRepository;

    @Override
    public CheckinCalendarResponse getCheckinCalendar(String userId) {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        // 计算过去 30 天的日期范围
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(29); // 包含今天，所以是 29 天前

        // 查询用户过去 30 天的所有打卡记录
        List<DailyCheckin> checkins = dailyCheckinRepository
                .findByUserIdAndCheckinDateBetween(userId, thirtyDaysAgo, today);

        // 提取打卡日期列表
        List<LocalDate> checkinDates = checkins.stream()
                .map(DailyCheckin::getCheckinDate)
                .sorted()
                .collect(Collectors.toList());

        return CheckinCalendarResponse.builder()
                .checkinDates(checkinDates)
                .streak(user.getStreak())
                .build();
    }

    @Override
    public CheckinStreakResponse getCheckinStreak(String userId) {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        return CheckinStreakResponse.builder()
                .streak(user.getStreak())
                .lastCheckinDate(user.getLastCheckinDate())
                .build();
    }
}
