package com.englishtyping.repository;

import com.englishtyping.entity.UserLevelProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户关卡进度数据访问接口
 */
@Repository
public interface UserLevelProgressRepository extends JpaRepository<UserLevelProgress, Integer> {

    /**
     * 根据用户 ID 查询所有关卡进度
     */
    List<UserLevelProgress> findByUserId(String userId);

    /**
     * 根据用户 ID 和关卡 ID 查询进度
     */
    UserLevelProgress findByUserIdAndLevelId(String userId, Integer levelId);

    /**
     * 根据用户 ID 删除所有关卡进度
     */
    void deleteByUserId(String userId);
}