<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 via-purple-50 to-pink-50">
    <!-- 顶部装饰 -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-purple-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-yellow-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-2000"></div>
      <div class="absolute top-1/2 left-1/2 w-80 h-80 bg-pink-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-4000"></div>
    </div>

    <div class="max-w-7xl mx-auto px-4 py-12 relative">
      <!-- Hero 区域 -->
      <div class="text-center mb-16">
        <div class="inline-block mb-6">
          <div class="text-7xl mb-4 animate-bounce">⌨️</div>
        </div>
        <h1 class="text-5xl md:text-6xl font-bold bg-gradient-to-r from-blue-600 via-purple-600 to-pink-600 bg-clip-text text-transparent mb-6">
          欢迎来到英语打字学习平台
        </h1>
        <p class="text-xl text-gray-600 mb-10 max-w-2xl mx-auto leading-relaxed">
          通过打字练习提升你的英语水平，在练习中进步，在挑战中成长 🚀
        </p>
        
        <div class="flex flex-col sm:flex-row justify-center gap-4">
          <router-link
            v-if="authStore.isLoggedIn"
            to="/levels"
            class="px-8 py-4 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold text-lg rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2"
          >
            <span>开始练习</span>
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </router-link>
          <router-link
            v-else
            to="/finger-typing"
            class="px-8 py-4 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold text-lg rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2"
          >
            <span>⌨️</span>
            <span>免费体验指法训练</span>
          </router-link>
          <router-link
            to="/leaderboard"
            class="px-8 py-4 bg-white hover:bg-gray-50 text-gray-700 font-bold text-lg rounded-xl border-2 border-gray-200 hover:border-purple-300 transition shadow-md hover:shadow-lg transform hover:scale-105 flex items-center justify-center gap-2"
          >
            <span>🏆</span>
            <span>查看排行榜</span>
          </router-link>
          <router-link
            v-if="authStore.isLoggedIn"
            to="/challenge"
            class="px-8 py-4 bg-gradient-to-r from-orange-500 to-red-500 hover:from-orange-600 hover:to-red-600 text-white font-bold text-lg rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2"
          >
            <span>⚡</span>
            <span>天梯挑战</span>
          </router-link>
          <router-link
            v-else
            to="/login"
            class="px-8 py-4 bg-gradient-to-r from-orange-500 to-red-500 hover:from-orange-600 hover:to-red-600 text-white font-bold text-lg rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2"
          >
            <span>🚀</span>
            <span>登录开始挑战</span>
          </router-link>
        </div>
      </div>

      <!-- 功能特色 -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
        <div class="bg-white rounded-2xl shadow-lg p-8 hover:shadow-xl transition transform hover:-translate-y-2 border border-gray-100">
          <div class="text-5xl mb-4">📚</div>
          <h3 class="text-xl font-bold text-gray-900 mb-3">分级练习</h3>
          <p class="text-gray-600">从简单单词到复杂短句，循序渐进提升打字速度</p>
        </div>
        <div class="bg-white rounded-2xl shadow-lg p-8 hover:shadow-xl transition transform hover:-translate-y-2 border border-gray-100">
          <div class="text-5xl mb-4">🎯</div>
          <h3 class="text-xl font-bold text-gray-900 mb-3">精准统计</h3>
          <p class="text-gray-600">实时追踪正确率、速度和进度，数据驱动进步</p>
        </div>
        <div class="bg-white rounded-2xl shadow-lg p-8 hover:shadow-xl transition transform hover:-translate-y-2 border border-gray-100">
          <div class="text-5xl mb-4">🏅</div>
          <h3 class="text-xl font-bold text-gray-900 mb-3">天梯挑战</h3>
          <p class="text-gray-600">参与限时挑战，冲击排行榜，证明你的实力</p>
        </div>
      </div>

      <!-- 用户统计卡片（仅登录用户显示） -->
      <div v-if="authStore.isLoggedIn">
        <div v-if="loading" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
          <p class="mt-4 text-gray-500">加载中...</p>
        </div>
        <div v-else class="mb-12">
          <h2 class="text-3xl font-bold text-center text-gray-900 mb-8">📊 我的学习数据</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="bg-gradient-to-br from-blue-500 to-blue-600 rounded-2xl shadow-lg p-8 text-white hover:shadow-xl transition transform hover:scale-105">
              <div class="flex items-center justify-between mb-4">
                <div class="text-4xl">⭐</div>
                <div class="text-5xl font-bold">{{ stats?.totalScore || 0 }}</div>
              </div>
              <div class="text-lg font-medium opacity-90">总积分</div>
            </div>
            <div class="bg-gradient-to-br from-green-500 to-green-600 rounded-2xl shadow-lg p-8 text-white hover:shadow-xl transition transform hover:scale-105">
              <div class="flex items-center justify-between mb-4">
                <div class="text-4xl">🔥</div>
                <div class="text-5xl font-bold">{{ stats?.streak || 0 }}</div>
              </div>
              <div class="text-lg font-medium opacity-90">连续打卡天数</div>
            </div>
            <div class="bg-gradient-to-br from-purple-500 to-purple-600 rounded-2xl shadow-lg p-8 text-white hover:shadow-xl transition transform hover:scale-105">
              <div class="flex items-center justify-between mb-4">
                <div class="text-4xl">✅</div>
                <div class="text-5xl font-bold">{{ stats?.totalCompletedLevels || 0 }}</div>
              </div>
              <div class="text-lg font-medium opacity-90">完成关卡数</div>
            </div>
          </div>
        </div>

        <!-- 打卡日历 -->
        <div v-if="!loading" class="mb-12">
          <h2 class="text-3xl font-bold text-center text-gray-900 mb-8">📅 打卡日历</h2>
          <div class="bg-white rounded-2xl shadow-lg p-8 border border-gray-100">
            <div class="grid grid-cols-7 gap-3 max-w-3xl mx-auto mb-4">
              <div v-for="day in ['日', '一', '二', '三', '四', '五', '六']" :key="day" class="text-center text-sm font-medium text-gray-500 py-2">
                {{ day }}
              </div>
            </div>
            <div class="grid grid-cols-7 gap-3 max-w-3xl mx-auto">
              <div
                v-for="day in calendarDays"
                :key="day.date"
                :class="[
                  'aspect-square rounded-xl flex items-center justify-center text-sm font-medium transition transform hover:scale-110',
                  day.isCheckedIn
                    ? 'bg-gradient-to-br from-green-400 to-green-600 text-white shadow-md'
                    : 'bg-gray-100 text-gray-400 hover:bg-gray-200'
                ]"
                :title="day.date + (day.isCheckedIn ? ' ✓ 已打卡' : '')"
              >
                {{ day.dayOfMonth }}
              </div>
            </div>
            <div class="mt-6 flex justify-center items-center gap-6 text-sm">
              <div class="flex items-center gap-2">
                <div class="w-4 h-4 rounded bg-gradient-to-br from-green-400 to-green-600"></div>
                <span class="text-gray-600">已打卡</span>
              </div>
              <div class="flex items-center gap-2">
                <div class="w-4 h-4 rounded bg-gray-100"></div>
                <span class="text-gray-600">未打卡</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 未登录用户的引导区域 -->
      <div v-else class="mb-12">
        <h2 class="text-3xl font-bold text-center text-gray-900 mb-8">🎯 为什么选择我们？</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div class="bg-white rounded-2xl shadow-lg p-8 border border-gray-100">
            <div class="text-5xl mb-4">🆓</div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">免费体验</h3>
            <p class="text-gray-600 mb-4">无需注册即可体验指法训练，感受我们的教学质量</p>
            <router-link
              to="/finger-typing"
              class="inline-flex items-center gap-2 text-blue-600 hover:text-blue-700 font-medium"
            >
              立即体验 →
            </router-link>
          </div>
          <div class="bg-white rounded-2xl shadow-lg p-8 border border-gray-100">
            <div class="text-5xl mb-4">📈</div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">进步可见</h3>
            <p class="text-gray-600 mb-4">注册后解锁完整功能，追踪学习进度，参与排行榜竞争</p>
            <router-link
              to="/register"
              class="inline-flex items-center gap-2 text-blue-600 hover:text-blue-700 font-medium"
            >
              免费注册 →
            </router-link>
          </div>
        </div>
      </div>

      <!-- 底部鼓励语 -->
      <div class="text-center py-8">
        <div class="bg-gradient-to-r from-purple-500 to-pink-500 rounded-2xl shadow-lg p-8 text-white">
          <div class="text-5xl mb-4">💪</div>
          <h3 class="text-2xl font-bold mb-3">每一天都是新的起点</h3>
          <p class="text-lg opacity-90">坚持练习，你会看到自己的进步！</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { getUserStats } from '@/api/stats'
