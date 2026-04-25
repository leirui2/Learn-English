package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 管理员操作日志实体类
 */
@Entity
@Table(name = "admin_operation_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOperationLog {

    /** 日志 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 操作人 ID */
    @Column(name = "operator_id", nullable = false, columnDefinition = "CHAR(36)")
    private String operatorId;

    /** 操作人用户名 */
    @Column(name = "operator_name", nullable = false, length = 50)
    private String operatorName;

    /** 操作类型 */
    @Column(name = "operation_type", nullable = false, length = 20)
    private String operationType;

    /** 操作对象类型 */
    @Column(name = "target_type", nullable = false, length = 20)
    private String targetType;

    /** 操作对象 ID */
    @Column(name = "target_id", nullable = false, length = 200)
    private String targetId;

    /** 操作详情（JSON） */
    @Column(name = "operation_detail", columnDefinition = "TEXT")
    private String operationDetail;

    /** 操作 IP 地址 */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /** 操作时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
