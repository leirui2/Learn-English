package com.englishtyping.config;

import com.englishtyping.entity.Category;
import com.englishtyping.entity.Level;
import com.englishtyping.entity.User;
import com.englishtyping.entity.UserLevelProgress;
import com.englishtyping.repository.CategoryRepository;
import com.englishtyping.repository.LevelRepository;
import com.englishtyping.repository.UserLevelProgressRepository;
import com.englishtyping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户关卡初始化器
 * 为所有现有用户自动解锁每个分类的第一关
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLevelInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LevelRepository levelRepository;
    private final UserLevelProgressRepository userLevelProgressRepository;

    @Override
    public void run(String... args) {
        log.info("开始检查并初始化用户关卡进度...");
        
        List<User> users = userRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        
        int initializedCount = 0;
        
        for (User user : users) {
            for (Category category : categories) {
                // 查找该分类下的第一关
                List<Level> levels = levelRepository.findByCategoryIdOrderByLevelOrderAsc(category.getId());
                
                if (!levels.isEmpty()) {
                    Level firstLevel = levels.get(0); // 已经按 levelOrder 排序，第一个就是第一关
                    
                    // 检查用户是否已有该关卡的进度记录
                    UserLevelProgress existingProgress = userLevelProgressRepository
                            .findByUserIdAndLevelId(user.getId(), firstLevel.getId());
                    
                    if (existingProgress == null) {
                        // 创建进度记录，解锁第一关
                        UserLevelProgress progress = UserLevelProgress.builder()
                                .user(user)
                                .level(firstLevel)
                                .unlocked(true)
                                .completed(false)
                                .build();
                        userLevelProgressRepository.save(progress);
                        initializedCount++;
                        log.debug("为用户 {} 解锁关卡 {}", user.getUsername(), firstLevel.getName());
                    } else if (!existingProgress.getUnlocked()) {
                        // 如果记录存在但未解锁，则解锁
                        existingProgress.setUnlocked(true);
                        userLevelProgressRepository.save(existingProgress);
                        initializedCount++;
                        log.debug("为用户 {} 解锁关卡 {}", user.getUsername(), firstLevel.getName());
                    }
                }
            }
        }
        
        log.info("用户关卡进度初始化完成，共处理 {} 条记录", initializedCount);
    }
}
