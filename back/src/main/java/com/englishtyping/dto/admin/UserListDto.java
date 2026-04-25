package com.englishtyping.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户列表 DTO（管理员视图）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {

    /** 用户 ID */
    private String id;

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 用户角色 */
    private String role;

    /** 账号状态 */
    private String status;

    /** 累计总积分 */
    private Integer totalScore;

    /** 连续打卡天数 */
    private Integer streak;

    /** 注册时间 */
    private LocalDateTime createdAt;

    /** 封禁时间 */
    private LocalDateTime bannedAt;

    /** 封禁原因 */
    private String bannedReason;
}
