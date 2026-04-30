package com.englishtyping.service.impl;

import com.englishtyping.annotation.AdminOperation;
import com.englishtyping.dto.LeaderboardTitleDto;
import com.englishtyping.dto.admin.*;
import com.englishtyping.entity.*;
import com.englishtyping.repository.*;
import com.englishtyping.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserLevelProgressRepository userLevelProgressRepository;
    private final TypingSessionRepository typingSessionRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final WordErrorStatRepository wordErrorStatRepository;
    private final CategoryRepository categoryRepository;
    private final LevelRepository levelRepository;
    private final ExerciseRepository exerciseRepository;
    private final AdminOperationLogRepository adminOperationLogRepository;
    private final LeaderboardTitleRepository leaderboardTitleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    /** 发件人邮箱地址（从 spring.mail.username 读取） */
    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public Page<UserListDto> getUserList(String keyword, UserRole role, UserStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByConditions(keyword, role, status, pageable);
        
        return userPage.map(this::convertToListDto);
    }

    @Override
    public UserListDto getUserDetail(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        
        return convertToListDto(user);
    }

    @Override
    @Transactional
    @AdminOperation(type = "BAN", targetType = "USER", description = "封禁用户")
    public void banUser(String userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        
        // 不能封禁管理员
        if (user.getRole() == UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "不能封禁管理员账号");
        }
        
        // 检查是否已被封禁
        if (user.getStatus() == UserStatus.BANNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "该用户已被封禁");
        }
        
        user.setStatus(UserStatus.BANNED);
        user.setBannedAt(LocalDateTime.now());
        user.setBannedReason(reason);
        userRepository.save(user);
    }

    @Override
    @Transactional
    @AdminOperation(type = "UNBAN", targetType = "USER", description = "解封用户")
    public void unbanUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        
        // 检查是否已被封禁
        if (user.getStatus() != UserStatus.BANNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "该用户未被封禁");
        }
        
        user.setStatus(UserStatus.ACTIVE);
        user.setBannedAt(null);
        user.setBannedReason(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    @AdminOperation(type = "DELETE", targetType = "USER", description = "删除用户")
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        
        // 不能删除管理员
        if (user.getRole() == UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "不能删除管理员账号");
        }
        
        // 级联删除相关数据
        userLevelProgressRepository.deleteByUserId(userId);
        typingSessionRepository.deleteByUserId(userId);
        dailyCheckinRepository.deleteByUserId(userId);
        wordErrorStatRepository.deleteByUserId(userId);
        
        // 删除用户
        userRepository.delete(user);
    }

    @Override
    @Transactional
    @AdminOperation(type = "RESET_PASSWORD", targetType = "USER", description = "重置用户密码")
    public void resetUserPassword(String userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        
        // BCrypt 加密新密码
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // 发送邮件通知用户密码已被重置
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(user.getEmail());
        message.setSubject("【英语打字学习】密码重置通知");
        message.setText("您好，\n\n您的账号密码已被管理员重置。\n\n新密码：" + newPassword 
                + "\n\n请尽快登录并修改密码。\n\n如有疑问，请联系管理员。");
        mailSender.send(message);
    }

    @Override
    @Transactional
    @AdminOperation(type = "UPDATE_ROLE", targetType = "USER", description = "修改用户角色")
    public void updateUserRole(String userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));
        
        user.setRole(role);
        userRepository.save(user);
    }

    /**
     * 将 User 实体转换为 UserListDto
     */
    private UserListDto convertToListDto(User user) {
        return UserListDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .totalScore(user.getTotalScore())
                .streak(user.getStreak())
                .createdAt(user.getCreatedAt())
                .bannedAt(user.getBannedAt())
                .bannedReason(user.getBannedReason())
                .build();
    }

    // ===== 分类管理 =====

    @Override
    public List<com.englishtyping.dto.admin.CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public com.englishtyping.dto.admin.CategoryDto getCategoryDetail(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "分类不存在"));
        return convertToCategoryDto(category);
    }

    @Override
    @Transactional
    @AdminOperation(type = "CREATE", targetType = "CATEGORY", description = "创建分类")
    public com.englishtyping.dto.admin.CategoryDto createCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .difficulty(request.getDifficultyLevel())
                .build();
        
        category = categoryRepository.save(category);
        return convertToCategoryDto(category);
    }

    @Override
    @Transactional
    @AdminOperation(type = "UPDATE", targetType = "CATEGORY", description = "更新分类")
    public com.englishtyping.dto.admin.CategoryDto updateCategory(Integer id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "分类不存在"));
        
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getDifficultyLevel() != null) {
            category.setDifficulty(request.getDifficultyLevel());
        }
        
        category = categoryRepository.save(category);
        return convertToCategoryDto(category);
    }

    @Override
    @Transactional
    @AdminOperation(type = "DELETE", targetType = "CATEGORY", description = "删除分类")
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "分类不存在"));
        
        // 检查是否有关卡
        List<Level> levels = levelRepository.findByCategoryIdOrderByLevelOrderAsc(id);
        if (!levels.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "该分类下还有 " + levels.size() + " 个关卡，无法删除");
        }
        
        categoryRepository.delete(category);
    }

    private com.englishtyping.dto.admin.CategoryDto convertToCategoryDto(Category category) {
        long levelCount = levelRepository.findByCategoryIdOrderByLevelOrderAsc(category.getId()).size();
        
        return com.englishtyping.dto.admin.CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .difficultyLevel(category.getDifficulty())
                .levelCount(levelCount)
                .build();
    }

    // ===== 关卡管理 =====

    @Override
    public List<LevelDto> getAllLevels(Integer categoryId) {
        List<Level> levels;
        if (categoryId != null) {
            levels = levelRepository.findByCategoryIdOrderByLevelOrderAsc(categoryId);
        } else {
            levels = levelRepository.findAll();
        }
        
        return levels.stream()
                .map(this::convertToLevelDto)
                .collect(Collectors.toList());
    }

    @Override
    public LevelDto getLevelDetail(Integer id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));
        return convertToLevelDto(level);
    }

    @Override
    @Transactional
    @AdminOperation(type = "CREATE", targetType = "LEVEL", description = "创建关卡")
    public LevelDto createLevel(CreateLevelRequest request) {
        // 验证分类是否存在
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "分类不存在"));
        
        Level level = Level.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .levelOrder(request.getOrderIndex())
                .standardTimeMs(request.getStandardTime() * 1000L)  // 秒转毫秒
                .build();
        
        level = levelRepository.save(level);
        return convertToLevelDto(level);
    }

    @Override
    @Transactional
    @AdminOperation(type = "UPDATE", targetType = "LEVEL", description = "更新关卡")
    public LevelDto updateLevel(Integer id, UpdateLevelRequest request) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "分类不存在"));
            level.setCategory(category);
        }
        if (request.getName() != null) {
            level.setName(request.getName());
        }
        if (request.getDescription() != null) {
            level.setDescription(request.getDescription());
        }
        if (request.getOrderIndex() != null) {
            level.setLevelOrder(request.getOrderIndex());
        }
        if (request.getStandardTime() != null) {
            level.setStandardTimeMs(request.getStandardTime() * 1000L);  // 秒转毫秒
        }
        
        level = levelRepository.save(level);
        return convertToLevelDto(level);
    }

    @Override
    @Transactional
    @AdminOperation(type = "DELETE", targetType = "LEVEL", description = "删除关卡")
    public void deleteLevel(Integer id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));
        
        // 检查是否有用户进度
        long progressCount = userLevelProgressRepository.findAll().stream()
                .filter(p -> p.getLevel().getId().equals(id))
                .count();
        
        if (progressCount > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "该关卡已有 " + progressCount + " 个用户进度记录，无法删除");
        }
        
        // 删除关联的练习内容
        List<Exercise> exercises = exerciseRepository.findByLevelIdOrderByExerciseOrderAsc(id);
        exerciseRepository.deleteAll(exercises);
        
        levelRepository.delete(level);
    }

    private LevelDto convertToLevelDto(Level level) {
        long exerciseCount = exerciseRepository.findByLevelIdOrderByExerciseOrderAsc(level.getId()).size();
        
        return LevelDto.builder()
                .id(level.getId())
                .categoryId(level.getCategory().getId())
                .categoryName(level.getCategory().getName())
                .name(level.getName())
                .description(level.getDescription())
                .orderIndex(level.getLevelOrder())
                .standardTime((int) (level.getStandardTimeMs() / 1000))  // 毫秒转秒
                .exerciseCount(exerciseCount)
                .build();
    }

    // ===== 练习内容管理 =====

    @Override
    public List<ExerciseDto> getAllExercises(Integer levelId) {
        List<Exercise> exercises;
        if (levelId != null) {
            exercises = exerciseRepository.findByLevelIdOrderByExerciseOrderAsc(levelId);
        } else {
            exercises = exerciseRepository.findAll();
        }
        
        return exercises.stream()
                .map(this::convertToExerciseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseDto getExerciseDetail(Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "练习内容不存在"));
        return convertToExerciseDto(exercise);
    }

    @Override
    @Transactional
    @AdminOperation(type = "CREATE", targetType = "EXERCISE", description = "创建练习内容")
    public ExerciseDto createExercise(CreateExerciseRequest request) {
        // 验证关卡是否存在
        Level level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));
        
        Exercise exercise = Exercise.builder()
                .level(level)
                .contentEn(request.getContentEn())
                .contentZh(request.getContentZh())
                .exerciseOrder(request.getOrderIndex())
                .build();
        
        exercise = exerciseRepository.save(exercise);
        return convertToExerciseDto(exercise);
    }

    @Override
    @Transactional
    @AdminOperation(type = "BATCH_CREATE", targetType = "EXERCISE", description = "批量创建练习内容")
    public List<ExerciseDto> batchCreateExercises(BatchCreateExerciseRequest request) {
        // 验证关卡是否存在
        Level level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));
        
        return request.getExercises().stream()
                .map(item -> {
                    Exercise exercise = Exercise.builder()
                            .level(level)
                            .contentEn(item.getContentEn())
                            .contentZh(item.getContentZh())
                            .exerciseOrder(item.getOrderIndex())
                            .build();
                    exercise = exerciseRepository.save(exercise);
                    return convertToExerciseDto(exercise);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @AdminOperation(type = "UPDATE", targetType = "EXERCISE", description = "更新练习内容")
    public ExerciseDto updateExercise(Integer id, UpdateExerciseRequest request) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "练习内容不存在"));
        
        if (request.getLevelId() != null) {
            Level level = levelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "关卡不存在"));
            exercise.setLevel(level);
        }
        if (request.getContentEn() != null) {
            exercise.setContentEn(request.getContentEn());
        }
        if (request.getContentZh() != null) {
            exercise.setContentZh(request.getContentZh());
        }
        if (request.getOrderIndex() != null) {
            exercise.setExerciseOrder(request.getOrderIndex());
        }
        
        exercise = exerciseRepository.save(exercise);
        return convertToExerciseDto(exercise);
    }

    @Override
    @Transactional
    @AdminOperation(type = "DELETE", targetType = "EXERCISE", description = "删除练习内容")
    public void deleteExercise(Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "练习内容不存在"));
        
        exerciseRepository.delete(exercise);
    }

    private ExerciseDto convertToExerciseDto(Exercise exercise) {
        return ExerciseDto.builder()
                .id(exercise.getId())
                .levelId(exercise.getLevel().getId())
                .levelName(exercise.getLevel().getName())
                .contentEn(exercise.getContentEn())
                .contentZh(exercise.getContentZh())
                .orderIndex(exercise.getExerciseOrder())
                .build();
    }

    // ===== 操作日志查询 =====

    @Override
    public Page<AdminOperationLogDto> getOperationLogs(
            String operatorId,
            String operationType,
            String targetType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminOperationLog> logPage = adminOperationLogRepository.findByConditions(
                operatorId, operationType, targetType, startDate, endDate, pageable
        );
        
        return logPage.map(this::convertToLogDto);
    }

    private AdminOperationLogDto convertToLogDto(AdminOperationLog log) {
        return AdminOperationLogDto.builder()
                .id(log.getId())
                .operatorId(log.getOperatorId())
                .operatorName(log.getOperatorName())
                .operationType(log.getOperationType())
                .targetType(log.getTargetType())
                .targetId(log.getTargetId())
                .operationDetail(log.getOperationDetail())
                .ipAddress(log.getIpAddress())
                .createdAt(log.getCreatedAt())
                .build();
    }

    // ===== 称号管理 =====

    @Override
    public List<LeaderboardTitleDto> getAllLeaderboardTitles() {
        return leaderboardTitleRepository.findAllByOrderBySortOrderAsc().stream()
                .map(this::convertToTitleDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaderboardTitleDto getLeaderboardTitleDetail(Long id) {
        LeaderboardTitle title = leaderboardTitleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "称号不存在"));
        return convertToTitleDto(title);
    }

    @Override
    @AdminOperation(type = "CREATE_TITLE", targetType = "LeaderboardTitle")
    public LeaderboardTitleDto createLeaderboardTitle(CreateLeaderboardTitleRequest request) {
        // 验证排名区间的合理性
        if (request.getMinRank() > request.getMaxRank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "最小排名不能大于最大排名");
        }

        // 检查排名区间是否与现有称号冲突
        List<LeaderboardTitle> existingTitles = leaderboardTitleRepository.findAllByOrderBySortOrderAsc();
        for (LeaderboardTitle existing : existingTitles) {
            if (isRankRangeOverlap(request.getMinRank(), request.getMaxRank(), 
                                 existing.getMinRank(), existing.getMaxRank())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    String.format("排名区间与现有称号 '%s' 冲突", existing.getName()));
            }
        }

        LeaderboardTitle title = LeaderboardTitle.builder()
                .name(request.getName())
                .minRank(request.getMinRank())
                .maxRank(request.getMaxRank())
                .icon(request.getIcon())
                .color(request.getColor())
                .sortOrder(request.getSortOrder())
                .build();

        title = leaderboardTitleRepository.save(title);
        return convertToTitleDto(title);
    }

    @Override
    @AdminOperation(type = "UPDATE_TITLE", targetType = "LeaderboardTitle")
    public LeaderboardTitleDto updateLeaderboardTitle(Long id, UpdateLeaderboardTitleRequest request) {
        LeaderboardTitle title = leaderboardTitleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "称号不存在"));

        // 如果更新了排名区间，需要验证合理性
        Integer newMinRank = request.getMinRank() != null ? request.getMinRank() : title.getMinRank();
        Integer newMaxRank = request.getMaxRank() != null ? request.getMaxRank() : title.getMaxRank();
        
        if (newMinRank > newMaxRank) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "最小排名不能大于最大排名");
        }

        // 检查更新后的排名区间是否与其他称号冲突
        List<LeaderboardTitle> existingTitles = leaderboardTitleRepository.findAllByOrderBySortOrderAsc();
        for (LeaderboardTitle existing : existingTitles) {
            if (!existing.getId().equals(id) && 
                isRankRangeOverlap(newMinRank, newMaxRank, existing.getMinRank(), existing.getMaxRank())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    String.format("排名区间与现有称号 '%s' 冲突", existing.getName()));
            }
        }

        if (request.getName() != null) {
            title.setName(request.getName());
        }
        if (request.getMinRank() != null) {
            title.setMinRank(request.getMinRank());
        }
        if (request.getMaxRank() != null) {
            title.setMaxRank(request.getMaxRank());
        }
        if (request.getIcon() != null) {
            title.setIcon(request.getIcon());
        }
        if (request.getColor() != null) {
            title.setColor(request.getColor());
        }
        if (request.getSortOrder() != null) {
            title.setSortOrder(request.getSortOrder());
        }

        title = leaderboardTitleRepository.save(title);
        return convertToTitleDto(title);
    }

    @Override
    @AdminOperation(type = "DELETE_TITLE", targetType = "LeaderboardTitle")
    public void deleteLeaderboardTitle(Long id) {
        if (!leaderboardTitleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "称号不存在");
        }
        leaderboardTitleRepository.deleteById(id);
    }

    /**
     * 检查两个排名区间是否重叠
     */
    private boolean isRankRangeOverlap(Integer min1, Integer max1, Integer min2, Integer max2) {
        return !(max1 < min2 || max2 < min1);
    }

    /**
     * 将 LeaderboardTitle 实体转换为 DTO
     */
    private LeaderboardTitleDto convertToTitleDto(LeaderboardTitle title) {
        return LeaderboardTitleDto.builder()
                .id(title.getId())
                .name(title.getName())
                .minRank(title.getMinRank())
                .maxRank(title.getMaxRank())
                .icon(title.getIcon())
                .color(title.getColor())
                .sortOrder(title.getSortOrder())
                .build();
    }


}