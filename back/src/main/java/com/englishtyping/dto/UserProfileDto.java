package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 用户个人信息 DTO（含扩展字段）
 */
@Data
@Builder
public class UserProfileDto {

    private String id;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    private String bio;
    private String gender;
    private String phone;
    private Integer totalScore;
    private Integer streak;
    private String role;
    private String status;
}
