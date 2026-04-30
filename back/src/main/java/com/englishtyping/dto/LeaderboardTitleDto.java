package com.englishtyping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排行榜称号 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardTitleDto {

    private Long id;

    private String name;

    private Integer minRank;

    private Integer maxRank;

    private String icon;

    private String color;

    private Integer sortOrder;
}