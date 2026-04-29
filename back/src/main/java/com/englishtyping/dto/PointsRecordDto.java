package com.englishtyping.dto;

import com.englishtyping.entity.PointsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 积分记录 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsRecordDto {

    /** 记录ID */
    private Long id;

    /** 积分变动（正数=获得，负数=消耗） */
    private Integer points;

    /** 变动类型 */
    private PointsType type;

    /** 描述 */
    private String description;

    /** 关联ID */
    private Long relatedId;

    /** 变动后余额 */
    private Integer balanceAfter;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
