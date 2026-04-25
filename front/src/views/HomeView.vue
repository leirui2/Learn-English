<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <div class="text-center">
      <h1 class="text-4xl font-bold text-gray-900 mb-4">
        欢迎来到英语打字学习平台
      </h1>
      <p class="text-xl text-gray-600 mb-8">
        通过打字练习提升你的英语水平
      </p>
      
      <div class="flex justify-center gap-4">
        <router-link
          to="/levels"
          class="px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition shadow-md hover:shadow-lg"
        >
          开始练习
        </router-link>
        <router-link
          to="/leaderboard"
          class="px-6 py-3 bg-white hover:bg-gray-50 text-gray-700 font-medium rounded-lg border border-gray-300 transition shadow-sm hover:shadow"
        >
          查看排行榜
        </router-link>
      </div>

      <!-- 用户统计卡片 -->
      <div v-if="loading" class="mt-12 text-gray-500">
        加载中...
      </div>
      <div v-else class="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="text-3xl font-bold text-blue-600 mb-2">
            {{ stats?.totalScore || 0 }}
          </div>
          <div class="text-sm text-gray-600">总积分</div>
        </div>
        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="text-3xl font-bold text-green-600 mb-2">
            {{ stats?.streak || 0 }}
          </div>
          <div class="text-sm text-gray-600">连续打卡天数</div>
        </div>
        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="text-3xl font-bold text-purple-600 mb-2">
            {{ stats?.totalCompletedLevels || 0 }}
          </div>
          <div class="text-sm text-gray-600">完成关卡数</div>
        </div>
      </div>

      <!-- 打卡日历 -->
      <div v-if="!loading" class="mt-12">
        <h2 class="text-2xl font-bold text-gray-900 mb-6">打卡日历</h2>
        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="grid grid-cols-7 gap-2 max-w-2xl mx-auto">
            <div
              v-for="day in calendarDays"
              :key="day.date"
              :class="[
                'aspect-square rounded-lg flex items-center justify-center text-sm',
                day.isCheckedIn
                  ? 'bg-green-500 text-white font-semibold'
                  : 'bg-gray-100 text-gray-400'
              ]"
              :title="day.date"
            >
              {{ day.dayOfMonth }}
            </div>
          </div>
          <div class="mt-4 text-sm text-gray-600 text-center">
            过去 30 天打卡记录
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { getUserStats } from '@/api/stats'
import { getCheckinCalendar } from '@/api/checkin'
import type { UserStatsResponse } from '@/api/stats'
import type { CheckinCalendarResponse } from '@/api/checkin'

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
    const [statsRes, calendarRes] = await Promise.all([
      getUserStats(),
      getCheckinCalendar()
    ])
    stats.value = statsRes.data
    calendar.value = calendarRes.data
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
