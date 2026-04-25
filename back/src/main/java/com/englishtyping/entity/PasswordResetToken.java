package com.englishtyping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 密码重置令牌实体
 * 存储 SHA-256 哈希后的令牌，原始令牌仅在邮件中出现
 */
@Entity
@Table(name = "password_reset_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    /** 令牌 ID，使用 UUID */
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false)
    private String id;

    /** 关联的用户 ID */
    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId;

    /** SHA-256 哈希后的令牌值 */
    @Column(name = "token_hash", length = 64, nullable = false)
    private String tokenHash;

    /** 令牌过期时间 */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /** 是否已使用（使用后立即标记为 true，防止重放攻击） */
    @Column(name = "used", nullable = false)
    @Builder.Default
    private boolean used = false;

    /** 令牌创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
