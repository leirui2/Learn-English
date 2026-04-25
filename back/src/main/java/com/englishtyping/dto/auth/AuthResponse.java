package com.englishtyping.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应 DTO（注册/登录成功后返回）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** JWT access token（有效期 2 小时） */
    private String accessToken;

    /** JWT refresh token（有效期 7 天） */
    private String refreshToken;

    /** 用户基本信息 */
    private UserDto user;
}
