package com.englishtyping.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关卡 DTO（管理员视图）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelDto {

    /** 关卡 ID */
    private Integer id;

    /** 关卡名称 */
    private String name;

    /** 关卡描述 */
    private String description;

    /** 所属分类 ID */
    private Integer categoryId;

    /** 所属分类名称 */
    private String categoryName;

    /** 关卡序号 */
    private Integer orderIndex;

    /** 标准完成时间（秒） */
    private Integer standardTime;

    /** 该关卡下的练习内容数量 */
    private Long exerciseCount;
}
