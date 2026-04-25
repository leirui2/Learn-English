package com.englishtyping.repository;

import com.englishtyping.entity.AdminOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 管理员操作日志 Repository
 */
@Repository
public interface AdminOperationLogRepository extends JpaRepository<AdminOperationLog, Integer> {

    /**
     * 根据条件查询操作日志（分页）
     */
    @Query("SELECT l FROM AdminOperationLog l WHERE " +
            "(:operatorId IS NULL OR l.operatorId = :operatorId) AND " +
            "(:operationType IS NULL OR l.operationType = :operationType) AND " +
            "(:targetType IS NULL OR l.targetType = :targetType) AND " +
            "(:startDate IS NULL OR l.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR l.createdAt <= :endDate) " +
            "ORDER BY l.createdAt DESC")
    Page<AdminOperationLog> findByConditions(
            @Param("operatorId") String operatorId,
            @Param("operationType") String operationType,
            @Param("targetType") String targetType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
