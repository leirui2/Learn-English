# 前端认证系统说明

## 已完成功能

### 1. 用户认证页面
- ✅ 登录页面 (`/login`)
- ✅ 注册页面 (`/register`)
- ✅ 忘记密码申请页面 (`/forgot-password`)
- ✅ 密码重置页面 (`/reset-password?token=xxx`)

### 2. 管理员系统
- ✅ 管理员仪表板 (`/admin`)
- ✅ 用户管理页面 (`/admin/users`) - 占位符
- ✅ 分类管理页面 (`/admin/categories`) - 占位符
- ✅ 关卡管理页面 (`/admin/levels`) - 占位符
- ✅ 练习内容管理页面 (`/admin/exercises`) - 占位符
- ✅ 操作日志页面 (`/admin/logs`) - 占位符

### 3. 自动跳转逻辑
- ✅ 登录成功后根据用户角色自动跳转
  - 管理员 (role='ADMIN') → `/admin`
  - 普通用户 (role='USER') → `/` 或 redirect 参数指定的页面
- ✅ 已登录用户访问登录页自动跳转到对应首页
- ✅ 未登录用户访问需要认证的页面自动跳转到登录页
- ✅ 普通用户访问管理员页面自动跳转到主页

## 技术实现

### 路由守卫
```typescript
// front/src/router/index.ts
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const requiresAuth = to.meta.requiresAuth
  const requiresAdmin = to.meta.requiresAdmin

  // 检查登录状态
  if (requiresAuth && !authStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 检查管理员权限
  if (requiresAdmin && !authStore.isAdmin) {
    next({ name: 'Home' })
    return
  }

  // 已登录用户访问登录页自动跳转
  if (to.name === 'Login' && authStore.isLoggedIn) {
    if (authStore.isAdmin) {
      next({ name: 'Admin' })
    } else {
      next({ name: 'Home' })
    }
    return
  }

  next()
})
```

### 认证 Store
```typescript
// front/src/stores/auth.ts
export const useAuthStore = defineStore('auth', () => {
  const userInfo = ref<UserInfo | null>(...)
  
  // 是否已登录
  const isLoggedIn = computed(() => !!accessToken.value)
  
  // 是否是管理员
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')
  
  // ...
})
```

### API 拦截器
```typescript
// front/src/api/auth.ts
// 请求拦截器：自动添加 token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('access_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：自动刷新 token
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401 && !originalRequest._retry) {
      // 尝试刷新 token
      const refreshToken = localStorage.getItem('refresh_token')
      if (refreshToken) {
        const response = await axios.post('/auth/refresh', { refreshToken })
        // 重试原请求
      }
    }
    return Promise.reject(error)
  }
)
```

## 使用方法

### 1. 安装依赖
```bash
cd front
npm install
```

### 2. 配置环境变量
编辑 `.env.development` 文件，设置后端 API 地址：
```
VITE_API_BASE_URL=http://localhost:8080
```

### 3. 启动开发服务器
```bash
npm run dev
```

### 4. 测试账号
- 普通用户：注册新账号
- 管理员：使用后端初始化的超级管理员账号
  - 用户名：`admin`
  - 邮箱：`admin@example.com`
  - 密码：`Admin@123456`

## 页面路由

### 公开页面（无需登录）
- `/login` - 登录页
- `/register` - 注册页
- `/forgot-password` - 忘记密码申请页
- `/reset-password?token=xxx` - 密码重置页

### 用户页面（需要登录）
- `/` - 用户主页
- `/levels` - 关卡列表

### 管理员页面（需要管理员权限）
- `/admin` - 管理员仪表板
- `/admin/users` - 用户管理
- `/admin/categories` - 分类管理
- `/admin/levels` - 关卡管理
- `/admin/exercises` - 练习内容管理
- `/admin/logs` - 操作日志

## 表单验证

### 登录表单
- 邮箱：必填，格式验证
- 密码：必填

### 注册表单
- 用户名：必填，至少 2 个字符
- 邮箱：必填，格式验证
- 密码：必填，至少 8 个字符
- 确认密码：必填，与密码一致

### 密码重置表单
- 新密码：必填，至少 8 个字符
- 确认密码：必填，与新密码一致

## 响应式设计
所有页面都采用响应式设计，适配：
- 移动端（< 768px）：全宽布局
- 平板（≥ 768px）：居中卡片布局
- 桌面（≥ 1024px）：居中卡片布局

## 后续开发建议

### 管理员页面功能
1. **用户管理**
   - 用户列表（分页、搜索、筛选）
   - 封禁/解封用户
   - 删除用户
   - 重置用户密码
   - 修改用户角色

2. **分类管理**
   - 分类列表
   - 创建/编辑/删除分类

3. **关卡管理**
   - 关卡列表（按分类分组）
   - 创建/编辑/删除关卡
   - 关卡排序

4. **练习内容管理**
   - 练习内容列表（按关卡筛选）
   - 创建/编辑/删除练习内容
   - 批量导入功能

5. **操作日志**
   - 日志列表（分页、筛选）
   - 操作详情查看

### 用户页面功能
1. 主页（打卡日历、统计概览）
2. 关卡列表（闯关模式）
3. 打字练习页面
4. 个人统计页面
5. 排行榜页面

## 注意事项

1. **Token 管理**
   - Access Token 有效期：2 小时
   - Refresh Token 有效期：7 天
   - Token 自动刷新机制已实现

2. **权限控制**
   - 路由级别权限控制
   - API 级别权限控制（后端）
   - 前端仅做 UI 控制，真正的权限验证在后端

3. **错误处理**
   - 网络错误提示
   - 表单验证错误提示
   - API 错误提示

4. **安全性**
   - 密码不在前端明文存储
   - Token 存储在 localStorage
   - 敏感操作需要二次确认
