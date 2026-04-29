package com.englishtyping.repository;

import com.englishtyping.entity.PointsRecord;
import com.englishtyping.entity.PointsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 积分记录 Repository
 */
@Repository
public interface PointsRecordRepository extends JpaRepository<PointsRecord, Long> {

    /**
     * 查询用户的积分记录（分页）
     */
    Page<PointsRecord> findByUserId(String userId, Pageable pageable);

    /**
     * 查询用户的积分记录（按类型筛选，分页）
     */
    Page<PointsRecord> findByUserIdAndType(String userId, PointsType type, Pageable pageable);

    /**
     * 查询所有用户的积分记录（分页）
     */
    Page<PointsRecord> findAll(Pageable pageable);

    /**
     * 查询指定用户的积分记录（按类型和时间范围筛选，分页）
     */
    Page<PointsRecord> findByUserIdAndTypeAndCreatedAtBetween(
            String userId, PointsType type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 查询指定用户的积分记录（按时间范围筛选，分页）
     */
    Page<PointsRecord> findByUserIdAndCreatedAtBetween(
            String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 按用户ID查询（管理员用，分页）
     */
    Page<PointsRecord> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * 按用户ID和类型查询（管理员用，分页）
     */
    Page<PointsRecord> findByUserIdAndTypeOrderByCreatedAtDesc(String userId, PointsType type, Pageable pageable);

    /**
     * 按用户名模糊查询（需要JOIN users表）
     */
    @Query("SELECT pr FROM PointsRecord pr JOIN User u ON pr.userId = u.id WHERE u.username LIKE %:username% ORDER BY pr.createdAt DESC")
    Page<PointsRecord> findByUsernameContaining(@Param("username") String username, Pageable pageable);

    /**
     * 按用户名和类型查询
     */
    @Query("SELECT pr FROM PointsRecord pr JOIN User u ON pr.userId = u.id WHERE u.username LIKE %:username% AND pr.type = :type ORDER BY pr.createdAt DESC")
    Page<PointsRecord> findByUsernameContainingAndType(@Param("username") String username, @Param("type") PointsType type, Pageable pageable);

    /**
     * 按用户名和时间范围查询
     */
    @Query("SELECT pr FROM PointsRecord pr JOIN User u ON pr.userId = u.id WHERE u.username LIKE %:username% AND pr.createdAt BETWEEN :startDate AND :endDate ORDER BY pr.createdAt DESC")
    Page<PointsRecord> findByUsernameContainingAndCreatedAtBetween(@Param("username") String username, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    /**
     * 按用户名、类型和时间范围查询
     */
    @Query("SELECT pr FROM PointsRecord pr JOIN User u ON pr.userId = u.id WHERE u.username LIKE %:username% AND pr.type = :type AND pr.createdAt BETWEEN :startDate AND :endDate ORDER BY pr.createdAt DESC")
    Page<PointsRecord> findByUsernameContainingAndTypeAndCreatedAtBetween(@Param("username") String username, @Param("type") PointsType type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
