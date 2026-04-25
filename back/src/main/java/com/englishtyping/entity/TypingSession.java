package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 打字会话实体类
 */
@Entity
@Table(name = "typing_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypingSession {

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

    @Column(name = "accuracy", nullable = false)
    private Double accuracy;

    @Column(name = "time_ms", nullable = false)
    private Long timeMs;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "characters_typed")
    private Integer charactersTyped;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
