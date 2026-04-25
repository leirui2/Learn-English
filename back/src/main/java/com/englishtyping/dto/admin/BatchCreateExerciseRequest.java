package com.englishtyping.dto.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 批量创建练习内容请求 DTO
 */
@Data
public class BatchCreateExerciseRequest {

    /** 所属关卡 ID */
    @NotNull(message = "关卡 ID 不能为空")
    private Integer levelId;

    /** 练习内容列表 */
    @NotEmpty(message = "练习内容列表不能为空")
    @Valid
    private List<ExerciseItem> exercises;

    @Data
    public static class ExerciseItem {
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
}
