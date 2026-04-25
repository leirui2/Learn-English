package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日打卡实体类
 */
@Entity
@Table(name = "daily_checkins", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "checkin_date"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyCheckin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "checkin_date", nullable = false)
    private LocalDate checkinDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
