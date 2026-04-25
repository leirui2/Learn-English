package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 天梯挑战记录实体
 */
@Entity
@Table(name = "challenge_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId;

    /** 内容类型：WORD / SENTENCE */
    @Column(name = "content_type", nullable = false, length = 10)
    private String contentType;

    /** 时间模式：TIMED / INFINITE */
    @Column(name = "time_mode", nullable = false, length = 10)
    private String timeMode;

    /** 限时模式的时长（秒），无限模式为 null */
    @Column(name = "time_limit")
    private Integer timeLimit;

    /** 通过数量 */
    @Column(name = "passed_count", nullable = false)
    private Integer passedCount;

    /** 正确率 */
    @Column(name = "accuracy", nullable = false)
    private Double accuracy;

    /** 无限模式用时（毫秒） */
    @Column(name = "time_ms")
    private Long timeMs;

    /** 每分钟单词数 */
    @Column(name = "wpm", nullable = false)
    private Integer wpm;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
