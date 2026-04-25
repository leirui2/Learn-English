# 英语打字学习 - 前端项目

基于 Vue 3 + TypeScript + Vite 构建的英语打字学习平台前端。

## 技术栈

- **框架**: Vue 3.4 (Composition API)
- **语言**: TypeScript 5.4
- **构建工具**: Vite 5.2
- **状态管理**: Pinia 2.1
- **路由**: Vue Router 4.3
- **样式**: Tailwind CSS 3.4
- **HTTP 客户端**: Axios 1.6
- **测试**:
  - 单元测试: Vitest 1.5 + @vue/test-utils 2.4
  - 属性测试: fast-check 3.17
  - E2E 测试: Playwright 1.43

## 项目结构

```
front/
├── src/
│   ├── api/          # API 请求封装
│   ├── assets/       # 静态资源（图片、字体等）
│   ├── components/   # 可复用组件
│   ├── layouts/      # 布局组件（AuthLayout、MainLayout）
│   ├── router/       # 路由配置
│   ├── stores/       # Pinia 状态管理
│   ├── types/        # TypeScript 类型定义
│   ├── utils/        # 工具函数
│   ├── views/        # 页面组件
│   ├── App.vue       # 根组件
│   ├── main.ts       # 应用入口
│   └── style.css     # 全局样式（Tailwind 指令）
├── index.html        # HTML 入口
├── vite.config.ts    # Vite 配置
├── tsconfig.json     # TypeScript 配置
└── package.json      # 依赖管理
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 运行测试

```bash
# 单元测试（单次运行）
npm run test

# 单元测试（监听模式）
npm run test:watch

# E2E 测试
npm run test:e2e
```

## 响应式断点

使用 Tailwind CSS 默认断点：

- **默认**（无前缀）: < 768px → 手机竖屏
- **md**: ≥ 768px → 平板 / 手机横屏
- **lg**: ≥ 1024px → 桌面

## API 代理配置

开发环境下，`/api` 开头的请求会自动代理到后端服务 `http://localhost:8085`。

配置位于 `vite.config.ts` 的 `server.proxy` 字段。

## 路由守卫

所有需要登录的路由（`meta.requiresAuth: true`）会在用户未登录时自动跳转到登录页。

认证状态由 `useAuthStore` 管理，token 持久化到 localStorage。
