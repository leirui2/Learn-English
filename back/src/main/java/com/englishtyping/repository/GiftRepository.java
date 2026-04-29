package com.englishtyping.repository;

import com.englishtyping.entity.Gift;
import com.englishtyping.entity.GiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 虚拟道具 Repository
 */
@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {

    /**
     * 查询所有上架的道具
     */
    List<Gift> findByStatus(GiftStatus status);

    /**
     * 分页查询所有道具（可按状态筛选）
     */
    Page<Gift> findByStatus(GiftStatus status, Pageable pageable);

    /**
     * 分页查询所有道具（不筛选）
     */
    Page<Gift> findAll(Pageable pageable);
}
