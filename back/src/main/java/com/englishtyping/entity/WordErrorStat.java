package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 单词错误统计实体类
 */
@Entity
@Table(name = "word_error_stats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "word"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordErrorStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "word", nullable = false, length = 100)
    private String word;

    @Column(name = "error_count", nullable = false)
    @Builder.Default
    private Integer errorCount = 0;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
