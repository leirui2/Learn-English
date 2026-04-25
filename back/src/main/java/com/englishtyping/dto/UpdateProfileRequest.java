package com.englishtyping.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改个人信息请求 DTO
 */
@Data
public class UpdateProfileRequest {

    @Size(max = 50, message = "昵称不能超过50个字符")
    private String nickname;

    @Size(max = 500, message = "个人简介不能超过500个字符")
    private String bio;

    /** MALE / FEMALE / OTHER */
    private String gender;

    @Size(max = 20, message = "手机号不能超过20个字符")
    private String phone;
}
