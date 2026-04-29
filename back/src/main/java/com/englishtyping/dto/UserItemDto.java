package com.englishtyping.dto;

import com.englishtyping.entity.UserItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户道具 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItemDto {

    private Long id;

    /** 道具 ID */
    private Long giftId;

    /** 道具名称 */
    private String giftName;

    /** 道具图标 */
    private String giftIcon;

    /** 状态 */
    private UserItemStatus status;

    /** 兑换时间 */
    private LocalDateTime redeemedAt;

    /** 使用时间 */
    private LocalDateTime usedAt;
}
