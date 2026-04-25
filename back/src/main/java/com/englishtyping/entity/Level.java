package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 关卡实体类
 */
@Entity
@Table(name = "levels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "level_order", nullable = false)
    private Integer levelOrder;

    @Column(name = "standard_time_ms", nullable = false)
    private Long standardTimeMs;
}