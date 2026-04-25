package com.englishtyping.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新练习内容请求 DTO
 */
@Data
public class UpdateExerciseRequest {

    /** 所属关卡 ID */
    @NotNull(message = "关卡 ID 不能为空")
    private Integer levelId;

    /** 英文内容 */
    @NotBlank(message = "英文内容不能为空")
    private String contentEn;

    /** 中文释义 */
    @NotBlank(message = "中文释义不能为空")
    private String contentZh;

    /** 练习序号 */
    @NotNull(message = "练习序号不能为空")
    private Integer orderIndex;
}
