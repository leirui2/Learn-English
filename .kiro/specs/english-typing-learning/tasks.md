# 实现计划：英语学习打字网站

## 概述

按模块逐步实现，从项目骨架搭建开始，依次完成后端基础设施、各业务模块 API、前端页面与打字引擎，最后完成数据初始化与 E2E 测试。每个任务均可独立提交，确保增量可验证。

---

## 任务列表

- [x] 1. 项目骨架与基础设施搭建
  - [x] 1.1 初始化后端 Spring Boot 项目结构
    - 创建 Maven 多模块或单模块项目，配置 `pom.xml`（Spring Boot 3.x、Spring Data JPA、Spring Security、jjwt、Spring Boot Mail、Spring Data Redis、jqwik、GreenMail、H2）
    - 创建 `application.yml`，配置 MySQL、Redis、SMTP 占位符（不硬编码真实值）
    - 创建 `docker-compose.yml`，包含 MySQL 8.0、Redis 7 服务
    - _需求：1.6、9.6_

  - [x] 1.2 初始化前端 Vue 3 + TypeScript 项目结构
    - 使用 Vite 创建项目，安装 Pinia、Vue Router v4、Tailwind CSS、fast-check、Playwright
    - 配置 Tailwind CSS 断点（默认/md/lg）
    - 配置 Vue Router，定义 `AuthLayout` 和 `MainLayout` 路由守卫（未登录跳转登录页）
    - _需求：1.4、10_

  - [x] 1.3 创建数据库 Schema 与 Flyway 迁移脚本
    - 编写 `V1__init_schema.sql`，创建 `users`、`categories`、`levels`、`exercises`、`user_level_progress`、`typing_sessions`、`daily_checkins`、`password_reset_tokens`、`word_error_stats` 表
    - 编写 `V2__seed_categories.sql`，插入五个难度分类（小学/初中/高中/四级/六级）
    - _需求：4.1_

- [-] 2. 用户认证模块（后端）
  - [x] 2.1 实现用户注册接口 `POST /auth/register`
    - 创建 `User` JPA 实体与 `UserRepository`
    - 实现邮箱格式校验（正则）、密码长度校验（≥8位）、邮箱唯一性检查
    - 使用 BCrypt 加密密码，注册成功后自动签发 JWT（access token 2h + refresh token 7d）
    - _需求：1.1、1.2、1.3、1.5_

  - [ ]* 2.2 编写属性测试：邮箱格式验证的普遍性（Property 1）
    - **属性 1：邮箱格式验证的普遍性**
    - **验证：需求 1.2**
    - 使用 jqwik 生成任意字符串，断言只有合法 RFC 5322 格式邮箱通过校验

  - [ ]* 2.3 编写属性测试：密码长度验证的普遍性（Property 2）
    - **属性 2：密码长度验证的普遍性**
    - **验证：需求 1.5**
    - 使用 jqwik 生成长度 0-7 的字符串断言被拒绝，长度 ≥8 的字符串断言被接受

  - [x] 2.4 实现用户登录接口 `POST /auth/login`
    - 验证用户名/邮箱+密码，签发 JWT，返回 access token 与 refresh token
    - 支持用户名或邮箱两种登录方式
    - 登录失败返回 401，不暴露具体原因
    - _需求：1.4_

  - [x] 2.5 实现 Token 刷新与退出接口
    - `POST /auth/refresh`：用 refresh token 换取新 access token
    - `POST /auth/logout`：使 refresh token 失效（黑名单存 Redis）
    - _需求：1.6_

  - [x] 2.6 实现获取当前用户信息接口 `GET /users/me`
    - 从 JWT 中提取用户 ID，查询并返回用户信息
    - 返回字段：用户名、邮箱、总积分、连续打卡天数、角色、账号状态
    - 未登录或 token 无效返回 401
    - _需求：1.7、1.8_

  - [ ] 2.7 编写集成测试：注册→登录→获取 JWT 完整流程
    - 使用 `@SpringBootTest` + MockMvc 测试完整认证流程
    - _需求：1.1、1.2、1.3、1.4_

