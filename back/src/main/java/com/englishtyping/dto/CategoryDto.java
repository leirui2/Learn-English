package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 分类DTO
 * @author lei
 */
@Data
@Builder
public class CategoryDto {

    /** 分类 ID */
    private Integer id;

    /** 分类名称 */
    private String name;

    /** 分类描述 */
    private String description;

    /** 难度等级 */
    private Integer difficulty;

    /** 关卡列表 */
    private List<LevelDto> levels;
}