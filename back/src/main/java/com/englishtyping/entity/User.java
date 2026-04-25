package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** 用户 ID，使用 UUID */
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false)
    private String id;

    /** 用户名 */
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    /** 昵称（展示用，可修改） */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /** 头像 URL */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /** 个人简介 */
    @Column(name = "bio", length = 500)
    private String bio;

    /** 性别（MALE/FEMALE/OTHER） */
    @Column(name = "gender", length = 10)
    private String gender;

    /** 手机号 */
    @Column(name = "phone", length = 20)
    private String phone;

    /** 邮箱（唯一） */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /** BCrypt 加密后的密码哈希 */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** 用户角色 */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private UserRole role = UserRole.USER;

    /** 账号状态 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    /** 封禁时间 */
    @Column(name = "banned_at")
    private LocalDateTime bannedAt;

    /** 封禁原因 */
    @Column(name = "banned_reason", length = 500)
    private String bannedReason;

    /** 累计总积分 */
    @Column(name = "total_score", nullable = false)
    @Builder.Default
    private Integer totalScore = 0;

    /** 连续打卡天数 */
    @Column(name = "streak", nullable = false)
    @Builder.Default
    private Integer streak = 0;

    /** 最后打卡日期 */
    @Column(name = "last_checkin_date")
    private LocalDateTime lastCheckinDate;

    /** 账号创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