- [x] 3. 邮箱密码重置模块（后端）
  - [x] 3.1 实现申请重置接口 `POST /auth/forgot-password`
    - 创建 `PasswordResetToken` JPA 实体与 Repository
    - 查询邮箱是否注册，未注册返回 404
    - 检查 Redis `reset_email_count:{user_id}` 限流（10分钟内 ≤3次），超限返回 429
    - 生成 UUID 令牌，存储 SHA-256 哈希值至数据库，有效期 30 分钟
    - 通过 `JavaMailSender` 发送重置邮件（SMTP 配置从 `application.yml` 读取）
    - _需求：9.1、9.2、9.3、9.4、9.5、9.6、9.12_

  - [ ]* 3.2 编写属性测试：重置令牌唯一性（Property 16）
    - **属性 16：重置令牌的唯一性**
    - **验证：需求 9.4**
    - 使用 jqwik 生成多次令牌，断言每次生成的令牌互不相同，过期时间为生成时刻 +30 分钟

  - [ ]* 3.3 编写属性测试：密码重置限流规则（Property 19）
    - **属性 19：密码重置限流规则**
    - **验证：需求 9.12**
    - 使用 jqwik 模拟同一邮箱在 10 分钟内发送 N 次请求，断言前 3 次成功、第 4 次被拒绝

  - [x] 3.4 实现令牌验证与密码重置接口 `POST /auth/reset-password`
    - 验证令牌存在、未过期、未使用（对应属性 17）
    - 校验新密码一致性与长度（≥8位）
    - 更新密码（BCrypt），立即将令牌 `used` 标记为 true（对应属性 18）
    - _需求：9.7、9.8、9.9、9.10、9.11_

  - [ ]* 3.5 编写属性测试：令牌验证逻辑正确性（Property 17）
    - **属性 17：令牌验证逻辑的正确性**
    - **验证：需求 9.7、9.8**

  - [ ] 3.6 编写属性测试：令牌使用后立即失效（Property 18）
    - **属性 18：令牌使用后立即失效**
    - **验证：需求 9.11**

  - [ ]* 3.7 编写属性测试：新密码验证规则（Property 20）
    - **属性 20：新密码验证规则**
    - **验证：需求 9.10**
    - 使用 jqwik 生成任意两个密码字符串组合，断言不一致或长度 <8 时被拒绝

  - [ ]* 3.8 编写集成测试：密码重置完整流程（GreenMail）
    - 使用 GreenMail 模拟 SMTP 服务器，验证邮件发送内容与令牌链接格式
    - _需求：9.5、9.6_

- [ ] 4. 检查点 —— 确保所有认证相关测试通过，如有疑问请告知。

- [x] 5. 关卡与练习内容模块（后端）
  - [x] 5.1 实现关卡列表接口 `GET /levels`
    - 创建 `Category`、`Level`、`UserLevelProgress` JPA 实体与 Repository
    - 返回所有分类及其关卡列表，附带当前用户的解锁/完成状态
    - _需求：4.1、4.2、4.6_

  - [x] 5.2 实现关卡详情接口 `GET /levels/{id}`
    - 返回关卡信息及所有 `Exercise`（含 `content_en`、`content_zh`）
    - 未解锁关卡返回 403
    - _需求：2.1、2.2、4.3_

  - [x] 5.3 实现关卡完成提交接口 `POST /levels/{id}/complete`
    - 接收 `accuracy`、`timeMs`、`errorChars` 参数
    - 计算积分（基础分 100 + 正确率加成 + 用时加成），原子更新 `users.total_score`
    - 若正确率 ≥ 80%，解锁下一关（更新 `user_level_progress`）；否则不解锁
    - 更新 `user_level_progress.best_accuracy`（只增不减，对应属性 14）
    - 创建 `typing_sessions` 记录
    - 批量 upsert `word_error_stats`（对应属性 15）
    - 触发每日打卡逻辑（若当日首次完成）
    - _需求：4.4、4.5、4.7、6.1、6.2、8.2、8.3、8.4_

  - [ ]* 5.4 编写属性测试：积分计算公式正确性（Property 10）
    - **属性 10：积分计算公式正确性**
    - **验证：需求 6.1、6.2**
    - 使用 jqwik 生成任意 accuracy（0-100）、actualTime、standardTime，断言结果严格等于公式值

  - [ ]* 5.5 编写属性测试：关卡解锁与正确率的关系（Property 6）
    - **属性 6：关卡解锁与正确率的关系**
    - **验证：需求 4.4、4.5**
    - 使用 jqwik 生成任意正确率，断言 ≥80% 时下一关解锁，<80% 时不解锁

  - [ ]* 5.6 编写属性测试：历史最高正确率的单调性（Property 14）
    - **属性 14：历史最高正确率的单调性**
    - **验证：需求 8.2、8.3**
    - 使用 jqwik 生成多次练习记录序列，断言 `best_accuracy` 只增不减

  - [ ]* 5.7 编写集成测试：关卡完成后积分和解锁状态的原子更新
    - 使用 `@DataJpaTest` + `@Transactional` 验证积分更新与关卡解锁的原子性
    - _需求：4.4、6.1、6.2_

- [-] 6. 每日打卡与积分模块（后端）
  - [x] 6.1 实现打卡记录逻辑
    - 在关卡完成时检查当日是否已有 `daily_checkins` 记录，若无则创建（对应属性 8）
    - 更新 `users.streak`：若昨日有打卡则 +1，否则重置为 1；更新 `last_checkin_date`
    - 若 streak 为 7 的整数倍，额外奖励 50 积分（对应属性 9、11）
    - _需求：5.1、5.3、5.4、6.3_

  - [x] 6.2 实现打卡日历接口 `GET /checkin/calendar` 与 `GET /checkin/streak`
    - 返回过去 30 天的打卡日期列表与当前 streak 值
    - _需求：5.2、5.5_

  - [ ]* 6.3 编写属性测试：打卡记录创建的正确性（Property 8）
    - **属性 8：打卡记录创建的正确性**
    - **验证：需求 5.1**
    - 使用 jqwik 模拟同一用户同一自然日多次完成关卡，断言只创建一条打卡记录

  - [ ]* 6.4 编写属性测试：Streak 计算规则（Property 9）
    - **属性 9：Streak 计算规则**
    - **验证：需求 5.3、5.4**
    - 使用 jqwik 生成任意打卡历史序列，断言 streak 为 7 整数倍时发放奖励，断日时重置为 0

  - [ ]* 6.5 编写属性测试：打卡积分奖励的固定性（Property 11）
    - **属性 11：打卡积分奖励的固定性**
    - **验证：需求 6.3**
    - 使用 jqwik 断言每次打卡奖励恰好 +50 分，不多不少

