package com.englishtyping.repository;

import com.englishtyping.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关卡数据访问接口
 */
@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {

    /**
     * 根据分类 ID 查询关卡列表，按关卡序号排序
     */
    List<Level> findByCategoryIdOrderByLevelOrderAsc(Integer categoryId);

    /**
     * 统计指定关卡下的练习内容数量
     */
    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.level.id = :levelId")
    long countExercisesByLevelId(@Param("levelId") Integer levelId);

    /**
     * 统计指定分类下的关卡数量
     */
    long countByCategoryId(Integer categoryId);
}