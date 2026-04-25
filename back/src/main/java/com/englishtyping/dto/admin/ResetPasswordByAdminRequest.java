package com.englishtyping.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员重置用户密码请求 DTO
 */
@Data
public class ResetPasswordByAdminRequest {

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "密码至少需要 8 个字符")
    private String newPassword;
}
