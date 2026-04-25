package com.englishtyping.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 提交新密码请求 DTO
 */
@Data
public class ResetPasswordRequest {

    /** 重置令牌（URL 中的原始令牌） */
    @NotBlank(message = "令牌不能为空")
    private String token;

    /** 新密码（至少 8 位） */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "密码至少需要 8 个字符")
    private String newPassword;

    /** 确认密码 */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
