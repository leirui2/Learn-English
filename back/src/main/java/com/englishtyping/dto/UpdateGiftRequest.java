package com.englishtyping.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新道具请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGiftRequest {

    private String name;

    private String description;

    private String icon;

    @Min(value = 1, message = "所需积分必须大于 0")
    private Integer pointsCost;

    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;
}
