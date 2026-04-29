package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 积分变动记录实体
 */
@Entity
@Table(name = "points_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId;

    /** 积分变动（正数=获得，负数=消耗） */
    @Column(name = "points", nullable = false)
    private Integer points;

    /** 变动类型 */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private PointsType type;

    /** 描述 */
    @Column(name = "description", length = 200)
    private String description;

    /** 关联ID（关卡ID/挑战记录ID/道具ID等） */
    @Column(name = "related_id")
    private Long relatedId;

    /** 变动后余额 */
    @Column(name = "balance_after")
    private Integer balanceAfter;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
