package com.englishtyping.service;

import com.englishtyping.dto.UserStatsResponse;
import com.englishtyping.dto.WeakWordDto;

import java.util.List;

/**
 * 统计服务接口
 */
public interface StatsService {

    /**
     * 获取用户个人统计数据
     */
    UserStatsResponse getUserStats(String userId);

    /**
     * 通过用户名获取统计数据
     */
    UserStatsResponse getUserStatsByUsername(String username);

    /**
     * 获取用户易错词汇 Top 10
     */
    List<WeakWordDto> getWeakWords(String userId);
}
