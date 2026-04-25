package com.englishtyping.repository;

import com.englishtyping.entity.DailyCheckin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 每日打卡数据访问接口
 */
@Repository
public interface DailyCheckinRepository extends JpaRepository<DailyCheckin, Integer> {

    /**
     * 根据用户 ID 和打卡日期查询打卡记录
     */
    Optional<DailyCheckin> findByUserIdAndCheckinDate(String userId, LocalDate checkinDate);

    /**
     * 根据用户 ID 和日期范围查询打卡记录
     */
    List<DailyCheckin> findByUserIdAndCheckinDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    /**
     * 根据用户 ID 删除所有打卡记录
     */
    void deleteByUserId(String userId);
}
