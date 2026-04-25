package com.englishtyping.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 修改用户角色请求 DTO
 */
@Data
public class UpdateUserRoleRequest {

    /** 新角色（USER 或 ADMIN） */
    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "USER|ADMIN", message = "角色只能是 USER 或 ADMIN")
    private String role;
}
