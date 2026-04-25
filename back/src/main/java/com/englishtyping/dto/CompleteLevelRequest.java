package com.englishtyping.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 完成关卡请求 DTO
 */
@Data
public class CompleteLevelRequest {

    /** 正确率（0-100） */
    @NotNull(message = "正确率不能为空")
    @Min(value = 0, message = "正确率不能小于0")
    @Max(value = 100, message = "正确率不能大于100")
    private Double accuracy;

    /** 用时（毫秒） */
    @NotNull(message = "用时不能为空")
    @Min(value = 0, message = "用时不能小于0")
    private Long timeMs;

    /** 输入字符数 */
    private Integer charactersTyped;

    /** 错误字符统计（单词 -> 错误次数） */
    private Map<String, Integer> errorChars;
}
