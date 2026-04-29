package com.englishtyping.repository;

import com.englishtyping.entity.UserItem;
import com.englishtyping.entity.UserItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户道具背包 Repository
 */
@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    /**
     * 查询用户的所有道具（不分页）
     */
    List<UserItem> findAllByUserId(String userId);

    /**
     * 查询用户的未使用道具
     */
    List<UserItem> findByUserIdAndStatus(String userId, UserItemStatus status);

    /**
     * 查询用户兑换的某个道具的数量
     */
    long countByUserIdAndGiftId(String userId, Long giftId);

    /**
     * 查询用户的道具（分页，按兑换时间倒序）
     */
    Page<UserItem> findByUserIdOrderByRedeemedAtDesc(String userId, Pageable pageable);
}
