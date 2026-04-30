package com.englishtyping.dto.admin;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新称号请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLeaderboardTitleRequest {

    private String name;

    private Integer minRank;

    private Integer maxRank;

    private String icon;

    private String color;

    @Min(value = 0, message = "排序不能为负数")
    private Integer sortOrder;
}