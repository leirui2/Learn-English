package com.englishtyping.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类 DTO（管理员视图）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    /** 分类 ID */
    private Integer id;

    /** 分类名称 */
    private String name;

    /** 分类描述 */
    private String description;

    /** 难度等级 */
    private Integer difficultyLevel;

    /** 该分类下的关卡数量 */
    private Long levelCount;
}
