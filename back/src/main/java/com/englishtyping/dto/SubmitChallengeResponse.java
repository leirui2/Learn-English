package com.englishtyping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提交挑战结果响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitChallengeResponse {

    /** 是否完成今日打卡 */
    private Boolean checkedIn;

    /** 打卡奖励积分（连续7天时） */
    private Integer checkinBonus;
}
