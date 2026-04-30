package com.englishtyping.controller;

import com.englishtyping.dto.CategoryDto;
import com.englishtyping.dto.CompleteLevelRequest;
import com.englishtyping.dto.CompleteLevelResponse;
import com.englishtyping.dto.LevelDto;
import com.englishtyping.entity.Exercise;
import com.englishtyping.service.LevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 关卡控制器
 * @author lei
 */
@RestController
@RequestMapping("/levels")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService levelService;

    /**
     * 获取关卡列表
     */
    @GetMapping
    public ResponseEntity<ApiResult<List<CategoryDto>>> getLevelList(Authentication authentication) {
        // 支持游客访问：如果未认证，userId为null
        String userId = authentication != null ? authentication.getName() : null;
        List<CategoryDto> levelList = levelService.getLevelList(userId);
        return ResponseEntity.ok(ApiResult.success(levelList));
    }

    /**
     * 获取关卡详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<LevelDto>> getLevelDetail(@PathVariable("id") Integer levelId, Authentication authentication) {
        // 从认证信息中获取用户ID
        String userId = authentication.getName();
        LevelDto levelDetail = levelService.getLevelDetail(levelId, userId);
        return ResponseEntity.ok(ApiResult.success(levelDetail));
    }

    /**
     * 获取关卡的练习内容
     */
    @GetMapping("/{id}/exercises")
    public ResponseEntity<ApiResult<List<Exercise>>> getLevelExercises(@PathVariable("id") Integer levelId, Authentication authentication) {
        // 从认证信息中获取用户ID
        String userId = authentication.getName();
        List<Exercise> exercises = levelService.getLevelExercises(levelId, userId);
        return ResponseEntity.ok(ApiResult.success(exercises));
    }

    /**
     * 完成关卡
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResult<CompleteLevelResponse>> completeLevel(
            @PathVariable("id") Integer levelId,
            @Valid @RequestBody CompleteLevelRequest request,
            Authentication authentication) {
        // 从认证信息中获取用户ID
        String userId = authentication.getName();
        CompleteLevelResponse response = levelService.completeLevel(levelId, userId, request);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    /**
     * 统一 API 响应格式
     */
    public record ApiResult<T>(int code, String message, T data) {
        public static <T> ApiResult<T> success(T data) {
            return new ApiResult<>(200, "success", data);
        }
    }
}