- [-] 7. 排行榜模块（后端）
  - [x] 7.1 实现排行榜接口 `GET /leaderboard`
    - 支持参数：`type`（score/streak）、`period`（week/all）、`category`（可选）
    - 先查 Redis 缓存（TTL 600s），未命中则聚合查询 MySQL 并写入缓存
    - 返回前 100 名用户数据，同时返回当前登录用户的排名位置
    - _需求：7.1、7.2、7.3、7.4、7.5、7.6、7.7_

  - [ ]* 7.2 编写集成测试：排行榜缓存命中与未命中路径
    - 使用 EmbeddedRedis 验证缓存命中直接返回、未命中时查库并写缓存
    - _需求：7.6_

- [x] 8. 学习统计模块（后端）
  - [x] 8.1 实现个人统计接口 `GET /stats/me`
    - 聚合查询：累计练习时长、累计完成关卡数、累计输入字符数
    - _需求：8.1_

  - [x] 8.2 实现易错词汇接口 `GET /stats/weak-words`
    - 查询 `word_error_stats` 按 `error_count DESC` 取前 10 条
    - _需求：8.4_

  - [ ]* 8.3 编写属性测试：易错词汇 Top-10 排序正确性（Property 15）
    - **属性 15：易错词汇 Top-10 排序正确性**
    - **验证：需求 8.4**
    - 使用 jqwik 生成超过 10 个单词的错误统计，断言返回恰好 10 个且按错误次数降序排列

- [-] 9. 管理员系统模块（后端）
  - [x] 9.1 扩展用户表和权限系统
    - 在 `users` 表添加 `role`（USER/ADMIN）、`status`（ACTIVE/BANNED）、`banned_at`、`banned_reason` 字段
    - 创建 `admin_operation_logs` 表用于记录管理操作日志
    - 创建 `UserRole` 和 `UserStatus` 枚举类
    - 更新 `User` 实体类添加新字段
    - _需求：10.1、12.1_

  - [x] 9.2 实现超级管理员初始化
    - 创建 `AdminInitializer` 组件，在应用启动时检查并创建超级管理员账号
    - 默认账号：admin / Admin@123456
    - _需求：12.5_

  - [x] 9.3 实现 JWT 令牌包含角色信息
    - 修改 `JwtUtil`，在生成 JWT 时添加 `role` 声明
    - 修改 JWT 过滤器，从令牌中提取角色信息并设置到 Spring Security 上下文
    - _需求：12.4_

  - [x] 9.4 实现管理员权限验证
    - 配置 Spring Security，为管理员接口添加 `ADMIN` 角色验证
    - 创建 `@PreAuthorize("hasRole('ADMIN')")` 注解保护管理接口
    - 普通用户访问管理接口返回 403
    - _需求：12.2、12.3_

  - [x] 9.5 实现用户管理接口
    - `GET /admin/users` - 获取用户列表（分页、搜索、筛选）
    - `GET /admin/users/{id}` - 获取用户详情
    - `PUT /admin/users/{id}/ban` - 封禁用户
    - `PUT /admin/users/{id}/unban` - 解封用户
    - `DELETE /admin/users/{id}` - 删除用户（级联删除相关数据）
    - `POST /admin/users/{id}/reset-password` - 重置用户密码
    - `PUT /admin/users/{id}/role` - 修改用户角色
    - _需求：10.3、10.4、10.5、10.6、10.7、10.8、10.9、10.10、12.6_

  - [x] 9.6 实现分类管理接口
    - `GET /admin/categories` - 获取所有分类
    - `GET /admin/categories/{id}` - 获取分类详情
    - `POST /admin/categories` - 创建新分类
    - `PUT /admin/categories/{id}` - 更新分类信息
    - `DELETE /admin/categories/{id}` - 删除分类（检查是否有关卡）
    - _需求：11.1、11.2、11.3_

  - [x] 9.7 实现关卡管理接口
    - `GET /admin/levels` - 获取所有关卡（可按分类筛选）
    - `GET /admin/levels/{id}` - 获取关卡详情
    - `POST /admin/levels` - 创建新关卡
    - `PUT /admin/levels/{id}` - 更新关卡信息
    - `DELETE /admin/levels/{id}` - 删除关卡（检查用户进度）
    - _需求：11.4、11.5、11.6、11.10、11.11_

  - [x] 9.8 实现练习内容管理接口
    - `GET /admin/exercises` - 获取练习内容列表（按关卡筛选）
    - `GET /admin/exercises/{id}` - 获取练习内容详情
    - `POST /admin/exercises` - 创建新练习内容
    - `POST /admin/exercises/batch` - 批量创建练习内容
    - `PUT /admin/exercises/{id}` - 更新练习内容
    - `DELETE /admin/exercises/{id}` - 删除练习内容
    - _需求：11.7、11.8、11.9、11.12_

  - [x] 9.9 实现操作日志记录
    - 创建 `AdminOperationLog` 实体类和 Repository
    - 创建 `@AdminOperation` 注解
    - 实现 AOP 切面自动记录所有管理操作
    - 记录操作人、操作类型、操作对象、操作详情、IP 地址
    - _需求：12.7_

  - [x] 9.10 实现操作日志查询接口
    - `GET /admin/logs` - 获取操作日志（分页、筛选）
    - 支持按操作人、操作类型、对象类型、时间范围筛选
    - _需求：12.7_

  - [ ]* 9.11 编写管理员功能集成测试
    - 测试管理员权限验证（普通用户访问管理接口返回 403）
    - 测试用户管理完整流程（封禁、解封、删除）
    - 测试题库管理完整流程（创建分类、关卡、练习内容）
    - 测试操作日志记录功能
    - _需求：10、11、12_

