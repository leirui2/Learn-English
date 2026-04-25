package com.englishtyping.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 封禁用户请求 DTO
 */
@Data
public class BanUserRequest {

    /** 封禁原因 */
    @NotBlank(message = "封禁原因不能为空")
    private String reason;
}
