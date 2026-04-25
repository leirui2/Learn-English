package com.englishtyping.repository;

import com.englishtyping.entity.WordErrorStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 单词错误统计数据访问接口
 */
@Repository
public interface WordErrorStatRepository extends JpaRepository<WordErrorStat, Integer> {

    /**
     * 根据用户 ID 和单词查询错误统计
     */
    Optional<WordErrorStat> findByUserIdAndWord(String userId, String word);

    /**
     * 根据用户 ID 查询所有错误统计，按错误次数降序排序
     */
    List<WordErrorStat> findByUserIdOrderByErrorCountDesc(String userId);

    /**
     * 根据用户 ID 删除所有错误统计
     */
    void deleteByUserId(String userId);
}