- [x] 10. 数据初始化脚本
  - [x] 10.1 编写 ECDICT 词库导入脚本
    - 创建 `src/main/resources/db/seed/import_ecdict.sql`
    - 按五个分类过滤条件（小学 frq>5000、初中 zk、高中 gk、四级 cet4、六级 cet6）插入 `exercises` 表
    - 每个分类初始 10-25 个关卡，每关 20 个词
    - _需求：4.1_

  - [x] 10.2 编写 Tatoeba 例句导入脚本
    - 创建 Python 脚本 `scripts/import_tatoeba.py`，提取中英对照句子并按长度分级
    - 生成 SQL 插入语句，为每个分类补充 5-10 个短句关卡
    - _需求：4.1_

- [ ] 11. 检查点 —— 确保所有后端测试通过，如有疑问请告知。

- [-] 12. 前端认证页面
  - [x] 12.1 实现登录页 `LoginPage`
    - 表单：邮箱 + 密码，提交调用 `POST /auth/login`，成功后跳转主页
    - 展示"忘记密码"入口链接
    - 响应式布局（移动端全宽，桌面居中卡片）
    - _需求：1.4、9.1_

  - [x] 12.2 实现注册页 `RegisterPage`
    - 表单：用户名 + 邮箱 + 密码，前端校验邮箱格式与密码长度（≥8位）
    - 提交调用 `POST /auth/register`，成功后自动登录跳转主页
    - _需求：1.1、1.2、1.3、1.5_

  - [x] 12.3 实现密码重置页面（申请页 + 重置表单页）
    - 申请页：输入邮箱，提交调用 `POST /auth/forgot-password`，展示成功/错误提示
    - 重置表单页：从 URL 读取 token，输入新密码+确认密码，提交调用 `POST /auth/reset-password`
    - 前端校验两次密码一致性与长度
    - _需求：9.1、9.7、9.8、9.9、9.10、9.11_

  - [x] 12.4 实现 Pinia 认证 Store（`useAuthStore`）
    - 管理 access token、refresh token、用户信息的持久化（localStorage）
    - 实现 token 自动刷新拦截器（axios）
    - _需求：1.6_

- [ ] 13. 前端关卡列表与预览页面
  - [x] 13.1 实现关卡列表页 `LevelListPage`
    - 展示五个难度分类 Tab，每个分类下的关卡网格（手机单列，桌面双列/三列）
    - 以视觉区分方式展示已解锁（可点击）、已完成（带勾）、未解锁（灰色锁图标）状态
    - _需求：4.1、4.2、4.6、10_

  - [ ]* 13.2 编写属性测试：关卡状态渲染的完整性（Property 7，前端）
    - **属性 7：关卡状态渲染的完整性**
    - **验证：需求 4.6**
    - 使用 fast-check 生成任意关卡状态列表，断言三种状态的视觉标识互不相同

  - [x] 13.3 实现关卡详情预览页 `LevelDetailPage`
    - 进入关卡后先展示练习内容预览列表（英文 + 中文释义，可滚动）
    - 提供"开始练习"按钮，点击后进入打字练习界面
    - _需求：2.1、2.2、2.3、2.4_

  - [ ]* 13.4 编写属性测试：练习内容预览包含双语信息（Property 3，前端）
    - **属性 3：练习内容预览包含双语信息**
    - **验证：需求 2.2**
    - 使用 fast-check 生成任意练习内容列表，断言每条渲染结果同时包含英文和中文字段

