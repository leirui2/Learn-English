package com.englishtyping.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息 DTO（对外暴露，不含敏感字段）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /** 用户 ID */
    private String id;

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 累计总积分 */
    private Integer totalScore;

    /** 连续打卡天数 */
    private Integer streak;

    /** 用户角色（USER/ADMIN） */
    private String role;

    /** 账号状态（ACTIVE/BANNED） */
    private String status;
}
