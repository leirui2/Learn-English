<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">📋 兑换记录</h1>
        <p class="mt-2 text-gray-600">查看道具兑换历史</p>
      </div>

      <!-- 导航标签 -->
      <div class="flex gap-4 mb-6">
        <router-link
          to="/gifts"
          class="px-4 py-2 bg-white rounded-lg text-gray-600 hover:bg-gray-100 transition-colors"
        >
          🎁 积分商城
        </router-link>
        <router-link
          to="/my-items"
          class="px-4 py-2 bg-white rounded-lg text-gray-600 hover:bg-gray-100 transition-colors"
        >
          🎒 我的背包
        </router-link>
        <router-link
          to="/exchange-records"
          class="px-4 py-2 bg-purple-500 text-white rounded-lg"
        >
          📋 兑换记录
        </router-link>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-500"></div>
      </div>

      <!-- 记录列表 -->
      <div v-else-if="records.length > 0" class="space-y-4">
        <div
          v-for="record in records"
          :key="record.id"
          class="bg-white rounded-xl shadow-sm p-4 flex items-center gap-4"
        >
          <!-- 道具图标 -->
          <div class="w-14 h-14 bg-purple-100 rounded-lg flex items-center justify-center flex-shrink-0 overflow-hidden">
            <img
              :src="record.giftIcon"
              :alt="record.giftName"
              class="w-full h-full object-contain"
              @error="handleImageError"
            />
          </div>

          <!-- 记录信息 -->
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2">
              <h3 class="font-bold text-gray-900">{{ record.giftName }}</h3>
              <span
                class="px-2 py-0.5 text-xs rounded-full"
                :class="record.status === 'UNUSED' ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-500'"
              >
                {{ record.status === 'UNUSED' ? '未使用' : '已使用' }}
              </span>
            </div>
            <p class="text-sm text-gray-500 mt-1">
              兑换时间: {{ formatDate(record.redeemedAt) }}
            </p>
          </div>

          <!-- 查看详情 -->
          <router-link
            to="/my-items"
            class="text-purple-500 hover:text-purple-600 text-sm"
          >
            查看背包 →
          </router-link>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="text-center py-12 bg-white rounded-xl">
        <span class="text-6xl">📭</span>
        <p class="text-gray-500 mt-4">暂无兑换记录</p>
        <router-link
          to="/gifts"
          class="inline-block mt-4 px-6 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors"
        >
          去商城逛逛
        </router-link>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="mt-8 flex justify-center gap-2">
        <button
          v-for="p in totalPages"
          :key="p"
          class="px-4 py-2 rounded-lg transition-colors"
          :class="p - 1 === page ? 'bg-purple-500 text-white' : 'bg-white text-gray-700 hover:bg-gray-100'"
          @click="loadRecords(p - 1)"
        >
          {{ p }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getExchangeRecords, type UserItemDto } from '../api/gift'

// 记录列表
const records = ref<UserItemDto[]>([])
const loading = ref(true)
const page = ref(0)
const totalPages = ref(0)

// 格式化日期
const formatDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 图片加载失败处理
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="56" height="56"%3E%3Crect width="56" height="56" fill="%23E5E7EB"/%3E%3Ctext x="50%25" y="50%25" dominant-baseline="middle" text-anchor="middle" font-size="28"%3E🎁%3C/text%3E%3C/svg%3E'
}

// 加载记录
const loadRecords = async (p: number = 0) => {
  loading.value = true
  try {
    const res = await getExchangeRecords(p, 20)
    if (res.data.code === 200) {
      records.value = res.data.data.content
      totalPages.value = res.data.data.totalPages
      page.value = res.data.data.number
    }
  } catch (error) {
    console.error('加载兑换记录失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRecords()
})
</script>