- [ ] 14. 前端打字引擎与练习页面
  - [x] 14.1 实现 `compareTyping` 工具函数
    - 输入：目标字符串 `target`、已输入字符串 `input`
    - 输出：每个字符的状态数组（`correct`/`incorrect`/`pending`/`current`）+ 正确率
    - 纯函数，无副作用，确保执行时间 < 5ms（1000字符场景）
    - _需求：3.1、3.2、3.3_

  - [ ]* 14.2 编写属性测试：打字对比逻辑正确性（Property 4，前端）
    - **属性 4：打字对比逻辑正确性**
    - **验证：需求 3.1、3.2、3.3**
    - 使用 fast-check 生成任意 target 和 input，断言字符状态标记正确，正确率 = correct数/总输入数×100

  - [ ]* 14.3 编写属性测试：退格操作正确回退状态（Property 5，前端）
    - **属性 5：退格操作正确回退状态**
    - **验证：需求 3.5**
    - 使用 fast-check 生成任意输入序列，在任意位置插入退格，断言已输入列表长度减 1 且最后字符被移除

  - [x] 14.4 实现 `TypingEngine` 组件
    - 监听 `keydown` 事件，同步调用 `compareTyping`，通过 Vue `ref` 更新状态
    - 禁止 `paste` 事件（`event.preventDefault()`）
    - 支持退格键删除最后一个字符
    - 当前练习内容完成后自动切换至下一条
    - _需求：3.1、3.4、3.5、3.6_

  - [x] 14.5 实现 `CharDisplay` 组件
    - 根据字符状态数组渲染带颜色高亮的字符序列（绿色=correct，红色=incorrect）
    - 手机端字体缩小，字符区域支持单行横向滚动；桌面端多行展示
    - _需求：3.2、10_

  - [x] 14.6 实现打字练习页 `TypingPage`
    - 集成 `TypingEngine` + `CharDisplay`，实时显示正确率
    - 所有练习完成后调用 `POST /levels/{id}/complete` 提交结果
    - 展示结果弹窗（正确率、用时、获得积分），提示是否解锁下一关
    - _需求：3.3、4.4、4.5、4.7、6.1_

  - [ ]* 14.7 编写打字引擎性能测试
    - 验证 1000 字符目标字符串下 `compareTyping` 执行时间 < 5ms
    - _需求：3.1_

- [x] 15. 检查点 —— 确保所有前端打字引擎测试通过，如有疑问请告知。

- [ ] 16. 前端主页、打卡日历与统计页面
  - [x] 16.1 实现主页 `HomePage`
    - 展示当前 streak、累计积分、累计完成关卡数
    - 集成打卡日历组件（过去 30 天，已打卡日期高亮）
    - _需求：5.2、5.5、6.4、8.1_

  - [D] 16.2 实现个人统计页 `ProfilePage`
    - 展示累计练习时长、累计输入字符数、各关卡历史最高正确率
    - 展示易错词汇 Top 10 列表b
    - _需求：8.1、8.2、8.3、8.4_

- [ ] 17. 前端排行榜页面
  - [x] 17.1 实现排行榜页 `LeaderboardPage`
    - Tab 切换：总积分榜 / 连续打卡榜
    - 筛选维度：全部 + 五个难度分类
    - 时间维度：本周榜 / 总榜
    - 展示前 100 名（用户名、头像、积分/streak），高亮当前登录用户
    - 手机端隐藏次要列，只保留排名、用户名、分数
    - _需求：7.1、7.2、7.3、7.4、7.5、7.7、10_

  - [ ]* 17.2 编写属性测试：排行榜渲染包含必要字段（Property 12，前端）
    - **属性 12：排行榜渲染包含必要字段**
    - **验证：需求 7.4**
    - 使用 fast-check 生成任意用户数据列表，断言每条记录包含用户名、头像、积分字段，总条数 ≤ 100

  - [ ]* 17.3 编写属性测试：当前用户排名高亮的正确性（Property 13，前端）
    - **属性 13：当前用户排名高亮的正确性**
    - **验证：需求 7.5**
    - 使用 fast-check 生成任意排行榜数据和当前用户，断言只有当前用户条目被标记高亮

- [x] 18. 导航栏与响应式布局完善
  - [x] 18.1 实现导航栏组件 `NavBar`
    - 桌面端展示完整导航链接；手机端折叠为汉堡菜单（点击展开）
    - 包含：主页、关卡、排行榜、个人中心、退出登录
    - _需求：10_

- [x] 17. 数据初始化脚本
  - [x] 17.1 编写 ECDICT 词库导入脚本
    - 创建 `src/main/resources/db/seed/import_ecdict.sql`
    - 按五个分类过滤条件（小学 frq>5000、初中 zk、高中 gk、四级 cet4、六级 cet6）插入 `exercises` 表
    - 每个分类初始 10-25 个关卡，每关 20 个词
    - _需求：4.1_

  - [x] 17.2 编写 Tatoeba 例句导入脚本
    - 创建 Python 脚本 `scripts/import_tatoeba.py`，提取中英对照句子并按长度分级
    - 生成 SQL 插入语句，为每个分类补充 5-10 个短句关卡
    - _需求：4.1_

- [ ] 20. E2E 测试
  - [ ]* 20.1 编写 Playwright E2E 测试：核心用户流程
    - 测试流程：注册 → 登录 → 进入关卡 → 完成打字练习 → 查看结果 → 查看排行榜
    - _需求：1.1、1.4、2.1、3.1、4.4、7.1_

- [ ] 21. 最终检查点 —— 确保所有测试通过，如有疑问请告知。

---

## 备注

- 标有 `*` 的子任务为可选项，可在 MVP 阶段跳过以加快交付
- 每个任务均引用了具体需求条款，确保可追溯性
- 属性测试每个至少运行 200 次迭代，注释格式：`Feature: english-typing-learning, Property {N}: {属性标题}`
- 打字检测逻辑完全在前端完成，不发送网络请求，保证 ≤50ms 响应
- SMTP 配置、数据库密码等敏感信息均通过 `application.yml` 或环境变量注入，不硬编码


