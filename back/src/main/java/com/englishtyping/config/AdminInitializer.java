package com.englishtyping.config;

import com.englishtyping.entity.User;
import com.englishtyping.entity.UserRole;
import com.englishtyping.entity.UserStatus;
import com.englishtyping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 管理员初始化组件
 * 在应用启动时检查并创建超级管理员账号
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 超级管理员默认用户名 */
    private static final String ADMIN_USERNAME = "admin";
    
    /** 超级管理员默认邮箱 */
    private static final String ADMIN_EMAIL = "admin@qq.com";
    
    /** 超级管理员默认密码 */
    private static final String ADMIN_PASSWORD = "12345678";

    @Override
    public void run(ApplicationArguments args) {
        // 检查是否已存在管理员账号
        if (!userRepository.existsByRole(UserRole.ADMIN)) {
            log.info("未检测到管理员账号，开始创建超级管理员...");
            
            // 创建超级管理员
            User admin = User.builder()
                    .id(UUID.randomUUID().toString())
                    .username(ADMIN_USERNAME)
                    .email(ADMIN_EMAIL)
                    .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .totalScore(0)
                    .streak(0)
                    .build();
            
            userRepository.save(admin);
            
            log.info("========================================");
            log.info("超级管理员账号创建成功！");
            log.info("用户名: {}", ADMIN_USERNAME);
            log.info("邮箱: {}", ADMIN_EMAIL);
            log.info("密码: {}", ADMIN_PASSWORD);
            log.info("请在首次登录后及时修改密码！");
            log.info("========================================");
        } else {
            log.info("管理员账号已存在，跳过初始化");
        }
    }
}
