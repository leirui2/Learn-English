package com.englishtyping.dto;

import com.englishtyping.entity.GiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 道具 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftDto {

    private Long id;

    /** 道具名称 */
    private String name;

    /** 道具描述 */
    private String description;

    /** 道具图标 URL */
    private String icon;

    /** 兑换所需积分 */
    private Integer pointsCost;

    /** 库存数量 */
    private Integer stock;

    /** 状态 */
    private GiftStatus status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
