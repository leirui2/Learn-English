package com.englishtyping.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新分类请求 DTO
 */
@Data
public class UpdateCategoryRequest {

    /** 分类名称 */
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /** 分类描述 */
    private String description;

    /** 难度等级 */
    @NotNull(message = "难度等级不能为空")
    private Integer difficultyLevel;
}
