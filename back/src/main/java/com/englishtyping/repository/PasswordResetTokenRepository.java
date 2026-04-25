package com.englishtyping.repository;

import com.englishtyping.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 密码重置令牌数据访问层
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    /**
     * 根据令牌哈希值查找令牌记录
     *
     * @param tokenHash SHA-256 哈希后的令牌值
     * @return 令牌记录（可能为空）
     */
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    /**
     * 统计指定用户在某时间点之后创建的令牌数量（用于限流）
     *
     * @param userId 用户 ID
     * @param after  时间下限（不含）
     * @return 令牌数量
     */
    long countByUserIdAndCreatedAtAfter(String userId, LocalDateTime after);
}
