package com.englishtyping.service;

import com.englishtyping.dto.AdminPointsRecordPageResponse;
import com.englishtyping.dto.PointsRecordPageResponse;
import com.englishtyping.entity.PointsType;

import java.time.LocalDateTime;

/**
 * 积分记录服务接口
 */
public interface PointsRecordService {

    /**
     * 查询用户的积分记录（分页）
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @param type 积分类型（可选）
     * @return 积分记录分页响应
     */
    PointsRecordPageResponse getUserPointsRecords(String userId, int page, int size, PointsType type);

    /**
     * 管理员查询所有用户的积分记录（分页，支持多条件筛选）
     *
     * @param userId 用户ID（可选）
     * @param username 用户名（可选，模糊查询）
     * @param type 积分类型（可选）
     * @param startDate 开始时间（可选）
     * @param endDate 结束时间（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 积分记录分页响应（包含用户信息）
     */
    AdminPointsRecordPageResponse getAllPointsRecords(
            String userId, String username, PointsType type,
            LocalDateTime startDate, LocalDateTime endDate,
            int page, int size);
}
