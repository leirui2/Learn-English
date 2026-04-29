package com.englishtyping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 兑换道具响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeGiftResponse {

    /** 是否兑换成功 */
    private Boolean success;

    /** 道具名称 */
    private String giftName;

    /** 消耗积分 */
    private Integer pointsSpent;

    /** 剩余积分 */
    private Integer remainingPoints;

    /** 提示信息 */
    private String message;
}
