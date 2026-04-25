package com.englishtyping.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 申请密码重置请求 DTO
 */
@Data
public class ForgotPasswordRequest {

    /** 注册邮箱 */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不合法")
    private String email;
}
