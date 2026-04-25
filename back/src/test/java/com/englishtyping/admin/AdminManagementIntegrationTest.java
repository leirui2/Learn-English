package com.englishtyping.admin;

import com.englishtyping.dto.admin.*;
import com.englishtyping.entity.Category;
import com.englishtyping.entity.Level;
import com.englishtyping.entity.Exercise;
import com.englishtyping.repository.CategoryRepository;
import com.englishtyping.repository.LevelRepository;
import com.englishtyping.repository.ExerciseRepository;
import com.englishtyping.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理员管理功能集成测试
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AdminManagementIntegrationTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private Category testCategory;
    private Level testLevel;

    @BeforeEach
    void setUp() {
        // 创建测试分类
        testCategory = Category.builder()
                .name("测试分类")
                .description("测试描述")
                .difficulty(1)
                .build();
        testCategory = categoryRepository.save(testCategory);

        // 创建测试关卡
        testLevel = Level.builder()
                .category(testCategory)
                .name("测试关卡")
                .description("测试关卡描述")
                .levelOrder(1)
                .standardTimeMs(60000L)
                .build();
        testLevel = levelRepository.save(testLevel);
    }

    // ===== 分类管理测试 =====

    @Test
    void testGetAllCategories() {
        List<CategoryDto> categories = adminService.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("测试分类")));
    }

    @Test
    void testGetCategoryDetail() {
        CategoryDto category = adminService.getCategoryDetail(testCategory.getId());
        assertNotNull(category);
        assertEquals("测试分类", category.getName());
        assertEquals("测试描述", category.getDescription());
        assertEquals(1, category.getDifficultyLevel());
    }

    @Test
    void testCreateCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("新分类");
        request.setDescription("新分类描述");
        request.setDifficultyLevel(2);

        CategoryDto created = adminService.createCategory(request);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("新分类", created.getName());
        assertEquals(2, created.getDifficultyLevel());
    }

    @Test
    void testUpdateCategory() {
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("更新后的分类");
        request.setDifficultyLevel(3);

        CategoryDto updated = adminService.updateCategory(testCategory.getId(), request);
        assertNotNull(updated);
        assertEquals("更新后的分类", updated.getName());
        assertEquals(3, updated.getDifficultyLevel());
    }

    @Test
    void testDeleteCategoryWithLevels() {
        // 尝试删除有关卡的分类应该失败
        assertThrows(ResponseStatusException.class, () -> {
            adminService.deleteCategory(testCategory.getId());
        });
    }

    @Test
    void testDeleteCategoryWithoutLevels() {
        // 创建一个没有关卡的分类
        Category emptyCategory = Category.builder()
                .name("空分类")
                .description("没有关卡")
                .difficulty(1)
                .build();
        emptyCategory = categoryRepository.save(emptyCategory);

        // 删除应该成功
        Integer categoryId = emptyCategory.getId();
        assertDoesNotThrow(() -> adminService.deleteCategory(categoryId));
        assertFalse(categoryRepository.findById(categoryId).isPresent());
    }

    // ===== 关卡管理测试 =====

    @Test
    void testGetAllLevels() {
        List<LevelDto> levels = adminService.getAllLevels(null);
        assertNotNull(levels);
        assertTrue(levels.size() > 0);
    }

    @Test
    void testGetLevelsByCategory() {
        List<LevelDto> levels = adminService.getAllLevels(testCategory.getId());
        assertNotNull(levels);
        assertEquals(1, levels.size());
        assertEquals("测试关卡", levels.get(0).getName());
    }

    @Test
    void testGetLevelDetail() {
        LevelDto level = adminService.getLevelDetail(testLevel.getId());
        assertNotNull(level);
        assertEquals("测试关卡", level.getName());
        assertEquals(testCategory.getId(), level.getCategoryId());
    }

    @Test
    void testCreateLevel() {
        CreateLevelRequest request = new CreateLevelRequest();
        request.setCategoryId(testCategory.getId());
        request.setName("新关卡");
        request.setDescription("新关卡描述");
        request.setOrderIndex(2);
        request.setStandardTime(90);  // 秒

        LevelDto created = adminService.createLevel(request);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("新关卡", created.getName());
        assertEquals(2, created.getOrderIndex());
    }

    @Test
    void testUpdateLevel() {
        UpdateLevelRequest request = new UpdateLevelRequest();
        request.setName("更新后的关卡");
        request.setOrderIndex(5);
        request.setCategoryId(testCategory.getId());
        request.setStandardTime(60);

        LevelDto updated = adminService.updateLevel(testLevel.getId(), request);
        assertNotNull(updated);
        assertEquals("更新后的关卡", updated.getName());
        assertEquals(5, updated.getOrderIndex());
    }

    @Test
    void testDeleteLevel() {
        // 删除没有用户进度的关卡应该成功
        Integer levelId = testLevel.getId();
        assertDoesNotThrow(() -> adminService.deleteLevel(levelId));
        assertFalse(levelRepository.findById(levelId).isPresent());
    }

    // ===== 练习内容管理测试 =====

    @Test
    void testGetAllExercises() {
        // 先创建一个练习
        Exercise exercise = Exercise.builder()
                .level(testLevel)
                .contentEn("Hello")
                .contentZh("你好")
                .exerciseOrder(1)
                .build();
        exerciseRepository.save(exercise);

        List<ExerciseDto> exercises = adminService.getAllExercises(null);
        assertNotNull(exercises);
        assertTrue(exercises.size() > 0);
    }

    @Test
    void testGetExercisesByLevel() {
        // 先创建一个练习
        Exercise exercise = Exercise.builder()
                .level(testLevel)
                .contentEn("Hello")
                .contentZh("你好")
                .exerciseOrder(1)
                .build();
        exerciseRepository.save(exercise);

        List<ExerciseDto> exercises = adminService.getAllExercises(testLevel.getId());
        assertNotNull(exercises);
        assertEquals(1, exercises.size());
        assertEquals("Hello", exercises.get(0).getContentEn());
    }

    @Test
    void testCreateExercise() {
        CreateExerciseRequest request = new CreateExerciseRequest();
        request.setLevelId(testLevel.getId());
        request.setContentEn("World");
        request.setContentZh("世界");
        request.setOrderIndex(1);

        ExerciseDto created = adminService.createExercise(request);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("World", created.getContentEn());
        assertEquals("世界", created.getContentZh());
    }

    @Test
    void testBatchCreateExercises() {
        BatchCreateExerciseRequest.ExerciseItem item1 = new BatchCreateExerciseRequest.ExerciseItem();
        item1.setContentEn("Apple");
        item1.setContentZh("苹果");
        item1.setOrderIndex(1);

        BatchCreateExerciseRequest.ExerciseItem item2 = new BatchCreateExerciseRequest.ExerciseItem();
        item2.setContentEn("Banana");
        item2.setContentZh("香蕉");
        item2.setOrderIndex(2);

        BatchCreateExerciseRequest request = new BatchCreateExerciseRequest();
        request.setLevelId(testLevel.getId());
        request.setExercises(List.of(item1, item2));

        List<ExerciseDto> created = adminService.batchCreateExercises(request);
        assertNotNull(created);
        assertEquals(2, created.size());
    }

    @Test
    void testUpdateExercise() {
        Exercise exercise = Exercise.builder()
                .level(testLevel)
                .contentEn("Old")
                .contentZh("旧的")
                .exerciseOrder(1)
                .build();
        exercise = exerciseRepository.save(exercise);

        UpdateExerciseRequest request = new UpdateExerciseRequest();
        request.setLevelId(testLevel.getId());
        request.setContentEn("New");
        request.setContentZh("新的");
        request.setOrderIndex(1);

        ExerciseDto updated = adminService.updateExercise(exercise.getId(), request);
        assertNotNull(updated);
        assertEquals("New", updated.getContentEn());
        assertEquals("新的", updated.getContentZh());
    }

    @Test
    void testDeleteExercise() {
        Exercise exercise = Exercise.builder()
                .level(testLevel)
                .contentEn("Delete me")
                .contentZh("删除我")
                .exerciseOrder(1)
                .build();
        exercise = exerciseRepository.save(exercise);

        Integer exerciseId = exercise.getId();
        assertDoesNotThrow(() -> adminService.deleteExercise(exerciseId));
        assertFalse(exerciseRepository.findById(exerciseId).isPresent());
    }
}
