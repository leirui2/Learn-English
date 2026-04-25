package com.englishtyping.controller;

import com.englishtyping.dto.UpdateProfileRequest;
import com.englishtyping.dto.UserProfileDto;
import com.englishtyping.dto.auth.UserDto;
import com.englishtyping.entity.User;
import com.englishtyping.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.englishtyping.service.AuthService;

/**
 * 用户控制器
 * @author lei
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    /**
     * 获取当前登录用户信息
     * GET /users/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResult<UserDto>> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResult<>(401, "未登录或登录已过期", null));
        }
        String userId = (String) authentication.getPrincipal();
        UserDto userDto = authService.getCurrentUser(userId);
        return ResponseEntity.ok(ApiResult.success(userDto));
    }

    /**
     * 获取当前用户完整个人信息（含头像、简介等）
     * GET /users/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResult<UserProfileDto>> getProfile(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        return ResponseEntity.ok(ApiResult.success(toProfileDto(user)));
    }

    /**
     * 修改个人信息（用户名、简介、性别、手机号）
     * PUT /users/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResult<UserProfileDto>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        // 用户名不允许修改，只能修改昵称、简介、性别、手机号
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);
        return ResponseEntity.ok(ApiResult.success(toProfileDto(user)));
    }

    /**
     * 上传/更换头像
     * POST /users/avatar
     */
    @PostMapping("/avatar")
    public ResponseEntity<ApiResult<String>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "只允许上传图片文件");
        }

        // 上传到 MinIO，路径按 userId 区分
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("avatars/" + userId + "/")
                .upload();

        if (fileInfo == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "头像上传失败");
        }

        // 更新用户头像 URL
        user.setAvatarUrl(fileInfo.getUrl());
        userRepository.save(user);

        return ResponseEntity.ok(ApiResult.success(fileInfo.getUrl()));
    }

    private UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .gender(user.getGender())
                .phone(user.getPhone())
                .totalScore(user.getTotalScore())
                .streak(user.getStreak())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }

    public record ApiResult<T>(int code, String message, T data) {
        public static <T> ApiResult<T> success(T data) {
            return new ApiResult<>(200, "success", data);
        }
    }
}