- [ ] 20. 管理员系统模块（后端）
  - [x] 20.1 扩展用户表和权限系统
    - 在 `users` 表添加 `role`（USER/ADMIN）、`status`（ACTIVE/BANNED）、`banned_at`、`banned_reason` 字段
    - 创建 `admin_operation_logs` 表用于记录管理操作日志
    - 创建 `UserRole` 和 `UserStatus` 枚举类
    - 更新 `User` 实体类添加新字段
    - _需求：10.1、12.1_

  - [x] 20.2 实现超级管理员初始化
    - 创建 `AdminInitializer` 组件，在应用启动时检查并创建超级管理员账号
    - 默认账号：admin / Admin@123456
    - _需求：12.5_

  - [x] 20.3 实现 JWT 令牌包含角色信息
    - 修改 `JwtUtil`，在生成 JWT 时添加 `role` 声明
    - 修改 JWT 过滤器，从令牌中提取角色信息并设置到 Spring Security 上下文
    - _需求：12.4_

  - [x] 20.4 实现管理员权限验证
    - 配置 Spring Security，为管理员接口添加 `ADMIN` 角色验证
    - 创建 `@PreAuthorize("hasRole('ADMIN')")` 注解保护管理接口
    - 普通用户访问管理接口返回 403
    - _需求：12.2、12.3_

  - [x] 20.5 实现用户管理接口
    - `GET /admin/users` - 获取用户列表（分页、搜索、筛选）
    - `GET /admin/users/{id}` - 获取用户详情
    - `PUT /admin/users/{id}/ban` - 封禁用户
    - `PUT /admin/users/{id}/unban` - 解封用户
    - `DELETE /admin/users/{id}` - 删除用户（级联删除相关数据）
    - `POST /admin/users/{id}/reset-password` - 重置用户密码
    - `PUT /admin/users/{id}/role` - 修改用户角色
    - _需求：10.3、10.4、10.5、10.6、10.7、10.8、10.9、10.10、12.6_

  - [x] 20.6 实现分类管理接口
    - `GET /admin/categories` - 获取所有分类
    - `GET /admin/categories/{id}` - 获取分类详情
    - `POST /admin/categories` - 创建新分类
    - `PUT /admin/categories/{id}` - 更新分类信息
    - `DELETE /admin/categories/{id}` - 删除分类（检查是否有关卡）
    - _需求：11.1、11.2、11.3_

  - [x] 20.7 实现关卡管理接口
    - `GET /admin/levels` - 获取所有关卡（可按分类筛选）
    - `GET /admin/levels/{id}` - 获取关卡详情
    - `POST /admin/levels` - 创建新关卡
    - `PUT /admin/levels/{id}` - 更新关卡信息
    - `DELETE /admin/levels/{id}` - 删除关卡（检查用户进度）
    - _需求：11.4、11.5、11.6、11.10、11.11_

  - [x] 20.8 实现练习内容管理接口
    - `GET /admin/exercises` - 获取练习内容列表（按关卡筛选）
    - `GET /admin/exercises/{id}` - 获取练习内容详情
    - `POST /admin/exercises` - 创建新练习内容
    - `POST /admin/exercises/batch` - 批量创建练习内容
    - `PUT /admin/exercises/{id}` - 更新练习内容
    - `DELETE /admin/exercises/{id}` - 删除练习内容
    - _需求：11.7、11.8、11.9、11.12_

  - [x] 20.9 实现操作日志记录
    - 创建 `AdminOperationLog` 实体类和 Repository
    - 创建 `@AdminOperation` 注解
    - 实现 AOP 切面自动记录所有管理操作
    - 记录操作人、操作类型、操作对象、操作详情、IP 地址
    - _需求：12.7_

  - [x] 20.10 实现操作日志查询接口
    - `GET /admin/logs` - 获取操作日志（分页、筛选）
    - 支持按操作人、操作类型、对象类型、时间范围筛选
    - _需求：12.7_

  - [ ]* 20.11 编写管理员功能集成测试
    - 测试管理员权限验证（普通用户访问管理接口返回 403）
    - 测试用户管理完整流程（封禁、解封、删除）
    - 测试题库管理完整流程（创建分类、关卡、练习内容）
    - 测试操作日志记录功能
    - _需求：10、11、12_

- [x] 21. 管理员前端页面（可选）
  - [x] 21.1 实现管理员登录页
    - 与普通用户共用登录接口，根据角色跳转不同页面
    - 管理员登录后跳转到管理后台首页
    - _需求：10.2_

  - [x] 21.2 实现用户管理页面
    - 用户列表展示（表格形式，支持分页）
    - 搜索和筛选功能（用户名、邮箱、角色、状态）
    - 用户详情查看（弹窗或侧边栏）
    - 封禁/解封/删除操作（带二次确认）
    - 重置密码功能
    - _需求：10.3、10.4、10.5、10.6、10.7、10.8、10.9、10.10_

  - [x] 21.3 实现分类管理页面
    - 分类列表展示
    - 创建/编辑分类表单
    - 删除分类（检查关联关卡）
    - _需求：11.1、11.2、11.3_

  - [x] 21.4 实现关卡管理页面
    - 关卡列表展示（按分类分组）
    - 创建/编辑关卡表单
    - 删除关卡（检查用户进度）
    - 关卡排序调整
    - _需求：11.4、11.5、11.6_

  - [x] 21.5 实现练习内容管理页面
    - 练习内容列表展示（按关卡筛选）
    - 创建/编辑练习内容表单
    - 批量导入功能（CSV/Excel）
    - 删除练习内容
    - 练习内容排序调整
    - _需求：11.7、11.8、11.9_

  - [x] 21.6 实现操作日志查询页面
    - 操作日志列表展示（表格形式，支持分页）
    - 筛选功能（操作人、操作类型、时间范围）
    - 操作详情查看
    - _需求：12.7_

  - [x] 21.7 实现管理后台导航和布局
    - 侧边栏导航菜单（用户管理、题库管理、操作日志）
    - 顶部导航栏（显示管理员信息、退出登录）
    - 响应式布局适配
    - _需求：10、11、12_

