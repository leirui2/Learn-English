package com.englishtyping.dto.admin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建称号请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeaderboardTitleRequest {

    @NotBlank(message = "称号名称不能为空")
    private String name;

    @NotNull(message = "最小排名不能为空")
    @Min(value = 1, message = "最小排名必须大于 0")
    private Integer minRank;

    @NotNull(message = "最大排名不能为空")
    @Min(value = 1, message = "最大排名必须大于 0")
    private Integer maxRank;

    private String icon;

    private String color;

    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能为负数")
    private Integer sortOrder;
}