package com.englishtyping.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求 DTO
 */
@Data
public class LoginRequest {

    /** 用户名或邮箱 */
    @NotBlank(message = "用户名或邮箱不能为空")
    private String identifier;

    /** 密码（明文） */
    @NotBlank(message = "密码不能为空")
    private String password;
}
