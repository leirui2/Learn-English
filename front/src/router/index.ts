import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 路由配置
const routes: RouteRecordRaw[] = [
  // 认证相关路由（使用 AuthLayout）
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { layout: 'auth', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { layout: 'auth', requiresAuth: false }
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('@/views/ForgotPasswordView.vue'),
    meta: { layout: 'auth', requiresAuth: false }
  },
  {
    path: '/reset-password',
    name: 'ResetPassword',
    component: () => import('@/views/ResetPasswordView.vue'),
    meta: { layout: 'auth', requiresAuth: false }
  },

  // 主应用路由（使用 MainLayout，需要登录）
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/levels',
    name: 'Levels',
    component: () => import('@/views/LevelListView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/levels/:id',
    name: 'LevelDetail',
    component: () => import('@/views/LevelDetailView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/practice/:id',
    name: 'Practice',
    component: () => import('@/views/PracticeView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/leaderboard',
    name: 'Leaderboard',
    component: () => import('@/views/LeaderboardView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/finger-typing',
    name: 'FingerTyping',
    component: () => import('@/views/FingerTypingView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/challenge',
    name: 'Challenge',
    component: () => import('@/views/ChallengeView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/ProfileView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },
  {
    path: '/profile/:userId',
    name: 'UserProfile',
    component: () => import('@/views/ProfileView.vue'),
    meta: { layout: 'main', requiresAuth: true }
  },

  // 管理员路由（需要管理员权限，不使用布局因为页面自带导航）
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/AdminDashboard.vue'),
    meta: { layout: 'admin', requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('@/views/admin/UserManagement.vue'),
    meta: { layout: 'admin', requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/categories',
    name: 'AdminCategories',
    component: () => import('@/views/admin/CategoryManagement.vue'),
    meta: { layout: 'admin', requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/levels',
    name: 'AdminLevels',
    component: () => import('@/views/admin/LevelManagement.vue'),
    meta: { layout: 'admin', requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/exercises',
    name: 'AdminExercises',
    component: () => import('@/views/admin/ExerciseManagement.vue'),
    meta: { layout: 'admin', requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/logs',
    name: 'AdminLogs',
    component: () => import('@/views/admin/OperationLogs.vue'),
    meta: { layout: 'admin', requiresAuth: true, requiresAdmin: true }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 全局路由守卫：检查登录状态和管理员权限
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  const requiresAuth = to.meta.requiresAuth
  const requiresAdmin = to.meta.requiresAdmin

  // 如果路由需要登录且用户未登录，跳转到登录页
  if (requiresAuth && !authStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 如果路由需要管理员权限但用户不是管理员，跳转到主页
  if (requiresAdmin && !authStore.isAdmin) {
    next({ name: 'Home' })
    return
  }

  // 如果用户已登录且访问登录页，根据角色自动跳转
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

export default router
