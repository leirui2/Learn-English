package com.englishtyping.service;

import com.englishtyping.dto.auth.AuthResponse;
import com.englishtyping.dto.auth.ForgotPasswordRequest;
import com.englishtyping.dto.auth.LoginRequest;
import com.englishtyping.dto.auth.RegisterRequest;
import com.englishtyping.dto.auth.ResetPasswordRequest;
import com.englishtyping.dto.auth.UserDto;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param request 注册请求（用户名、邮箱、密码）
     * @return 认证响应（access token + refresh token + 用户信息）
     */
    AuthResponse register(RegisterRequest request);

    /**
     * 用户登录
     *
     * @param request 登录请求（邮箱、密码）
     * @return 认证响应（access token + refresh token + 用户信息）
     */
    AuthResponse login(LoginRequest request);

    /**
     * 刷新 access token
     *
     * @param refreshToken 有效的 refresh token
     * @return 新的 access token
     */
    String refresh(String refreshToken);

    /**
     * 退出登录（将 refresh token 加入 Redis 黑名单）
     *
     * @param refreshToken 要失效的 refresh token
     */
    void logout(String refreshToken);

    /**
     * 申请密码重置（发送重置邮件）
     *
     * @param request 包含注册邮箱的请求
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * 验证令牌并重置密码
     *
     * @param request 包含令牌、新密码、确认密码的请求
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * 获取当前登录用户信息
     *
     * @param userId 从 JWT 中提取的用户 ID
     * @return 用户信息 DTO
     */
    UserDto getCurrentUser(String userId);
}
