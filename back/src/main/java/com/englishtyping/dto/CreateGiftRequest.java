package com.englishtyping.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建道具请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGiftRequest {

    @NotBlank(message = "道具名称不能为空")
    private String name;

    private String description;

    private String icon;

    @NotNull(message = "所需积分不能为空")
    @Min(value = 1, message = "所需积分必须大于 0")
    private Integer pointsCost;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;
}
