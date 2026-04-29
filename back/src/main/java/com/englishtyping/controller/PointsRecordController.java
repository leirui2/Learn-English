package com.englishtyping.controller;

import com.englishtyping.dto.PointsRecordPageResponse;
import com.englishtyping.entity.PointsType;
import com.englishtyping.service.PointsRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 积分记录控制器
 */
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointsRecordController {

    private final PointsRecordService pointsRecordService;

    /**
     * 查询当前用户的积分记录
     * GET /points/records?page=0&size=20&type=LEVEL_COMPLETE
     *
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param type 积分类型（可选）
     * @param authentication 当前用户认证信息
     * @return 积分记录分页响应
     */
    @GetMapping("/records")
    public ResponseEntity<ApiResult<PointsRecordPageResponse>> getMyPointsRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) PointsType type,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        PointsRecordPageResponse response = pointsRecordService.getUserPointsRecords(userId, page, size, type);

        return ResponseEntity.ok(ApiResult.success(response));
    }

    public record ApiResult<T>(int code, String message, T data) {
        public static <T> ApiResult<T> success(T data) {
            return new ApiResult<>(200, "success", data);
        }
    }
}
