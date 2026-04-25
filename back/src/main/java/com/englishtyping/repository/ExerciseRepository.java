package com.englishtyping.repository;

import com.englishtyping.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 练习内容数据访问接口
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    /**
     * 根据关卡 ID 查询练习内容列表，按练习序号排序
     */
    List<Exercise> findByLevelIdOrderByExerciseOrderAsc(Integer levelId);

    /**
     * 根据关卡 ID 分页查询练习内容
     */
    Page<Exercise> findByLevelIdOrderByExerciseOrderAsc(Integer levelId, Pageable pageable);

    /**
     * 统计指定关卡下的练习内容数量
     */
    long countByLevelId(Integer levelId);
}