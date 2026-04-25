package com.englishtyping.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类，负责生成和验证 JWT token
 */
@Component
public class JwtUtil {

    /** JWT 签名密钥（从配置文件读取） */
    private final SecretKey secretKey;

    /** access token 有效期（毫秒） */
    private final long accessTokenExpiration;

    /** refresh token 有效期（毫秒） */
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${app.jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * 生成 access token（包含用户角色）
     *
     * @param userId 用户 ID
     * @param role 用户角色
     * @return access token 字符串
     */
    public String generateAccessToken(String userId, String role) {
        return buildToken(userId, role, accessTokenExpiration);
    }

    /**
     * 生成 refresh token（包含用户角色）
     *
     * @param userId 用户 ID
     * @param role 用户角色
     * @return refresh token 字符串
     */
    public String generateRefreshToken(String userId, String role) {
        return buildToken(userId, role, refreshTokenExpiration);
    }

    /**
     * 验证 token 是否有效（未过期、签名正确）
     *
     * @param token JWT 字符串
     * @return true 表示有效
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 从 token 中提取用户 ID
     *
     * @param token JWT 字符串
     * @return 用户 ID
     */
    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 从 token 中提取用户角色
     *
     * @param token JWT 字符串
     * @return 用户角色
     */
    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * 获取 token 的剩余有效期（毫秒），用于设置 Redis TTL
     *
     * @param token JWT 字符串
     * @return 剩余毫秒数，若已过期则返回 0
     */
    public long getRemainingExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        long remaining = expiration.getTime() - System.currentTimeMillis();
        return Math.max(remaining, 0);
    }

    // ===== 私有方法 =====

    private String buildToken(String userId, String role, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
