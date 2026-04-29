<template>
  <div class="flex h-screen bg-gray-50">
    <!-- 侧边栏 -->
    <aside :class="['bg-gray-900 text-white transition-all duration-300 flex-shrink-0', sidebarOpen ? 'w-64' : 'w-16']">
      <div class="flex flex-col h-full">
        <!-- Logo -->
        <div class="h-16 flex items-center justify-between px-4 border-b border-gray-800">
          <router-link to="/admin" class="flex items-center gap-2">
            <div class="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center text-white font-bold text-sm">A</div>
            <span v-if="sidebarOpen" class="font-bold text-lg">管理后台</span>
          </router-link>
          <button @click="sidebarOpen = !sidebarOpen" class="p-1 hover:bg-gray-800 rounded transition lg:hidden">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>

        <!-- 导航菜单 -->
        <nav class="flex-1 px-2 py-4 space-y-1 overflow-y-auto">
          <router-link
            v-for="item in menuItems"
            :key="item.to"
            :to="item.to"
            :class="['flex items-center gap-3 px-3 py-2.5 rounded-lg transition', isActive(item.to) ? 'bg-blue-600 text-white' : 'text-gray-300 hover:bg-gray-800 hover:text-white']"
          >
            <span class="text-xl flex-shrink-0">{{ item.icon }}</span>
            <span v-if="sidebarOpen" class="text-sm font-medium">{{ item.label }}</span>
          </router-link>
        </nav>

        <!-- 底部：返回用户页 -->
        <div class="p-2 border-t border-gray-800">
          <router-link to="/" class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-gray-300 hover:bg-gray-800 hover:text-white transition">
            <span class="text-xl flex-shrink-0">🏠</span>
            <span v-if="sidebarOpen" class="text-sm font-medium">返回用户页</span>
          </router-link>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- 顶部导航栏 -->
      <header class="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 flex-shrink-0">
        <button @click="sidebarOpen = !sidebarOpen" class="p-2 hover:bg-gray-100 rounded-lg transition lg:hidden">
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
        <div class="flex items-center gap-4 ml-auto">
          <span class="text-sm text-gray-600">欢迎，<strong>{{ authStore.userInfo?.username }}</strong></span>
          <button @click="handleLogout" class="text-sm text-gray-500 hover:text-red-500 transition px-3 py-1.5 rounded-lg hover:bg-red-50">
            退出登录
          </button>
        </div>
      </header>

      <!-- 页面内容 -->
      <main class="flex-1 overflow-y-auto">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { logout } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const sidebarOpen = ref(true)

const menuItems = [
  { to: '/admin', label: '仪表盘', icon: '📊' },
  { to: '/admin/users', label: '用户管理', icon: '👥' },
  { to: '/admin/categories', label: '分类管理', icon: '🏷️' },
  { to: '/admin/levels', label: '关卡管理', icon: '📚' },
  { to: '/admin/exercises', label: '练习内容', icon: '✏️' },
  { to: '/admin/gifts', label: '道具管理', icon: '🎁' },
  { to: '/admin/points', label: '积分记录', icon: '💰' },
  { to: '/admin/logs', label: '操作日志', icon: '📝' },
]

const isActive = (path: string) => {
  if (path === '/admin') return route.path === '/admin'
  return route.path.startsWith(path)
}

const handleLogout = async () => {
  try {
    await logout()
  } catch (e) {
    console.error(e)
  } finally {
    authStore.logout()
    router.push('/login')
  }
}
</script>