import { getCheckinCalendar } from '@/api/checkin'
import type { UserStatsResponse } from '@/api/stats'
import type { CheckinCalendarResponse } from '@/api/checkin'

const authStore = useAuthStore()
const loading = ref(true)
const stats = ref<UserStatsResponse | null>(null)
const calendar = ref<CheckinCalendarResponse | null>(null)

interface CalendarDay {
  date: string
  dayOfMonth: number
  isCheckedIn: boolean
}

const calendarDays = computed<CalendarDay[]>(() => {
  if (!calendar.value) return []

  const checkinSet = new Set(calendar.value.checkinDates)
  const days: CalendarDay[] = []
  const today = new Date()

  // 生成过去 30 天的日期
  for (let i = 29; i >= 0; i--) {
    const date = new Date(today)
    date.setDate(date.getDate() - i)
    const dateStr = date.toISOString().split('T')[0]
    
    days.push({
      date: dateStr,
      dayOfMonth: date.getDate(),
      isCheckedIn: checkinSet.has(dateStr)
    })
  }

  return days
})

const loadData = async () => {
  try {
    loading.value = true
    
    // 只有在登录时才加载用户数据
    if (authStore.isLoggedIn) {
      const [statsRes, calendarRes] = await Promise.all([
        getUserStats(),
        getCheckinCalendar()
      ])
      stats.value = statsRes.data
      calendar.value = calendarRes.data
    } else {
      // 未登录时设置为null
      stats.value = null
      calendar.value = null
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
@keyframes blob {
  0% {
    transform: translate(0px, 0px) scale(1);
  }
  33% {
    transform: translate(30px, -50px) scale(1.1);
  }
  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }
  100% {
    transform: translate(0px, 0px) scale(1);
  }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}

.animation-delay-4000 {
  animation-delay: 4s;
}
</style>
