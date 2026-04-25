<template>
  <div class="p-6">
    <!-- 欢迎信息 -->
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900">仪表盘</h1>
      <p class="text-gray-500 mt-1">欢迎回来，{{ authStore.userInfo?.username }}</p>
    </div>

    <!-- 功能卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      <router-link
        v-for="item in cards"
        :key="item.to"
        :to="item.to"
        class="bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition p-6 block"
      >
        <div class="flex items-center gap-4 mb-3">
          <div :class="['w-12 h-12 rounded-xl flex items-center justify-center text-2xl', item.bg]">
            {{ item.icon }}
          </div>
          <h3 class="text-base font-semibold text-gray-900">{{ item.label }}</h3>
        </div>
        <p class="text-sm text-gray-500">{{ item.desc }}</p>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const cards = [
  { to: '/admin/users', label: '用户管理', icon: '👥', bg: 'bg-blue-50', desc: '管理用户账号、权限和状态' },
  { to: '/admin/categories', label: '分类管理', icon: '🏷️', bg: 'bg-green-50', desc: '管理难度分类和分类信息' },
  { to: '/admin/levels', label: '关卡管理', icon: '📚', bg: 'bg-purple-50', desc: '管理各分类下的关卡' },
  { to: '/admin/exercises', label: '练习内容', icon: '✏️', bg: 'bg-yellow-50', desc: '管理单词和短句练习内容' },
  { to: '/admin/logs', label: '操作日志', icon: '📝', bg: 'bg-red-50', desc: '查看管理员操作记录' },
  { to: '/', label: '返回用户页', icon: '🏠', bg: 'bg-gray-50', desc: '切换到普通用户视图' },
]
</script>
