package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 排行榜称号实体
 */
@Entity
@Table(name = "leaderboard_titles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 称号名称 */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /** 最小排名 */
    @Column(name = "min_rank", nullable = false)
    private Integer minRank;

    /** 最大排名 */
    @Column(name = "max_rank", nullable = false)
    private Integer maxRank;

    /** 称号图标（emoji） */
    @Column(name = "icon", length = 50)
    private String icon;

    /** 称号颜色 */
    @Column(name = "color", length = 20)
    private String color;

    /** 排序（越小越靠前） */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}