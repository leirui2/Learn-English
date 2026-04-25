-- ========================================
-- 英语学习打字网站 - 数据库初始化脚本
-- 执行顺序：先创建表结构，再插入初始数据
-- ========================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 第一部分：创建表结构
-- ========================================

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` CHAR(36) NOT NULL COMMENT '用户 ID (UUID)',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱（唯一）',
  `password_hash` VARCHAR(255) NOT NULL COMMENT 'BCrypt 加密后的密码',
  `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '用户角色（USER/ADMIN）',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '账号状态（ACTIVE/BANNED）',
  `banned_at` DATETIME NULL DEFAULT NULL COMMENT '封禁时间',
  `banned_reason` VARCHAR(500) NULL DEFAULT NULL COMMENT '封禁原因',
  `total_score` INT NOT NULL DEFAULT 0 COMMENT '累计总积分',
  `streak` INT NOT NULL DEFAULT 0 COMMENT '连续打卡天数',
  `last_checkin_date` DATETIME NULL DEFAULT NULL COMMENT '最后打卡日期',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_total_score` (`total_score`),
  KEY `idx_streak` (`streak`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '分类 ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称（如：小学、初中、高中、四级、六级）',
  `description` VARCHAR(200) NULL COMMENT '分类描述',
  `difficulty` INT NOT NULL COMMENT '难度等级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='难度分类表';

-- ----------------------------
-- Table structure for levels
-- ----------------------------
DROP TABLE IF EXISTS `levels`;
CREATE TABLE `levels` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '关卡 ID',
  `category_id` INT NOT NULL COMMENT '所属分类 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '关卡名称',
  `description` VARCHAR(200) NULL COMMENT '关卡描述',
  `level_order` INT NOT NULL COMMENT '关卡序号',
  `standard_time_ms` BIGINT NOT NULL COMMENT '标准完成时间（毫秒）',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_level_order` (`category_id`, `level_order`),
  CONSTRAINT `fk_levels_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关卡表';

-- ----------------------------
-- Table structure for exercises
-- ----------------------------
DROP TABLE IF EXISTS `exercises`;
CREATE TABLE `exercises` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '练习内容 ID',
  `level_id` INT NOT NULL COMMENT '所属关卡 ID',
  `content_en` VARCHAR(500) NOT NULL COMMENT '英文内容',
  `content_zh` VARCHAR(500) NOT NULL COMMENT '中文释义',
  `exercise_order` INT NOT NULL COMMENT '练习序号',
  PRIMARY KEY (`id`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_exercise_order` (`level_id`, `exercise_order`),
  CONSTRAINT `fk_exercises_level` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='练习内容表';

-- ----------------------------
-- Table structure for user_level_progress
-- ----------------------------
DROP TABLE IF EXISTS `user_level_progress`;
CREATE TABLE `user_level_progress` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '进度 ID',
  `user_id` CHAR(36) NOT NULL COMMENT '用户 ID',
  `level_id` INT NOT NULL COMMENT '关卡 ID',
  `unlocked` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已解锁',
  `completed` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已完成',
  `best_accuracy` DOUBLE NULL COMMENT '历史最高正确率',
  `best_time_ms` BIGINT NULL COMMENT '历史最佳用时（毫秒）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_level` (`user_id`, `level_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_level_id` (`level_id`),
  CONSTRAINT `fk_ulp_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ulp_level` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关卡进度表';

