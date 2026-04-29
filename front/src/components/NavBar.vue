<template>
  <nav class="bg-white border-b border-gray-200 sticky top-0 z-30">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">

        <!-- Logo -->
        <router-link to="/" class="flex items-center gap-2 flex-shrink-0">
          <div class="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center text-white font-bold text-sm">E</div>
          <span class="font-bold text-gray-900 text-lg hidden sm:block">英语打字</span>
        </router-link>

        <!-- 桌面端导航链接 -->
        <div class="hidden md:flex items-center gap-1">
          <router-link
            v-for="item in navItems"
            :key="item.to"
            :to="item.to"
            class="flex items-center gap-1.5 px-3 py-2 rounded-lg text-sm font-medium transition"
            :class="[
              isActive(item.to) ? 'bg-blue-50 text-blue-600' : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900',
              (item as any).desktopOnly ? 'hidden lg:flex' : ''
            ]"
          >
            <span>{{ item.icon }}</span>
            <span>{{ item.label }}</span>
          </router-link>
        </div>

        <!-- 右侧：用户信息 + 退出 -->
        <div class="flex items-center gap-3">
          <!-- 桌面端用户头像 + 下拉 -->
          <div class="hidden md:flex items-center gap-3">
            <router-link to="/profile" class="flex items-center gap-2 px-3 py-1.5 rounded-lg hover:bg-gray-100 transition">
              <div class="w-7 h-7 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-white text-xs font-bold">
                {{ displayName.charAt(0).toUpperCase() }}
              </div>
              <span class="text-sm font-medium text-gray-700">{{ displayName }}</span>
            </router-link>
            <button
              @click="handleLogout"
              class="text-sm text-gray-500 hover:text-red-500 transition px-2 py-1.5 rounded-lg hover:bg-red-50"
            >
              退出
            </button>
          </div>

          <!-- 移动端汉堡按钮 -->
          <button
            @click="mobileOpen = !mobileOpen"
            class="md:hidden p-2 rounded-lg text-gray-500 hover:bg-gray-100 transition"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path v-if="!mobileOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
              <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- 移动端展开菜单 -->
    <Transition name="slide">
      <div v-if="mobileOpen" class="md:hidden border-t border-gray-100 bg-white">
        <!-- 用户信息 -->
        <div class="flex items-center gap-3 px-4 py-3 border-b border-gray-100">
          <div class="w-10 h-10 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-white font-bold">
            {{ displayName.charAt(0).toUpperCase() }}
          </div>
          <div>
            <div class="text-sm font-semibold text-gray-900">{{ displayName }}</div>
            <div class="text-xs text-gray-500">{{ authStore.userInfo?.email }}</div>
          </div>
        </div>

        <!-- 导航链接 -->
        <div class="py-2 px-2">
          <router-link
            v-for="item in navItems"
            :key="item.to"
            :to="item.to"
            @click="mobileOpen = false"
            class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition mb-0.5"
            :class="isActive(item.to)
              ? 'bg-blue-50 text-blue-600'
              : 'text-gray-700 hover:bg-gray-100'"
          >
            <span class="text-base">{{ item.icon }}</span>
            <span>{{ item.label }}</span>
          </router-link>
        </div>

        <!-- 退出登录 -->
        <div class="px-2 pb-3 border-t border-gray-100 pt-2">
          <button
            @click="handleLogout"
            class="flex items-center gap-3 w-full px-3 py-2.5 rounded-lg text-sm font-medium text-red-500 hover:bg-red-50 transition"
          >
            <span class="text-base">🚪</span>
            <span>退出登录</span>
          </button>
        </div>
      </div>
    </Transition>
  </nav>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { logout } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const mobileOpen = ref(false)

const navItems = [
  { to: '/', label: '主页', icon: '🏠' },
  { to: '/levels', label: '关卡', icon: '📚' },
  { to: '/challenge', label: '天梯挑战', icon: '⚡' },
  { to: '/finger-typing', label: '指法训练', icon: '⌨️', desktopOnly: true },
  { to: '/leaderboard', label: '排行榜', icon: '🏆' },
  { to: '/gifts', label: '积分商城', icon: '🎁' },
  { to: '/profile', label: '个人中心', icon: '👤' },
]

const displayName = computed(() => authStore.userInfo?.username || '用户')

const isActive = (path: string) => {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}

const handleLogout = async () => {
  mobileOpen.value = false
  try {
    if (authStore.refreshToken) await logout()
  } catch (e) {
    console.error('退出失败:', e)
  } finally {
    authStore.logout()
    await router.push('/login')
  }
}
</script>

<style scoped>
.slide-enter-active,
.slide-leave-active {
  transition: all 0.2s ease;
}
.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>