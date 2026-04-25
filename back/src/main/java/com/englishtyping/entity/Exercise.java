package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 练习内容实体类
 */
@Entity
@Table(name = "exercises")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Column(name = "content_en", nullable = false, length = 500)
    private String contentEn;

    @Column(name = "content_zh", nullable = false, length = 500)
    private String contentZh;

    @Column(name = "exercise_order", nullable = false)
    private Integer exerciseOrder;
}