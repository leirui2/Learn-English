package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户关卡进度实体类
 */
@Entity
@Table(name = "user_level_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "level_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Column(name = "unlocked", nullable = false)
    @Builder.Default
    private Boolean unlocked = false;

    @Column(name = "completed", nullable = false)
    @Builder.Default
    private Boolean completed = false;

    @Column(name = "best_accuracy")
    private Double bestAccuracy;

    @Column(name = "best_time_ms")
    private Long bestTimeMs;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}