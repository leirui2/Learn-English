package com.englishtyping.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 练习内容 DTO（管理员视图）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    /** 练习内容 ID */
    private Integer id;

    /** 所属关卡 ID */
    private Integer levelId;

    /** 所属关卡名称 */
    private String levelName;

    /** 英文内容 */
    private String contentEn;

    /** 中文释义 */
    private String contentZh;

    /** 练习序号 */
    private Integer orderIndex;
}
