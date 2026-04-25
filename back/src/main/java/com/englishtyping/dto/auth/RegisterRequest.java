package com.englishtyping.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求 DTO
 */
@Data
public class RegisterRequest {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度需在 2-50 个字符之间")
    private String username;

    /** 邮箱 */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不合法")
    private String email;

    /** 密码（明文，至少 8 位） */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码至少需要 8 个字符")
    private String password;
}