- [ ] 22. 检查点 —— 确保所有管理员功能测试通过，如有疑问请告知。


- [x] 20. 管理员系统模块（后端）
  - [x] 20.1 扩展用户表和权限系统
    - 在 `users` 表添加 `role`（USER/ADMIN）、`status`（ACTIVE/BANNED）、`banned_at`、`banned_reason` 字段
    - 创建 `admin_operation_logs` 表用于记录管理操作日志
    - 创建 `UserRole` 和 `UserStatus` 枚举类
    - 更新 `User` 实体类添加新字段
    - _需求：10.1、12.1_

  - [x] 20.2 实现超级管理员初始化
    - 创建 `AdminInitializer` 组件，在应用启动时检查并创建超级管理员账号
    - 默认账号：admin / Admin@123456
    - _需求：12.5_

  - [x] 20.3 实现 JWT 令牌包含角色信息
    - 修改 `JwtUtil`，在生成 JWT 时添加 `role` 声明
    - 修改 JWT 过滤器，从令牌中提取角色信息并设置到 Spring Security 上下文
    - _需求：12.4_

  - [x] 20.4 实现管理员权限验证
    - 配置 Spring Security，为管理员接口添加 `ADMIN` 角色验证
    - 创建 `@PreAuthorize("hasRole('ADMIN')")` 注解保护管理接口
    - 普通用户访问管理接口返回 403
    - _需求：12.2、12.3_

  - [x] 20.5 实现用户管理接口
    - `GET /admin/users` - 获取用户列表（分页、搜索、筛选）
    - `GET /admin/users/{id}` - 获取用户详情
    - `PUT /admin/users/{id}/ban` - 封禁用户
    - `PUT /admin/users/{id}/unban` - 解封用户
    - `DELETE /admin/users/{id}` - 删除用户（级联删除相关数据）
    - `POST /admin/users/{id}/reset-password` - 重置用户密码
    - `PUT /admin/users/{id}/role` - 修改用户角色
    - _需求：10.3、10.4、10.5、10.6、10.7、10.8、10.9、10.10、12.6_

  - [x] 20.6 实现分类管理接口
    - `GET /admin/categories` - 获取所有分类
    - `GET /admin/categories/{id}` - 获取分类详情
    - `POST /admin/categories` - 创建新分类
    - `PUT /admin/categories/{id}` - 更新分类信息
    - `DELETE /admin/categories/{id}` - 删除分类（检查是否有关卡）
    - _需求：11.1、11.2、11.3_

  - [x] 20.7 实现关卡管理接口
    - `GET /admin/levels` - 获取所有关卡（可按分类筛选）
    - `GET /admin/levels/{id}` - 获取关卡详情
    - `POST /admin/levels` - 创建新关卡
    - `PUT /admin/levels/{id}` - 更新关卡信息
    - `DELETE /admin/levels/{id}` - 删除关卡（检查用户进度）
    - _需求：11.4、11.5、11.6、11.10、11.11_

  - [x] 20.8 实现练习内容管理接口
    - `GET /admin/exercises` - 获取练习内容列表（按关卡筛选）
    - `GET /admin/exercises/{id}` - 获取练习内容详情
    - `POST /admin/exercises` - 创建新练习内容
    - `POST /admin/exercises/batch` - 批量创建练习内容
    - `PUT /admin/exercises/{id}` - 更新练习内容
    - `DELETE /admin/exercises/{id}` - 删除练习内容
    - _需求：11.7、11.8、11.9、11.12_

  - [x] 20.9 实现操作日志记录
    - 创建 `AdminOperationLog` 实体类和 Repository
    - 创建 `@AdminOperation` 注解
    - 实现 AOP 切面自动记录所有管理操作
    - 记录操作人、操作类型、操作对象、操作详情、IP 地址
    - _需求：12.7_

  - [x] 20.10 实现操作日志查询接口
    - `GET /admin/logs` - 获取操作日志（分页、筛选）
    - 支持按操作人、操作类型、对象类型、时间范围筛选
    - _需求：12.7_

  - [ ]* 20.11 编写管理员功能集成测试
    - 测试管理员权限验证（普通用户访问管理接口返回 403）
    - 测试用户管理完整流程（封禁、解封、删除）
    - 测试题库管理完整流程（创建分类、关卡、练习内容）
    - 测试操作日志记录功能
    - _需求：10、11、12_


