# 🎯 英语打字学习系统 (English Typing Learning System)

一个专为英语学习者设计的打字练习平台，通过分级练习、天梯挑战和精准统计，帮助用户在提升打字速度的同时掌握英语词汇和短语。

---

## 📋 目录

- [项目简介](#项目简介)
- [核心功能](#核心功能)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [后端文档](#后端文档)
  - [环境要求](#环境要求)
  - [快速开始](#快速开始)
  - [配置说明](#配置说明)
  - [API 接口](#api-接口)
- [前端文档](#前端文档)
  - [环境要求](#前端环境要求)
  - [快速开始](#前端快速开始)
  - [功能模块](#功能模块)
  - [页面说明](#页面说明)
- [数据库初始化](#数据库初始化)
- [Docker 部署](#docker-部署)
- [开发规范](#开发规范)
- [常见问题](#常见问题)

---

## 🌟 项目简介

**英语打字学习系统** 是一个集打字练习、词汇学习、天梯挑战于一体的综合性学习平台。系统采用分级练习模式，从简单单词到复杂短句循序渐进，配合实时语音播报、精准错误统计和可视化热力图，为用户提供沉浸式的英语学习体验。

### 主要特点

- 🎯 **分级练习**：从基础单词到高级短句，循序渐进
- ⚡ **天梯挑战**：限时/无限模式，冲击排行榜
- 🔊 **语音播报**：实时朗读，强化听力记忆
- 📊 **精准统计**：错误词汇追踪，针对性提升
- 🎮 **指法训练**：键盘热力图，规范打字姿势
- 📱 **响应式设计**：完美适配桌面端和移动端
- 🏆 **打卡系统**：连续打卡激励，养成学习习惯

---

## 🚀 核心功能

### 1. 用户系统
- ✅ 用户注册/登录（JWT 认证）
- ✅ 密码找回与重置（邮件验证）
- ✅ 个人资料管理（头像、昵称、简介）
- ✅ 用户主页（公开可见）

### 2. 关卡练习系统
- 📝 **分级关卡**：按难度分类，从单词到短句
- 🔓 **逐级解锁**：完成当前关卡解锁下一关（正确率 ≥ 80%）
- 🎯 **实时反馈**：打字过程中实时显示正确/错误状态
- 🔊 **语音播报**：自动/手动朗读单词或短语
- 📊 **结果统计**：正确率、用时、获得积分
- 📈 **历史记录**：各关卡最佳正确率追踪

### 3. 天梯挑战系统
- ⏱️ **限时挑战**：60秒/120秒/300秒模式
- ♾️ **无限模式**：自由挑战，不计时间
- 📝 **内容类型**：单词挑战 / 短句挑战
- 🏆 **排行榜**：积分榜、连续打卡榜、天梯挑战榜
- 📊 **数据统计**：WPM（每分钟字数）、正确率、用时

### 4. 指法训练系统
- ⌨️ **键位解锁**：从 F、J 开始，逐步解锁全键盘
- 🎹 **键盘热力图**：可视化显示按键熟练度
- 📊 **实时统计**：WPM、准确率、练习时间
- 🔄 **分组练习**：按手指分组，针对性训练
- ⚡ **进度保存**：自动保存练习进度

### 5. 统计与分析
- 📈 **总积分**：累计练习获得的积分
- 🔥 **连续打卡**：连续打卡天数统计
- ⏱️ **练习时长**：总练习时间统计
- ⌨️ **输入字符**：累计输入字符数
- 📝 **易错词汇**：Top 10 错误词汇追踪
- 🎯 **关卡进度**：各关卡历史最高正确率

### 6. 管理系统（Admin）
- 👥 **用户管理**：查看用户列表、封禁/解封用户
- 📝 **内容管理**：创建/编辑关卡、分类、练习内容
- 📊 **操作日志**：记录管理员操作历史
- 🔧 **批量导入**：支持批量导入练习内容

---

## 💻 技术栈

### 后端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| **Spring Boot** | 3.2.5 | 核心框架 |
| **Java** | 17 | 编程语言 |
| **Spring Security** | - | 安全认证 |
| **JWT (jjwt)** | 0.12.5 | Token 认证 |
| **Spring Data JPA** | - | 数据持久层 |
| **MySQL** | 8.0 | 关系型数据库 |
| **Redis** | 7 | 缓存服务 |
| **Spring Mail** | - | 邮件发送 |
| **MinIO** | 8.5.2 | 对象存储（头像等） |
| **Lombok** | - | 简化实体类开发 |
| **Validation** | - | 参数校验 |
| **AOP** | - | 切面编程（操作日志） |

### 前端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| **Vue 3** | ^3.4.0 | 核心框架（Composition API） |
| **TypeScript** | ^5.4.0 | 类型安全 |
| **Vite** | ^5.2.0 | 构建工具 |
| **Vue Router** | ^4.3.0 | 路由管理 |
| **Pinia** | ^2.1.0 | 状态管理 |
| **Axios** | ^1.6.0 | HTTP 客户端 |
| **TailwindCSS** | ^3.4.0 | 原子化 CSS 框架 |
| **Web Speech API** | - | 浏览器语音合成 |

### 开发工具
| 工具 | 说明 |
|------|------|
| **Docker Compose** | 容器化部署（MySQL + Redis） |
| **Maven** | 后端依赖管理 |
| **npm** | 前端依赖管理 |
| **Vitest** | 前端单元测试 |
| **Playwright** | E2E 测试 |

---

## 📁 项目结构

```
Learn-English/
├── back/                          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/englishtyping/
│   │   │   │   ├── config/        # 配置类（Security、JWT、Redis）
│   │   │   │   ├── controller/    # 控制器（REST API）
│   │   │   │   ├── dto/           # 数据传输对象
│   │   │   │   ├── entity/        # JPA 实体类
│   │   │   │   ├── repository/    # 数据访问层
│   │   │   │   ├── service/       # 业务逻辑层
│   │   │   │   ├── aspect/        # AOP 切面
│   │   │   │   ├── exception/     # 全局异常处理
│   │   │   │   └── utils/         # 工具类
│   │   │   └── resources/
│   │   │       ├── application.yml      # 应用配置
│   │   │       └── application-prod.yml # 生产环境配置
│   │   └── test/                  # 单元测试
│   └── pom.xml                    # Maven 配置
│
├── front/                         # 前端项目
│   ├── src/
│   │   ├── api/                   # API 接口封装
│   │   ├── assets/                # 静态资源
│   │   ├── components/            # 公共组件
│   │   │   ├── TypingEngine.vue   # 打字引擎核心组件
│   │   │   ├── CharDisplay.vue    # 字符显示组件
│   │   │   ├── KeyboardDisplay.vue # 键盘热力图组件
│   │   │   └── FireworksEffect.vue # 烟花特效组件
│   │   ├── router/                # 路由配置
│   │   ├── stores/                # Pinia 状态管理
│   │   ├── utils/                 # 工具函数
│   │   ├── views/                 # 页面组件
│   │   │   ├── HomeView.vue       # 首页
│   │   │   ├── LoginView.vue      # 登录
│   │   │   ├── RegisterView.vue   # 注册
│   │   │   ├── PracticeView.vue   # 关卡练习
│   │   │   ├── ChallengeView.vue  # 天梯挑战
│   │   │   ├── FingerTypingView.vue # 指法训练
│   │   │   ├── ProfileView.vue    # 个人资料
│   │   │   ├── LeaderboardView.vue # 排行榜
│   │   │   └── admin/             # 管理后台页面
│   │   └── App.vue                # 根组件
│   ├── package.json               # npm 配置
│   ├── vite.config.ts             # Vite 配置
│   └── tailwind.config.js         # TailwindCSS 配置
│
├── sql/                           # 数据库脚本
│   ├── init_database.sql          # 数据库初始化
│   ├── seed_levels.sql            # 关卡数据
│   └── seed_exercises.sql         # 练习内容数据
│
└── docker-compose.yml             # Docker 编排文件
```

---

## 🔧 后端文档

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.8+ 
- **MySQL**: 8.0
- **Redis**: 7.x
- **MinIO**: （可选，用于文件存储）

### 快速开始

#### 1. 克隆项目

```bash
git clone <repository-url>
cd Learn-English/back
```

#### 2. 启动数据库和缓存服务

```bash
# 使用 Docker Compose 启动 MySQL 和 Redis
cd ..
docker-compose up -d
```

#### 3. 初始化数据库

```bash
# 进入 SQL 目录
cd sql

# 执行初始化脚本
mysql -h localhost -u root -proot english_typing < init_database.sql
mysql -h localhost -u root -proot english_typing < seed_levels.sql
mysql -h localhost -u root -proot english_typing < seed_exercises.sql
```

#### 4. 配置应用

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/english_typing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
    username: root
    password: root
  
  data:
    redis:
      host: localhost
      port: 6379
  
  mail:
    host: smtp.example.com
    port: 587
    username: your-email@example.com
    password: your-password
```

#### 5. 启动应用

```bash
# 方式一：使用 Maven
mvn spring-boot:run

# 方式二：使用 IDE
# 运行 EnglishTypingApplication.java

# 方式三：打包后运行
mvn clean package -DskipTests
java -jar target/english-typing-backend-0.0.1-SNAPSHOT.jar
```

#### 6. 验证启动

后端默认运行在：`http://localhost:8080`

### 配置说明

#### 核心配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `spring.datasource.url` | 数据库连接地址 | `jdbc:mysql://localhost:3306/english_typing` |
| `spring.data.redis.host` | Redis 主机地址 | `localhost` |
| `spring.mail.host` | SMTP 服务器地址 | - |
| `jwt.secret` | JWT 密钥 | 需在配置文件中设置 |
| `jwt.expiration` | Token 过期时间（毫秒） | 86400000（24小时） |
| `app.frontend-url` | 前端地址（用于邮件链接） | `http://localhost:5173` |

#### MinIO 配置（可选）

```yaml
x-file-storage:
  minio:
    url: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
    bucket-name: english-typing
```

### API 接口

#### 认证相关

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | `/api/auth/register` | 注册新用户 |
| 用户登录 | POST | `/api/auth/login` | 登录获取 Token |
| 忘记密码 | POST | `/api/auth/forgot-password` | 发送重置邮件 |
| 重置密码 | POST | `/api/auth/reset-password` | 验证 Token 重置密码 |

#### 关卡相关

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取关卡列表 | GET | `/api/levels` | 获取所有分类及关卡 |
| 获取关卡详情 | GET | `/api/levels/{id}` | 获取关卡练习内容 |
| 完成关卡 | POST | `/api/levels/{id}/complete` | 提交练习结果 |

#### 挑战相关

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取随机练习 | GET | `/api/challenge/exercises` | 随机获取练习内容 |
| 提交挑战结果 | POST | `/api/challenge/submit` | 提交挑战成绩 |
| 获取挑战排行榜 | GET | `/api/challenge/leaderboard` | 获取排行榜数据 |
| 获取历史记录 | GET | `/api/challenge/history` | 获取个人历史 |

#### 用户相关

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取个人资料 | GET | `/api/users/profile` | 获取当前用户资料 |
| 更新个人资料 | PUT | `/api/users/profile` | 更新昵称、简介等 |
| 上传头像 | POST | `/api/users/avatar` | 上传用户头像 |
| 获取用户统计 | GET | `/api/users/stats` | 获取练习统计数据 |
| 查看用户主页 | GET | `/api/users/{username}` | 查看其他用户主页 |

#### 排行榜相关

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取积分排行榜 | GET | `/api/leaderboard` | 按积分或打卡排序 |
| 获取打卡日历 | GET | `/api/checkin/calendar` | 获取打卡日历 |
| 获取连续打卡 | GET | `/api/checkin/streak` | 获取连续打卡天数 |

#### 管理接口（需要管理员权限）

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户列表 | GET | `/api/admin/users` | 获取所有用户 |
| 封禁用户 | PUT | `/api/admin/users/{id}/ban` | 封禁/解封用户 |
| 创建关卡 | POST | `/api/admin/levels` | 创建新关卡 |
| 创建练习 | POST | `/api/admin/exercises` | 创建练习内容 |
| 操作日志 | GET | `/api/admin/logs` | 查看操作日志 |

---

## 🎨 前端文档

### 环境要求

- **Node.js**: 18.x 或更高版本
- **npm**: 9.x 或更高版本（或使用 pnpm、yarn）

### 快速开始

#### 1. 进入前端目录

```bash
cd front
```

#### 2. 安装依赖

```bash
npm install
```

#### 3. 配置代理（开发环境）

编辑 `vite.config.ts`，配置后端代理：

```typescript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

#### 4. 启动开发服务器

```bash
npm run dev
```

前端默认运行在：`http://localhost:5173`

#### 5. 构建生产版本

```bash
npm run build
```

构建产物将输出到 `dist/` 目录。

### 功能模块

#### 1. 语音播报系统

**实现方式**：使用浏览器原生 Web Speech API

**功能特性**：
- ✅ 自动/手动播放模式
- ✅ 倒计时弹窗选择（5秒自动开启）
- ✅ 支持单词和短语朗读
- ✅ 可调节语速（默认 0.9 倍速）

**使用场景**：
- 关卡练习：进入关卡时弹出语音选择对话框
- 天梯挑战：开始挑战前弹出语音选择对话框
- 所有场景：练习过程中可随时切换自动/手动模式

#### 2. 打字引擎核心

**组件**：`TypingEngine.vue`

**功能**：
- 📝 实时字符比对（正确/错误/待输入）
- ⌨️ 键盘事件捕获（支持退格、Enter 跳过）
- 📊 首次输入准确率统计
- 🚫 禁止粘贴防作弊
- 🔄 支持多练习序列

#### 3. 字符显示组件

**组件**：`CharDisplay.vue`

**特点**：
- 🎨 空格字符特殊处理（底部线条显示）
- 📱 响应式布局（桌面端多行、移动端横向滚动）
- ✨ 动画效果（当前字符闪烁、正确/错误颜色区分）
- 🎯 等宽字体显示（Courier New）

#### 4. 键盘热力图

**组件**：`KeyboardDisplay.vue`

**功能**：
- 🎹 可视化显示按键熟练度
- 🌈 颜色分级（绿色优秀、黄色良好、红色需练习）
- 🔓 显示未解锁键位
- 📊 实时显示当前目标键位

#### 5. 状态管理

**使用 Pinia 管理全局状态**：

```typescript
// 示例：用户状态
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token'),
    user: null
  }),
  actions: {
    login(token, user) {
      this.token = token
      this.user = user
      localStorage.setItem('token', token)
    }
  }
})
```

### 页面说明

#### 1. 首页 (HomeView)

**路由**：`/`

**功能**：
- 🎯 展示系统特色（分级练习、精准统计、天梯挑战）
- 📊 显示用户个人统计卡片
- 📅 打卡日历可视化
- 💪 底部鼓励语横幅

**设计特点**：
- 渐变背景（蓝色 → 紫色 → 粉色）
- 浮动动画装饰元素
- 响应式卡片布局

#### 2. 关卡列表 (LevelListView)

**路由**：`/levels`

**功能**：
- 📚 按分类展示关卡
- 🔓 显示解锁状态
- 🏆 显示最佳成绩
- 📈 完成进度追踪

**交互**：
- 点击已解锁关卡进入练习
- 未解锁关卡显示锁定图标
- Tab 切换不同难度分类

#### 3. 关卡详情 (LevelDetailView)

**路由**：`/levels/:id`

**功能**：
- 📝 显示关卡信息（名称、描述、练习数量）
- 🎯 预览所有练习内容
- 🏆 显示历史最佳正确率
- 🚀 开始练习按钮

**设计特点**：
- 渐变背景（绿色 → 蓝绿色 → 蓝色）
- 练习内容卡片式展示
- 序号圆形徽章（渐变色）

#### 4. 关卡练习 (PracticeView)

**路由**：`/practice/:id`

**功能**：
- 📝 逐条显示练习内容
- 🔊 语音播报（自动/手动）
- 📊 实时正确率统计
- ⏭️ 跳过当前练习（Enter 键）
- 🎉 完成弹窗（正确率、用时、积分）
- 🔓 自动解锁下一关（正确率 ≥ 80%）

**交互流程**：
1. 进入关卡 → 显示语音选择弹窗（5秒倒计时）
2. 选择语音模式 → 开始练习
3. 打字过程中实时显示正确/错误状态
4. 完成所有练习 → 显示结果弹窗
5. 点击"重新练习"或"进入下一关"

#### 5. 天梯挑战 (ChallengeView)

**路由**：`/challenge`

**功能**：
- ⏱️ 选择时间模式（60秒/120秒/300秒/无限）
- 📝 选择内容类型（单词/短句）
- 🎮 实时打字挑战
- 📊 显示 WPM、正确率、通过数量
- 🏆 查看排行榜
- 📈 查看个人历史记录

**排行榜筛选**：
- 按内容类型筛选（单词/短句）
- 按时间模式筛选（60秒/120秒/300秒/无限）

#### 6. 指法训练 (FingerTypingView)

**路由**：`/finger-typing`

**功能**：
- ⌨️ 从 F、J 键开始，逐步解锁全键盘
- 🎹 键盘热力图显示
- 📊 WPM、准确率、时间统计
- 🔄 换一组练习
- ⚠️ 重置进度确认

**按键解锁规则**：
- 正确率达到要求后自动解锁下一组按键
- 解锁顺序：F、J → D、K → S、L → ... → 全键盘

#### 7. 个人资料 (ProfileView)

**路由**：`/profile`（自己的主页）  
**路由**：`/profile/:username`（其他用户主页）

**功能**：
- 👤 显示头像、昵称、简介
- 📊 统计卡片（练习时长、输入字符、完成关卡、总积分）
- 🔥 连续打卡天数
- 📝 各关卡历史最高正确率
- 📋 易错词汇 Top 10
- ✏️ 编辑资料（仅自己可见）

#### 8. 排行榜 (LeaderboardView)

**路由**：`/leaderboard`

**功能**：
- 🏆 普通排行榜（积分/打卡）
- ⚡ 天梯挑战排行榜
- 📊 筛选（类型、周期、分类）
- 👥 查看其他用户主页
- 🎯 显示我的排名

#### 9. 登录/注册/忘记密码

**路由**：
- `/login` - 登录
- `/register` - 注册
- `/forgot-password` - 忘记密码
- `/reset-password` - 重置密码

**设计特点**：
- 渐变背景
- 大图标 + 渐变色标题
- 表单验证提示
- 响应式布局

---

## 🗄️ 数据库初始化

### 1. 创建数据库

```sql
CREATE DATABASE english_typing 
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

### 2. 执行初始化脚本

```bash
mysql -u root -p english_typing < sql/init_database.sql
```

**主要表结构**：
- `user` - 用户表
- `category` - 关卡分类表
- `level` - 关卡表
- `exercise` - 练习内容表
- `user_level_progress` - 用户关卡进度表
- `typing_session` - 打字记录表
- `daily_checkin` - 每日打卡表
- `challenge_record` - 挑战记录表
- `password_reset_token` - 密码重置 Token 表
- `word_error_stat` - 单词错误统计表
- `admin_operation_log` - 管理员操作日志表

### 3. 导入初始数据

```bash
# 导入关卡分类和关卡数据
mysql -u root -p english_typing < sql/seed_levels.sql

# 导入练习内容（单词和短句）
mysql -u root -p english_typing < sql/seed_exercises.sql
```

---

## 🐳 Docker 部署

### 1. 启动基础设施

```bash
docker-compose up -d
```

这将启动：
- MySQL 8.0（端口 3306）
- Redis 7（端口 6379）

### 2. 查看日志

```bash
docker-compose logs -f mysql
docker-compose logs -f redis
```

### 3. 停止服务

```bash
docker-compose down
```

### 4. 清理数据卷（⚠️ 会删除所有数据）

```bash
docker-compose down -v
```

---

## 📐 开发规范

### 后端规范

1. **命名规范**
   - 类名：大驼峰（`UserController`）
   - 方法名：小驼峰（`getUserProfile`）
   - 常量：全大写 + 下划线（`JWT_EXPIRATION`）

2. **代码结构**
   - Controller 层：仅处理 HTTP 请求
   - Service 层：业务逻辑
   - Repository 层：数据库操作

3. **异常处理**
   - 使用 `@RestControllerAdvice` 全局异常处理
   - 统一返回格式：`{ code, message, data }`

4. **安全规范**
   - 密码使用 BCrypt 加密
   - JWT Token 有效期 24 小时
   - 敏感操作需要管理员权限（`@AdminOperation`）

### 前端规范

1. **命名规范**
   - 组件名：大驼峰（`TypingEngine.vue`）
   - 变量名：小驼峰（`currentExercise`）
   - 常量：全大写 + 下划线（`API_BASE_URL`）

2. **组件结构**
   ```vue
   <template>
     <!-- HTML 结构 -->
   </template>
   
   <script setup lang="ts">
   // TypeScript 代码
   </script>
   
   <style scoped>
   /* CSS 样式 */
   </style>
   ```

3. **类型安全**
   - 所有接口使用 TypeScript
   - 定义清晰的接口类型（`interface`）
   - 避免使用 `any` 类型

4. **样式规范**
   - 使用 TailwindCSS 原子类
   - 自定义样式使用 `<style scoped>`
   - 渐变色统一使用预定义类

---

## ❓ 常见问题

### 1. 后端启动失败

**问题**：`Cannot connect to database`

**解决方案**：
```bash
# 检查 MySQL 是否启动
docker-compose ps

# 检查数据库是否创建
docker-compose exec mysql mysql -u root -proot -e "SHOW DATABASES;"

# 重新初始化数据库
docker-compose down -v
docker-compose up -d
```

### 2. 前端无法连接后端

**问题**：`Network Error` 或 `403 Forbidden`

**解决方案**：
- 检查 `vite.config.ts` 中的代理配置
- 确认后端运行在 `http://localhost:8080`
- 清除浏览器缓存和 localStorage

### 3. 语音播报不工作

**问题**：点击播放按钮无声音

**解决方案**：
- 检查浏览器是否支持 Web Speech API
- 确认浏览器音量未静音
- 某些浏览器需要用户交互后才能播放语音

### 4. 空格显示异常

**问题**：短语练习中单词之间没有空格

**解决方案**：
- 已修复：更新 `CharDisplay.vue` 组件
- 空格字符使用底部线条显示，避免 padding 影响

### 5. 邮件发送失败

**问题**：忘记密码时无法收到邮件

**解决方案**：
- 检查 `application.yml` 中的邮件配置
- 确认 SMTP 服务器地址和端口正确
- 开发环境可使用 Mailhog 等工具模拟 SMTP

---

## 📝 更新日志

### v1.0.0 (2024-XX-XX)

**新增功能**：
- ✅ 用户注册/登录系统
- ✅ 分级关卡练习
- ✅ 天梯挑战系统
- ✅ 指法训练
- ✅ 语音播报
- ✅ 排行榜
- ✅ 打卡系统
- ✅ 管理后台
- ✅ 响应式 UI 美化

**技术改进**：
- 🎨 全面使用 TailwindCSS
- ✨ 添加浮动动画和渐变色
- 📊 优化统计图表
- 🔒 JWT 认证
- 📱 移动端适配

---

## 👥 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支（`git checkout -b feature/AmazingFeature`）
3. 提交更改（`git commit -m 'Add some AmazingFeature'`）
4. 推送到分支（`git push origin feature/AmazingFeature`）
5. 开启 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。


---

⭐ 如果这个项目对你有帮助，请给一个 Star！
