package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 虚拟道具实体
 */
@Entity
@Table(name = "gifts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 道具名称 */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** 道具描述 */
    @Column(name = "description", length = 500)
    private String description;

    /** 道具图标 URL */
    @Column(name = "icon", length = 200)
    private String icon;

    /** 兑换所需积分 */
    @Column(name = "points_cost", nullable = false)
    private Integer pointsCost;

    /** 库存数量 */
    @Column(name = "stock", nullable = false)
    private Integer stock;

    /** 状态：ON_SHELF / OFF_SHELF */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private GiftStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
