package com.englishtyping.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新关卡请求 DTO
 */
@Data
public class UpdateLevelRequest {

    /** 关卡名称 */
    @NotBlank(message = "关卡名称不能为空")
    private String name;

    /** 关卡描述 */
    private String description;

    /** 所属分类 ID */
    @NotNull(message = "分类 ID 不能为空")
    private Integer categoryId;

    /** 关卡序号 */
    @NotNull(message = "关卡序号不能为空")
    private Integer orderIndex;

    /** 标准完成时间（秒） */
    @NotNull(message = "标准完成时间不能为空")
    private Integer standardTime;
}
