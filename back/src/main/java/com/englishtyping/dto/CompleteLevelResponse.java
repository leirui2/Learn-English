package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 完成关卡响应 DTO
 */
@Data
@Builder
public class CompleteLevelResponse {

    /** 正确率 */
    private Double accuracy;

    /** 用时（毫秒） */
    private Long timeMs;

    /** 获得积分 */
    private Integer score;

    /** 是否解锁下一关 */
    private Boolean nextLevelUnlocked;

    /** 下一关 ID（如果有） */
    private Integer nextLevelId;

    /** 是否完成打卡 */
    private Boolean checkedIn;

    /** 打卡奖励积分 */
    private Integer checkinBonus;
}
