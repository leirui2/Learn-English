package com.englishtyping.repository;

import com.englishtyping.entity.LeaderboardTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 排行榜称号 Repository
 */
@Repository
public interface LeaderboardTitleRepository extends JpaRepository<LeaderboardTitle, Long> {

    /**
     * 查询所有称号，按排序字段升序
     */
    List<LeaderboardTitle> findAllByOrderBySortOrderAsc();

    /**
     * 根据排名查询对应的称号
     */
    LeaderboardTitle findByMinRankLessThanEqualAndMaxRankGreaterThanEqualOrderBySortOrderAsc(Integer minRank, Integer maxRank);
}