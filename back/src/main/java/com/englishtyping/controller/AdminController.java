package com.englishtyping.controller;

import com.englishtyping.dto.AdminPointsRecordPageResponse;
import com.englishtyping.dto.CreateGiftRequest;
import com.englishtyping.dto.GiftDto;
import com.englishtyping.dto.LeaderboardTitleDto;
import com.englishtyping.dto.UpdateGiftRequest;
import com.englishtyping.dto.admin.*;
import com.englishtyping.entity.PointsType;
import com.englishtyping.entity.UserRole;
import com.englishtyping.entity.UserStatus;
import com.englishtyping.service.AdminService;
import com.englishtyping.service.GiftService;
import com.englishtyping.service.PointsRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员控制器
 * 提供用户管理、题库管理等管理员功能
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final GiftService giftService;
    private final PointsRecordService pointsRecordService;

    /**
     * 获取用户列表（分页、搜索、筛选）
     * GET /admin/users
     *
     * @param keyword 搜索关键词（用户名或邮箱，可选）
     * @param role 角色筛选（USER/ADMIN，可选）
     * @param status 状态筛选（ACTIVE/BANNED，可选）8
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页大小（默认 20）
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResult<Page<UserListDto>>> getUserList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<UserListDto> userPage = adminService.getUserList(keyword, role, status, page, size);
        return ResponseEntity.ok(ApiResult.success(userPage));
    }

    /**
     * 获取用户详情
     * GET /admin/users/{id}
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResult<UserListDto>> getUserDetail(@PathVariable String id) {
        UserListDto userDetail = adminService.getUserDetail(id);
        return ResponseEntity.ok(ApiResult.success(userDetail));
    }

    /**
     * 封禁用户
     * PUT /admin/users/{id}/ban
     */
    @PutMapping("/users/{id}/ban")
    public ResponseEntity<ApiResult<Void>> banUser(
            @PathVariable String id,
            @Valid @RequestBody BanUserRequest request
    ) {
        adminService.banUser(id, request.getReason());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 解封用户
     * PUT /admin/users/{id}/unban
     */
    @PutMapping("/users/{id}/unban")
    public ResponseEntity<ApiResult<Void>> unbanUser(@PathVariable String id) {
        adminService.unbanUser(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 删除用户（级联删除相关数据）
     * DELETE /admin/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResult<Void>> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 重置用户密码
     * POST /admin/users/{id}/reset-password
     */
    @PostMapping("/users/{id}/reset-password")
    public ResponseEntity<ApiResult<Void>> resetUserPassword(
            @PathVariable String id,
            @Valid @RequestBody ResetPasswordByAdminRequest request
    ) {
        adminService.resetUserPassword(id, request.getNewPassword());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 修改用户角色
     * PUT /admin/users/{id}/role
     */
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResult<Void>> updateUserRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        UserRole role = UserRole.valueOf(request.getRole());
        adminService.updateUserRole(id, role);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    // ===== 分类管理 =====

    /**
     * 获取所有分类
     * GET /admin/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResult<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = adminService.getAllCategories();
        return ResponseEntity.ok(ApiResult.success(categories));
    }

    /**
     * 获取分类详情
     * GET /admin/categories/{id}
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResult<CategoryDto>> getCategoryDetail(@PathVariable Integer id) {
        CategoryDto category = adminService.getCategoryDetail(id);
        return ResponseEntity.ok(ApiResult.success(category));
    }

    /**
     * 创建新分类
     * POST /admin/categories
     */
    @PostMapping("/categories")
    public ResponseEntity<ApiResult<CategoryDto>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        CategoryDto category = adminService.createCategory(request);
        return ResponseEntity.ok(ApiResult.success(category));
    }

    /**
     * 更新分类信息
     * PUT /admin/categories/{id}
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResult<CategoryDto>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        CategoryDto category = adminService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResult.success(category));
    }

    /**
     * 删除分类（检查是否有关卡）
     * DELETE /admin/categories/{id}
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResult<Void>> deleteCategory(@PathVariable Integer id) {
        adminService.deleteCategory(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    // ===== 关卡管理 =====

    /**
     * 获取所有关卡（可按分类筛选）
     * GET /admin/levels
     *
     * @param categoryId 分类 ID（可选）
     */
    @GetMapping("/levels")
    public ResponseEntity<ApiResult<List<LevelDto>>> getAllLevels(
            @RequestParam(required = false) Integer categoryId
    ) {
        List<LevelDto> levels = adminService.getAllLevels(categoryId);
        return ResponseEntity.ok(ApiResult.success(levels));
    }

    /**
     * 获取关卡详情
     * GET /admin/levels/{id}
     */
    @GetMapping("/levels/{id}")
    public ResponseEntity<ApiResult<LevelDto>> getLevelDetail(@PathVariable Integer id) {
        LevelDto level = adminService.getLevelDetail(id);
        return ResponseEntity.ok(ApiResult.success(level));
    }

    /**
     * 创建新关卡
     * POST /admin/levels
     */
    @PostMapping("/levels")
    public ResponseEntity<ApiResult<LevelDto>> createLevel(
            @Valid @RequestBody CreateLevelRequest request
    ) {
        LevelDto level = adminService.createLevel(request);
        return ResponseEntity.ok(ApiResult.success(level));
    }

    /**
     * 更新关卡信息
     * PUT /admin/levels/{id}
     */
    @PutMapping("/levels/{id}")
    public ResponseEntity<ApiResult<LevelDto>> updateLevel(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateLevelRequest request
    ) {
        LevelDto level = adminService.updateLevel(id, request);
        return ResponseEntity.ok(ApiResult.success(level));
    }

    /**
     * 删除关卡（检查用户进度）
     * DELETE /admin/levels/{id}
     */
    @DeleteMapping("/levels/{id}")
    public ResponseEntity<ApiResult<Void>> deleteLevel(@PathVariable Integer id) {
        adminService.deleteLevel(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    // ===== 练习内容管理 =====

    /**
     * 获取练习内容列表（按关卡筛选）
     * GET /admin/exercises
     *
     * @param levelId 关卡 ID（可选）
     */
    @GetMapping("/exercises")
    public ResponseEntity<ApiResult<List<ExerciseDto>>> getAllExercises(
            @RequestParam(required = false) Integer levelId
    ) {
        List<ExerciseDto> exercises = adminService.getAllExercises(levelId);
        return ResponseEntity.ok(ApiResult.success(exercises));
    }

    /**
     * 获取练习内容详情
     * GET /admin/exercises/{id}
     */
    @GetMapping("/exercises/{id}")
    public ResponseEntity<ApiResult<ExerciseDto>> getExerciseDetail(@PathVariable Integer id) {
        ExerciseDto exercise = adminService.getExerciseDetail(id);
        return ResponseEntity.ok(ApiResult.success(exercise));
    }

    /**
     * 创建新练习内容
     * POST /admin/exercises
     */
    @PostMapping("/exercises")
    public ResponseEntity<ApiResult<ExerciseDto>> createExercise(
            @Valid @RequestBody CreateExerciseRequest request
    ) {
        ExerciseDto exercise = adminService.createExercise(request);
        return ResponseEntity.ok(ApiResult.success(exercise));
    }

    /**
     * 批量创建练习内容
     * POST /admin/exercises/batch
     */
    @PostMapping("/exercises/batch")
    public ResponseEntity<ApiResult<List<ExerciseDto>>> batchCreateExercises(
            @Valid @RequestBody BatchCreateExerciseRequest request
    ) {
        List<ExerciseDto> exercises = adminService.batchCreateExercises(request);
        return ResponseEntity.ok(ApiResult.success(exercises));
    }

    /**
     * 更新练习内容
     * PUT /admin/exercises/{id}
     */
    @PutMapping("/exercises/{id}")
    public ResponseEntity<ApiResult<ExerciseDto>> updateExercise(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateExerciseRequest request
    ) {
        ExerciseDto exercise = adminService.updateExercise(id, request);
        return ResponseEntity.ok(ApiResult.success(exercise));
    }

    /**
     * 删除练习内容
     * DELETE /admin/exercises/{id}
     */
    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<ApiResult<Void>> deleteExercise(@PathVariable Integer id) {
        adminService.deleteExercise(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    // ===== 操作日志查询 =====

    /**
     * 获取操作日志（分页、筛选）
     * GET /admin/logs
     *
     * @param operatorId 操作人 ID（可选）
     * @param operationType 操作类型（可选）
     * @param targetType 对象类型（可选）
     * @param startDate 开始时间（可选）
     * @param endDate 结束时间（可选）
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页大小（默认 20）
     */
    @GetMapping("/logs")
    public ResponseEntity<ApiResult<Page<AdminOperationLogDto>>> getOperationLogs(
            @RequestParam(required = false) String operatorId,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<AdminOperationLogDto> logs = adminService.getOperationLogs(
                operatorId, operationType, targetType, startDate, endDate, page, size
        );
        return ResponseEntity.ok(ApiResult.success(logs));
    }

    /**
     * 查询所有用户的积分记录（分页，支持多条件筛选）
     * GET /admin/points/records
     *
     * @param userId 用户ID（可选）
     * @param username 用户名（可选，模糊查询）
     * @param type 积分类型（可选）
     * @param startDate 开始时间（可选）
     * @param endDate 结束时间（可选）
     * @param page 页码（从0开始，默认0）
     * @param size 每页大小（默认20）
     */
    @GetMapping("/points/records")
    public ResponseEntity<ApiResult<AdminPointsRecordPageResponse>> getAllPointsRecords(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) PointsType type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        AdminPointsRecordPageResponse response = pointsRecordService.getAllPointsRecords(
                userId, username, type, startDate, endDate, page, size);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    // ===== 道具管理 =====

    /**
     * 获取所有道具（分页）
     * GET /admin/gifts
     *
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页大小（默认 20）
     */
    @GetMapping("/gifts")
    public ResponseEntity<ApiResult<Page<GiftDto>>> getAllGifts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GiftDto> gifts = giftService.getAllGifts(pageRequest);
        return ResponseEntity.ok(ApiResult.success(gifts));
    }

    /**
     * 获取道具详情
     * GET /admin/gifts/{id}
     */
    @GetMapping("/gifts/{id}")
    public ResponseEntity<ApiResult<GiftDto>> getGiftDetail(@PathVariable Long id) {
        GiftDto gift = giftService.getGiftDetail(id);
        return ResponseEntity.ok(ApiResult.success(gift));
    }

    /**
     * 创建道具
     * POST /admin/gifts
     */
    @PostMapping("/gifts")
    public ResponseEntity<ApiResult<GiftDto>> createGift(
            @Valid @RequestBody CreateGiftRequest request
    ) {
        GiftDto gift = giftService.createGift(request);
        return ResponseEntity.ok(ApiResult.success(gift));
    }

    /**
     * 更新道具
     * PUT /admin/gifts/{id}
     */
    @PutMapping("/gifts/{id}")
    public ResponseEntity<ApiResult<GiftDto>> updateGift(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGiftRequest request
    ) {
        GiftDto gift = giftService.updateGift(id, request);
        return ResponseEntity.ok(ApiResult.success(gift));
    }

    /**
     * 删除道具
     * DELETE /admin/gifts/{id}
     */
    @DeleteMapping("/gifts/{id}")
    public ResponseEntity<ApiResult<Void>> deleteGift(@PathVariable Long id) {
        giftService.deleteGift(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 上架道具
     * PUT /admin/gifts/{id}/on-shelf
     */
    @PutMapping("/gifts/{id}/on-shelf")
    public ResponseEntity<ApiResult<Void>> onShelf(@PathVariable Long id) {
        giftService.onShelf(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 下架道具
     * PUT /admin/gifts/{id}/off-shelf
     */
    @PutMapping("/gifts/{id}/off-shelf")
    public ResponseEntity<ApiResult<Void>> offShelf(@PathVariable Long id) {
        giftService.offShelf(id);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 上传道具图片
     * POST /admin/gifts/{id}/image
     */
    @PostMapping("/gifts/{id}/image")
    public ResponseEntity<ApiResult<String>> uploadGiftImage(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) {
        String imageUrl = giftService.uploadGiftImage(id, file);
        return ResponseEntity.ok(ApiResult.success(imageUrl));
    }

    // ===== 称号管理 =====
    
    /**
     * 获取所有称号
     * GET /admin/leaderboard-titles
     */
    @GetMapping("/leaderboard-titles")
    public ResponseEntity<ApiResult<List<LeaderboardTitleDto>>> getAllTitles() {
        List<LeaderboardTitleDto> titles = adminService.getAllLeaderboardTitles();
        return ResponseEntity.ok(ApiResult.success(titles));
    }

    /**
     * 获取称号详情
     * GET /admin/leaderboard-titles/{id}
     */
    @GetMapping("/leaderboard-titles/{id}")
    public ResponseEntity<ApiResult<LeaderboardTitleDto>> getTitleDetail(@PathVariable Long id) {
        LeaderboardTitleDto title = adminService.getLeaderboardTitleDetail(id);
        return ResponseEntity.ok(ApiResult.success(title));
    }

    /**
     * 创建称号
     * POST /admin/leaderboard-titles
     */
    @PostMapping("/leaderboard-titles")
    public ResponseEntity<ApiResult<LeaderboardTitleDto>> createTitle(
            @Valid @RequestBody CreateLeaderboardTitleRequest request
    ) {
        LeaderboardTitleDto title = adminService.createLeaderboardTitle(request);
        return ResponseEntity.ok(ApiResult.success(title));
    }

    /**
     * 更新称号
     * PUT /admin/leaderboard-titles/{id}
     */
    @PutMapping("/leaderboard-titles/{id}")
    public ResponseEntity<ApiResult<LeaderboardTitleDto>> updateTitle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLeaderboardTitleRequest request
    ) {
        LeaderboardTitleDto title = adminService.updateLeaderboardTitle(id, request);
        return ResponseEntity.ok(ApiResult.success(title));
    }

    /**
     * 删除称号
     * DELETE /admin/leaderboard-titles/{id}
     */
    @DeleteMapping("/leaderboard-titles/{id}")
    public ResponseEntity<ApiResult<Void>> deleteTitle(@PathVariable Long id) {
        adminService.deleteLeaderboardTitle(id);
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