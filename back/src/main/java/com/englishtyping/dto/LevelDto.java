package com.englishtyping.dto;

import com.englishtyping.entity.Exercise;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 关卡DTO
 * @author lei
 */
@Data
@Builder
public class LevelDto {

    /** 关卡 ID */
    private Integer id;

    /** 关卡名称 */
    private String name;

    /** 关卡描述 */
    private String description;

    /** 关卡序号 */
    private Integer levelOrder;

    /** 标准完成时间（毫秒） */
    private Long standardTimeMs;

    /** 是否解锁 */
    private Boolean unlocked;

    /** 是否完成 */
    private Boolean completed;

    /** 最佳正确率 */
    private Double bestAccuracy;

    /** 最佳用时（毫秒） */
    private Long bestTimeMs;

    /** 练习内容列表 */
    private List<Exercise> exercises;
}