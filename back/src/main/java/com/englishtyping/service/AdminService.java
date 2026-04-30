package com.englishtyping.service;

import com.englishtyping.dto.LeaderboardTitleDto;
import com.englishtyping.dto.admin.*;
import com.englishtyping.entity.UserRole;
import com.englishtyping.entity.UserStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员服务接口
 * @author lei
 */
public interface AdminService {

    // ===== 用户管理 =====
    
    /**
     * 获取用户列表（分页、搜索、筛选）
     *
     * @param keyword 搜索关键词（用户名或邮箱）
     * @param role 角色筛选
     * @param status 状态筛选
     * @param page 页码（从 0 开始）
     * @param size 每页大小
     * @return 用户列表分页数据
     */
    Page<UserListDto> getUserList(String keyword, UserRole role, UserStatus status, int page, int size);

    /**
     * 获取用户详情
     *
     * @param userId 用户 ID
     * @return 用户详情
     */
    UserListDto getUserDetail(String userId);

    /**
     * 封禁用户
     *
     * @param userId 用户 ID
     * @param reason 封禁原因
     */
    void banUser(String userId, String reason);

    /**
     * 解封用户
     *
     * @param userId 用户 ID
     */
    void unbanUser(String userId);

    /**
     * 删除用户（级联删除相关数据）
     *
     * @param userId 用户 ID
     */
    void deleteUser(String userId);

    /**
     * 重置用户密码
     *
     * @param userId 用户 ID
     * @param newPassword 新密码
     */
    void resetUserPassword(String userId, String newPassword);

    /**
     * 修改用户角色
     *
     * @param userId 用户 ID
     * @param role 新角色
     */
    void updateUserRole(String userId, UserRole role);

    // ===== 分类管理 =====
    
    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    List<com.englishtyping.dto.admin.CategoryDto> getAllCategories();

    /**
     * 获取分类详情
     *
     * @param id 分类 ID
     * @return 分类详情
     */
    com.englishtyping.dto.admin.CategoryDto getCategoryDetail(Integer id);

    /**
     * 创建新分类
     *
     * @param request 创建请求
     * @return 创建的分类
     */
    com.englishtyping.dto.admin.CategoryDto createCategory(CreateCategoryRequest request);

    /**
     * 更新分类信息
     *
     * @param id 分类 ID
     * @param request 更新请求
     * @return 更新后的分类
     */
    com.englishtyping.dto.admin.CategoryDto updateCategory(Integer id, UpdateCategoryRequest request);

    /**
     * 删除分类（检查是否有关卡）
     *
     * @param id 分类 ID
     */
    void deleteCategory(Integer id);

    // ===== 关卡管理 =====
    
    /**
     * 获取所有关卡（可按分类筛选）
     *
     * @param categoryId 分类 ID（可选）
     * @return 关卡列表
     */
    List<LevelDto> getAllLevels(Integer categoryId);

    /**
     * 获取关卡详情
     *
     * @param id 关卡 ID
     * @return 关卡详情
     */
    LevelDto getLevelDetail(Integer id);

    /**
     * 创建新关卡
     *
     * @param request 创建请求
     * @return 创建的关卡
     */
    LevelDto createLevel(CreateLevelRequest request);

    /**
     * 更新关卡信息
     *
     * @param id 关卡 ID
     * @param request 更新请求
     * @return 更新后的关卡
     */
    LevelDto updateLevel(Integer id, UpdateLevelRequest request);

    /**
     * 删除关卡（检查用户进度）
     *
     * @param id 关卡 ID
     */
    void deleteLevel(Integer id);

    // ===== 练习内容管理 =====
    
    /**
     * 获取练习内容列表（按关卡筛选）
     *
     * @param levelId 关卡 ID（可选）
     * @return 练习内容列表
     */
    List<ExerciseDto> getAllExercises(Integer levelId);

    /**
     * 获取练习内容详情
     *
     * @param id 练习内容 ID
     * @return 练习内容详情
     */
    ExerciseDto getExerciseDetail(Integer id);

    /**
     * 创建新练习内容
     *
     * @param request 创建请求
     * @return 创建的练习内容
     */
    ExerciseDto createExercise(CreateExerciseRequest request);

    /**
     * 批量创建练习内容
     *
     * @param request 批量创建请求
     * @return 创建的练习内容列表
     */
    List<ExerciseDto> batchCreateExercises(BatchCreateExerciseRequest request);

    /**
     * 更新练习内容
     *
     * @param id 练习内容 ID
     * @param request 更新请求
     * @return 更新后的练习内容
     */
    ExerciseDto updateExercise(Integer id, UpdateExerciseRequest request);

    /**
     * 删除练习内容
     *
     * @param id 练习内容 ID
     */
    void deleteExercise(Integer id);

    // ===== 称号管理 =====
    
    /**
     * 获取所有称号
     *
     * @return 称号列表
     */
    List<LeaderboardTitleDto> getAllLeaderboardTitles();

    /**
     * 获取称号详情
     *
     * @param id 称号 ID
     * @return 称号详情
     */
    LeaderboardTitleDto getLeaderboardTitleDetail(Long id);

    /**
     * 创建称号
     *
     * @param request 创建请求
     * @return 创建的称号
     */
    LeaderboardTitleDto createLeaderboardTitle(CreateLeaderboardTitleRequest request);

    /**
     * 更新称号
     *
     * @param id 称号 ID
     * @param request 更新请求
     * @return 更新后的称号
     */
    LeaderboardTitleDto updateLeaderboardTitle(Long id, UpdateLeaderboardTitleRequest request);

    /**
     * 删除称号
     *
     * @param id 称号 ID
     */
    void deleteLeaderboardTitle(Long id);


    // ===== 操作日志查询 =====
    
    /**
     * 获取操作日志（分页、筛选）
     *
     * @param operatorId 操作人 ID（可选）
     * @param operationType 操作类型（可选）
     * @param targetType 对象类型（可选）
     * @param startDate 开始时间（可选）
     * @param endDate 结束时间（可选）
     * @param page 页码（从 0 开始）
     * @param size 每页大小
     * @return 操作日志分页数据
     */
    Page<AdminOperationLogDto> getOperationLogs(
            String operatorId,
            String operationType,
            String targetType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size
    );
}
