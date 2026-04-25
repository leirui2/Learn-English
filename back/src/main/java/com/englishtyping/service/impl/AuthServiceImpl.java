package com.englishtyping.service.impl;

import com.englishtyping.dto.auth.AuthResponse;
import com.englishtyping.dto.auth.ForgotPasswordRequest;
import com.englishtyping.dto.auth.LoginRequest;
import com.englishtyping.dto.auth.RegisterRequest;
import com.englishtyping.dto.auth.ResetPasswordRequest;
import com.englishtyping.dto.auth.UserDto;
import com.englishtyping.entity.Category;
import com.englishtyping.entity.Level;
import com.englishtyping.entity.PasswordResetToken;
import com.englishtyping.entity.User;
import com.englishtyping.entity.UserLevelProgress;
import com.englishtyping.entity.UserStatus;
import com.englishtyping.repository.CategoryRepository;
import com.englishtyping.repository.LevelRepository;
import com.englishtyping.repository.PasswordResetTokenRepository;
import com.englishtyping.repository.UserLevelProgressRepository;
import com.englishtyping.repository.UserRepository;
import com.englishtyping.service.AuthService;
import com.englishtyping.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;
    private final CategoryRepository categoryRepository;
    private final LevelRepository levelRepository;
    private final UserLevelProgressRepository userLevelProgressRepository;

    /** 发件人邮箱地址（从 spring.mail.username 读取） */
    @Value("${spring.mail.username}")
    private String mailFrom;

    /** 前端地址（用于拼接重置链接） */
    @Value("${app.frontend-url}")
    private String frontendUrl;

    /** 令牌有效期（分钟） */
    @Value("${app.password-reset.token-expiration-minutes:30}")
    private int tokenExpirationMinutes;

    /** 限流窗口内最大发送次数 */
    @Value("${app.password-reset.rate-limit-count:3}")
    private int rateLimitCount;

    /** 限流窗口时长（分钟） */
    @Value("${app.password-reset.rate-limit-window-minutes:10}")
    private int rateLimitWindowMinutes;

    /** Redis 黑名单 key 前缀 */
    private static final String BLACKLIST_PREFIX = "blacklist:";

    /** Redis 限流 key 前缀 */
    private static final String RESET_EMAIL_COUNT_PREFIX = "reset_email_count:";

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 检查邮箱是否已注册
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "该邮箱已被注册");
        }

        // BCrypt 加密密码
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // 生成 UUID 作为用户 ID
        String userId = UUID.randomUUID().toString();

        // 构建并保存用户
        User user = User.builder()
                .id(userId)
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordHash)
                .totalScore(0)
                .streak(0)
                .build();
        userRepository.save(user);

        // 自动解锁每个分类的第一关
        unlockFirstLevelsForNewUser(user);

        // 生成 JWT
        return buildAuthResponse(user);
    }

    /**
     * 为新用户自动解锁每个分类的第一关
     */
    private void unlockFirstLevelsForNewUser(User user) {
        List<Category> categories = categoryRepository.findAll();
        
        for (Category category : categories) {
            // 查找该分类下的第一关（levelOrder = 1）
            List<Level> levels = levelRepository.findByCategoryIdOrderByLevelOrderAsc(category.getId());
            
            if (!levels.isEmpty()) {
                Level firstLevel = levels.get(0); // 已经按 levelOrder 排序，第一个就是第一关
                
                // 创建进度记录，解锁第一关
                UserLevelProgress progress = UserLevelProgress.builder()
                        .user(user)
                        .level(firstLevel)
                        .unlocked(true)
                        .completed(false)
                        .build();
                userLevelProgressRepository.save(progress);
            }
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 判断 identifier 是邮箱还是用户名（简单判断：包含 @ 符号视为邮箱）
        String identifier = request.getIdentifier();
        User user;
        
        if (identifier.contains("@")) {
            // 邮箱登录
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "该邮箱尚未注册，请先注册"));
        } else {
            // 用户名登录
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "该用户名不存在，请先注册"));
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "密码错误，请重试");
        }

        // 生成 JWT
        return buildAuthResponse(user);
    }

    @Override
    public String refresh(String refreshToken) {
        // 检查 token 是否在黑名单中
        if (isBlacklisted(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录已过期，请重新登录");
        }

        // 验证 refresh token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录已过期，请重新登录");
        }

        // 提取用户 ID 和角色，生成新 access token
        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        String role = jwtUtil.getRoleFromToken(refreshToken);
        return jwtUtil.generateAccessToken(userId, role);
    }

    @Override
    public void logout(String refreshToken) {
        // 验证 token 有效性（只有有效 token 才需要加入黑名单）
        if (!jwtUtil.validateToken(refreshToken)) {
            return;
        }

        // 计算剩余有效期，作为 Redis TTL
        long remainingMs = jwtUtil.getRemainingExpiration(refreshToken);
        if (remainingMs > 0) {
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + refreshToken,
                    "1",
                    remainingMs,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // 1. 查找邮箱对应用户，不存在抛出 404
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "该邮箱未注册"));

        // 2. 检查 Redis 限流：10 分钟内发送次数是否已达上限
        String rateLimitKey = RESET_EMAIL_COUNT_PREFIX + user.getId();
        String countStr = redisTemplate.opsForValue().get(rateLimitKey);
        if (countStr != null && Integer.parseInt(countStr) >= rateLimitCount) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "发送过于频繁，请稍后再试");
        }

        // 3. 生成 UUID 原始令牌，SHA-256 哈希后存入数据库
        String rawToken = UUID.randomUUID().toString().replace("-", "");
        String tokenHash = sha256Hex(rawToken);
        LocalDateTime now = LocalDateTime.now();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .tokenHash(tokenHash)
                .expiresAt(now.plusMinutes(tokenExpirationMinutes))
                .used(false)
                .createdAt(now)
                .build();
        passwordResetTokenRepository.save(resetToken);

        // 4. Redis incr 限流计数，首次设置时添加 TTL
        Long newCount = redisTemplate.opsForValue().increment(rateLimitKey);
        if (newCount != null && newCount == 1) {
            // 第一次设置，添加 TTL（限流窗口时长）
            redisTemplate.expire(rateLimitKey, rateLimitWindowMinutes, TimeUnit.MINUTES);
        }

        // 5. 发送重置邮件
        String resetLink = frontendUrl + "/reset-password?token=" + rawToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(user.getEmail());
        message.setSubject("【英语打字学习】密码重置");
        message.setText("您好，\n\n请点击以下链接重置您的密码（链接 " + tokenExpirationMinutes + " 分钟内有效）：\n\n"
                + resetLink + "\n\n如果您没有申请重置密码，请忽略此邮件。");
        mailSender.send(message);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // 1. 校验两次密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "两次输入的密码不一致");
        }

        // 2. SHA-256 哈希 token，查找令牌记录
        String tokenHash = sha256Hex(request.getToken());
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "链接已失效，请重新申请"));

        // 3. 校验令牌状态：已过期、已使用均视为无效
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "链接已失效，请重新申请");
        }
        if (resetToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "链接已失效，请重新申请");
        }

        // 4. BCrypt 加密新密码，更新用户记录
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "链接已失效，请重新申请"));
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 5. 将令牌标记为已使用，防止重放攻击
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public UserDto getCurrentUser(String userId) {
        // 根据用户 ID 查询用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户不存在或已被删除"));

        // 检查账号状态
        if (user.getStatus() == UserStatus.BANNED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "账号已被封禁");
        }

        // 构建并返回用户 DTO
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .totalScore(user.getTotalScore())
                .streak(user.getStreak())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }

    // ===== 私有方法 =====

    /**
     * 构建认证响应（生成 token + 用户 DTO）
     */
    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole().name());

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .totalScore(user.getTotalScore())
                .streak(user.getStreak())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();
    }

    /**
     * 检查 token 是否在 Redis 黑名单中
     */
    private boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    /**
     * 对字符串进行 SHA-256 哈希，返回十六进制字符串
     *
     * @param input 原始字符串
     * @return SHA-256 哈希的十六进制表示（64位小写）
     */
    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 是 Java 标准算法，不会出现此异常
            throw new IllegalStateException("SHA-256 算法不可用", e);
        }
    }
}
