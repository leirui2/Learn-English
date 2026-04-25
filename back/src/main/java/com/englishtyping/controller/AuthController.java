package com.englishtyping.controller;

import com.englishtyping.dto.auth.AuthResponse;
import com.englishtyping.dto.auth.ForgotPasswordRequest;
import com.englishtyping.dto.auth.LoginRequest;
import com.englishtyping.dto.auth.RegisterRequest;
import com.englishtyping.dto.auth.ResetPasswordRequest;
import com.englishtyping.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 提供注册、登录、token 刷新、退出登录接口
 * @author lei
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     * POST /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResult<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    /**
     * 用户登录
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResult<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    /**
     * 刷新 access token
     * POST /auth/refresh
     * 请求体：{"refreshToken": "..."}
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResult<Map<String, String>>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        String newAccessToken = authService.refresh(refreshToken);
        return ResponseEntity.ok(ApiResult.success(Map.of("accessToken", newAccessToken)));
    }

    /**
     * 退出登录（需认证）
     * POST /auth/logout
     * 请求体：{"refreshToken": "..."}
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<Void>> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        authService.logout(refreshToken);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 申请密码重置（发送重置邮件）
     * POST /auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResult<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 验证令牌并重置密码
     * POST /auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResult<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }


    // ===== 内部统一响应包装类 =====

    /**
     * 统一 API 响应格式
     */
    public record ApiResult<T>(int code, String message, T data) {
        public static <T> ApiResult<T> success(T data) {
            return new ApiResult<>(200, "success", data);
        }
    }
}