- [x] 23. 指法训练模块（前端）
  - [x] 23.1 实现置信度算法与进度管理工具函数
    - 创建 `front/src/utils/fingerTyping.ts`
    - 实现置信度计算：`confidence = min(100, correct / (correct + incorrect * 2) * 100)`
    - 实现按键解锁逻辑：所有已解锁按键置信度 ≥ 100% 时解锁下一组
    - 实现练习序列生成算法：优先生成置信度低的按键，穿插已掌握按键
    - 实现 localStorage 持久化读写（`FingerTypingProgress` 接口）
    - _需求：13.3、13.8、13.11_

  - [x] 23.2 实现可视化键盘组件 `KeyboardDisplay.vue`
    - 渲染标准 QWERTY 键盘布局（3行字母键 + 空格键）
    - 根据置信度显示热力图颜色（绿/黄/红/灰锁/蓝高亮）
    - 显示手指分区颜色（左右手各四指）
    - 高亮当前待按键
    - _需求：13.4、13.7_

  - [x] 23.3 实现练习引擎组件 `FingerTypingEngine.vue`
    - 监听 `keydown` 事件，实时比对输入
    - 正确输入：更新置信度，切换到下一字符
    - 错误输入：高亮错误，记录错误次数，增加该键练习权重
    - 序列完成后自动生成下一组练习序列
    - 禁止粘贴
    - _需求：13.5、13.6、13.8、13.9_

  - [x] 23.4 实现实时统计组件 `TypingStats.vue`
    - 实时显示 WPM（每分钟单词数）
    - 实时显示准确率
    - 计时器（从第一次按键开始）
    - 显示按键解锁进度（已解锁 / 总按键数）
    - _需求：13.6_

  - [x] 23.5 实现指法训练主页面 `FingerTypingView.vue`
    - 集成 KeyboardDisplay + FingerTypingEngine + TypingStats
    - 显示当前练习序列（高亮当前字符、已输入字符）
    - 显示当前待按键和下一个按键提示
    - 实现"重置本组"和"重置全部进度"按钮
    - 移动端显示提示"请使用物理键盘进行指法训练"
    - _需求：13.1、13.2、13.10、13.12_

  - [x] 23.6 添加路由和导航入口
    - 在 `router/index.ts` 添加 `/finger-typing` 路由
    - 在 `NavBar.vue` 导航栏添加"指法训练"入口（移动端隐藏）
    - _需求：13.1、13.12_


- [ ] 24. 天梯挑战模块（后端）
  - [x] 24.1 创建挑战记录数据库表
    - 执行 SQL 创建 `challenge_records` 表
    - 创建 `ChallengeRecord` JPA 实体类
    - 创建 `ChallengeRecordRepository`
    - _需求：14.9_

  - [x] 24.2 实现随机练习内容接口
    - `GET /challenge/exercises?type={WORD|SENTENCE}&count=20`
    - 单词：`LENGTH(content_en) <= 20` 且不含空格
    - 短句：`LENGTH(content_en) > 20` 或含空格
    - 使用 `ORDER BY RAND() LIMIT ?` 随机获取
    - _需求：14.4_

  - [x] 24.3 实现提交挑战结果接口
    - `POST /challenge/submit`
    - 接收 contentType、timeMode、timeLimit、passedCount、accuracy、timeMs、wpm
    - 保存到 `challenge_records` 表
    - _需求：14.9_

  - [x] 24.4 实现天梯排行榜接口
    - `GET /challenge/leaderboard?type=&mode=&timeLimit=`
    - 限时榜：按 passedCount DESC, accuracy DESC
    - 无限榜：按 passedCount DESC, timeMs ASC
    - 返回前 100 名 + 当前用户排名
    - _需求：14.10、14.11_

  - [x] 24.5 实现历史记录接口
    - `GET /challenge/history?page=0&size=10`
    - 返回当前用户最近的挑战记录
    - _需求：14.12_

- [x] 25. 天梯挑战模块（前端）
  - [x] 25.1 创建挑战相关 API 文件
    - 创建 `front/src/api/challenge.ts`
    - 封装 getRandomExercises、submitChallenge、getChallengeLeaderboard、getChallengeHistory
    - _需求：14.4、14.9、14.10_

  - [x] 25.2 实现挑战配置页面
    - 内容类型选择：单词 / 短句
    - 时间模式选择：60秒 / 120秒 / 300秒 / 无限
    - "开始挑战"按钮
    - _需求：14.2、14.3_

  - [x] 25.3 实现挑战游戏主界面
    - 顶部状态栏：倒计时/已用时、已通过数、正确率
    - 复用 TypingEngine + CharDisplay 组件
    - 显示中文提示
    - 跳过按钮（不计入通过数）
    - 停止按钮（无限模式）
    - 预加载机制：剩余 5 条时自动请求下一批
    - _需求：14.4、14.5、14.6、14.7_

  - [x] 25.4 实现挑战结果弹窗
    - 展示通过数量、正确率、WPM、用时（无限模式）
    - 提交结果到后端
    - 按钮：查看排行榜 / 再来一次 / 返回
    - _需求：14.8、14.9_

  - [x] 25.5 实现天梯排行榜页面
    - Tab：单词榜 / 短句榜
    - 子 Tab：限时（60s/120s/300s）/ 无限
    - 高亮当前用户排名
    - 展示历史记录（最近10次）
    - _需求：14.10、14.11、14.12_

  - [x] 25.6 添加路由和导航入口
    - 在 `router/index.ts` 添加 `/challenge` 路由
    - 在 `NavBar.vue` 添加"天梯挑战"入口
    - _需求：14.1_
