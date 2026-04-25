package com.englishtyping.repository;

import com.englishtyping.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 分类数据访问接口
 * @author lei
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    /**
     * 统计指定分类下的关卡数量
     */
    @Query("SELECT COUNT(l) FROM Level l WHERE l.category.id = :categoryId")
    long countLevelsByCategoryId(@Param("categoryId") Integer categoryId);
}