-- ----------------------------
-- Table structure for typing_sessions
-- ----------------------------
DROP TABLE IF EXISTS `typing_sessions`;
CREATE TABLE `typing_sessions` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '会话 ID',
  `user_id` CHAR(36) NOT NULL COMMENT '用户 ID',
  `level_id` INT NOT NULL COMMENT '关卡 ID',
  `accuracy` DOUBLE NOT NULL COMMENT '正确率（0-100）',
  `time_ms` BIGINT NOT NULL COMMENT '用时（毫秒）',
  `score` INT NOT NULL COMMENT '获得积分',
  `characters_typed` INT NULL COMMENT '输入字符数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '完成时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_ts_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ts_level` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打字会话记录表';

-- ----------------------------
-- Table structure for daily_checkins
-- ----------------------------
DROP TABLE IF EXISTS `daily_checkins`;
CREATE TABLE `daily_checkins` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '打卡 ID',
  `user_id` CHAR(36) NOT NULL COMMENT '用户 ID',
  `checkin_date` DATE NOT NULL COMMENT '打卡日期',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_checkin_date` (`checkin_date`),
  CONSTRAINT `fk_dc_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日打卡记录表';

-- ----------------------------
-- Table structure for password_reset_tokens
-- ----------------------------
DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE `password_reset_tokens` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '令牌 ID',
  `user_id` CHAR(36) NOT NULL COMMENT '用户 ID',
  `token_hash` VARCHAR(64) NOT NULL COMMENT 'SHA-256 哈希后的令牌值',
  `expires_at` DATETIME NOT NULL COMMENT '过期时间',
  `used` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已使用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_token_hash` (`token_hash`),
  KEY `idx_expires_at` (`expires_at`),
  CONSTRAINT `fk_prt_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='密码重置令牌表';

-- ----------------------------
-- Table structure for word_error_stats
-- ----------------------------
DROP TABLE IF EXISTS `word_error_stats`;
CREATE TABLE `word_error_stats` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '统计 ID',
  `user_id` CHAR(36) NOT NULL COMMENT '用户 ID',
  `word` VARCHAR(100) NOT NULL COMMENT '单词',
  `error_count` INT NOT NULL DEFAULT 0 COMMENT '错误次数',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_word` (`user_id`, `word`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_error_count` (`user_id`, `error_count`),
  CONSTRAINT `fk_wes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词错误统计表';

-- ----------------------------
-- Table structure for admin_operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `admin_operation_logs`;
CREATE TABLE `admin_operation_logs` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '日志 ID',
  `operator_id` CHAR(36) NOT NULL COMMENT '操作人 ID',
  `operator_name` VARCHAR(50) NOT NULL COMMENT '操作人用户名',
  `operation_type` VARCHAR(20) NOT NULL COMMENT '操作类型（CREATE/UPDATE/DELETE/BAN/UNBAN）',
  `target_type` VARCHAR(20) NOT NULL COMMENT '操作对象类型（USER/CATEGORY/LEVEL/EXERCISE）',
  `target_id` VARCHAR(50) NOT NULL COMMENT '操作对象 ID',
  `operation_detail` TEXT NULL COMMENT '操作详情（JSON）',
  `ip_address` VARCHAR(50) NULL COMMENT '操作 IP',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_target_type` (`target_type`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_aol_operator` FOREIGN KEY (`operator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';

-- ========================================
-- 第二部分：插入初始数据
-- ========================================

-- 插入五个难度分类
INSERT INTO `categories` (`id`, `name`, `description`, `difficulty`) VALUES
(1, '小学', '适合小学生的基础词汇', 1),
(2, '初中', '适合初中生的常用词汇', 2),
(3, '高中', '适合高中生的进阶词汇', 3),
(4, '四级', '大学英语四级词汇', 4),
(5, '六级', '大学英语六级词汇', 5);

-- 插入示例关卡
-- 小学分类的关卡
INSERT INTO `levels` (`id`, `category_id`, `name`, `description`, `level_order`, `standard_time_ms`) VALUES
(1, 1, '第1关：基础单词', '学习最基础的英语单词', 1, 60000),
(2, 1, '第2关：常用动词', '学习常用的动作词汇', 2, 60000),
(3, 1, '第3关：颜色与数字', '学习颜色和数字的表达', 3, 60000);

-- 初中分类的关卡
INSERT INTO `levels` (`id`, `category_id`, `name`, `description`, `level_order`, `standard_time_ms`) VALUES
(4, 2, '第1关：日常交流', '学习日常交流用语', 1, 90000),
(5, 2, '第2关：学校生活', '学习校园相关词汇', 2, 90000);

-- 高中分类的关卡
INSERT INTO `levels` (`id`, `category_id`, `name`, `description`, `level_order`, `standard_time_ms`) VALUES
(6, 3, '第1关：学术词汇', '学习学术场景词汇', 1, 120000);

-- 四级分类的关卡
INSERT INTO `levels` (`id`, `category_id`, `name`, `description`, `level_order`, `standard_time_ms`) VALUES
(7, 4, '第1关：四级核心词汇', '学习四级考试核心词汇', 1, 150000);

-- 六级分类的关卡
INSERT INTO `levels` (`id`, `category_id`, `name`, `description`, `level_order`, `standard_time_ms`) VALUES
(8, 5, '第1关：六级核心词汇', '学习六级考试核心词汇', 1, 180000);

-- 插入练习内容
-- 小学第1关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(1, 'hello', '你好', 1),
(1, 'world', '世界', 2),
(1, 'apple', '苹果', 3),
(1, 'book', '书', 4),
(1, 'cat', '猫', 5),
(1, 'dog', '狗', 6),
(1, 'sun', '太阳', 7),
(1, 'moon', '月亮', 8),
(1, 'star', '星星', 9),
(1, 'water', '水', 10);

-- 小学第2关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(2, 'run', '跑', 1),
(2, 'jump', '跳', 2),
(2, 'walk', '走', 3),
(2, 'eat', '吃', 4),
(2, 'drink', '喝', 5),
(2, 'sleep', '睡觉', 6),
(2, 'play', '玩', 7),
(2, 'read', '读', 8),
(2, 'write', '写', 9),
(2, 'sing', '唱', 10);

-- 小学第3关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(3, 'red', '红色', 1),
(3, 'blue', '蓝色', 2),
(3, 'green', '绿色', 3),
(3, 'yellow', '黄色', 4),
(3, 'one', '一', 5),
(3, 'two', '二', 6),
(3, 'three', '三', 7),
(3, 'four', '四', 8),
(3, 'five', '五', 9),
(3, 'ten', '十', 10);

-- 初中第1关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(4, 'Good morning', '早上好', 1),
(4, 'How are you', '你好吗', 2),
(4, 'Thank you', '谢谢你', 3),
(4, 'You are welcome', '不客气', 4),
(4, 'Excuse me', '打扰一下', 5),
(4, 'I am sorry', '对不起', 6),
(4, 'Nice to meet you', '很高兴见到你', 7),
(4, 'See you later', '再见', 8),
(4, 'Have a nice day', '祝你有美好的一天', 9),
(4, 'Take care', '保重', 10);

-- 初中第2关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(5, 'classroom', '教室', 1),
(5, 'teacher', '老师', 2),
(5, 'student', '学生', 3),
(5, 'homework', '家庭作业', 4),
(5, 'exam', '考试', 5),
(5, 'library', '图书馆', 6),
(5, 'cafeteria', '食堂', 7),
(5, 'playground', '操场', 8),
(5, 'subject', '科目', 9),
(5, 'schedule', '课程表', 10);

-- 高中第1关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(6, 'analyze', '分析', 1),
(6, 'evaluate', '评估', 2),
(6, 'hypothesis', '假设', 3),
(6, 'conclusion', '结论', 4),
(6, 'evidence', '证据', 5),
(6, 'research', '研究', 6),
(6, 'theory', '理论', 7),
(6, 'experiment', '实验', 8),
(6, 'observation', '观察', 9),
(6, 'methodology', '方法论', 10);

-- 四级第1关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(7, 'accommodate', '容纳；适应', 1),
(7, 'acknowledge', '承认；致谢', 2),
(7, 'acquire', '获得；学到', 3),
(7, 'adequate', '足够的；适当的', 4),
(7, 'advocate', '提倡；拥护', 5),
(7, 'alternative', '可供选择的', 6),
(7, 'anticipate', '预期；期望', 7),
(7, 'appropriate', '适当的；恰当的', 8),
(7, 'arbitrary', '任意的；武断的', 9),
(7, 'assess', '评估；评定', 10);

-- 六级第1关的练习内容
INSERT INTO `exercises` (`level_id`, `content_en`, `content_zh`, `exercise_order`) VALUES
(8, 'ambiguous', '模棱两可的', 1),
(8, 'arbitrary', '任意的；专断的', 2),
(8, 'coherent', '连贯的；一致的', 3),
(8, 'compatible', '兼容的；和谐的', 4),
(8, 'comprehensive', '全面的；综合的', 5),
(8, 'conceive', '构思；设想', 6),
(8, 'consecutive', '连续的；连贯的', 7),
(8, 'controversial', '有争议的', 8),
(8, 'deliberate', '故意的；深思熟虑的', 9),
(8, 'elaborate', '详尽的；精心制作的', 10);

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 初始化完成！
-- ========================================
