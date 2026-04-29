<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">🎒 我的背包</h1>
        <p class="mt-2 text-gray-600">查看和管理已兑换的道具</p>
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
          class="px-4 py-2 bg-purple-500 text-white rounded-lg"
        >
          🎒 我的背包
        </router-link>
        <router-link
          to="/exchange-records"
          class="px-4 py-2 bg-white rounded-lg text-gray-600 hover:bg-gray-100 transition-colors"
        >
          📋 兑换记录
        </router-link>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-500"></div>
      </div>

      <!-- 道具列表 -->
      <div v-else-if="items.length > 0" class="space-y-4">
        <div
          v-for="item in items"
          :key="item.id"
          class="bg-white rounded-xl shadow-sm p-4 flex items-center gap-4"
        >
          <!-- 道具图标 -->
          <div class="w-16 h-16 bg-purple-100 rounded-lg flex items-center justify-center flex-shrink-0 overflow-hidden">
            <img
              :src="item.giftIcon"
              :alt="item.giftName"
              class="w-full h-full object-contain"
              @error="handleImageError"
            />
          </div>

          <!-- 道具信息 -->
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2">
              <h3 class="font-bold text-gray-900">{{ item.giftName }}</h3>
              <span
                class="px-2 py-0.5 text-xs rounded-full"
                :class="item.status === 'UNUSED' ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-500'"
              >
                {{ item.status === 'UNUSED' ? '未使用' : '已使用' }}
              </span>
            </div>
            <p class="text-sm text-gray-500 mt-1">
              兑换时间: {{ formatDate(item.redeemedAt) }}
            </p>
            <p v-if="item.usedAt" class="text-sm text-gray-400">
              使用时间: {{ formatDate(item.usedAt) }}
            </p>
          </div>

          <!-- 操作按钮 -->
          <button
            v-if="item.status === 'UNUSED'"
            class="px-4 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors disabled:opacity-50"
            :disabled="usingItemId === item.id"
            @click="handleUseItem(item)"
          >
            {{ usingItemId === item.id ? '使用中...' : '使用' }}
          </button>
          <span v-else class="text-gray-400 text-sm">已使用</span>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="text-center py-12 bg-white rounded-xl">
        <span class="text-6xl">📭</span>
        <p class="text-gray-500 mt-4">背包空空如也</p>
        <router-link
          to="/gifts"
          class="inline-block mt-4 px-6 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors"
        >
          去商城逛逛
        </router-link>
      </div>
    </div>

    <!-- 使用道具弹窗 -->
    <div
      v-if="showUseModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      @click.self="closeUseModal"
    >
      <div class="bg-white rounded-2xl max-w-md w-full p-6">
        <h2 class="text-xl font-bold text-gray-900">确认使用道具</h2>

        <div v-if="selectedItem" class="mt-4">
          <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <span class="text-2xl">{{ selectedItem.giftIcon || '🎁' }}</span>
            </div>
            <div>
              <h3 class="font-bold text-gray-900">{{ selectedItem.giftName }}</h3>
              <p class="text-sm text-gray-500">道具使用后将无法撤销</p>
            </div>
          </div>
        </div>

        <div class="mt-6 flex gap-3">
          <button
            class="flex-1 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
            @click="closeUseModal"
          >
            取消
          </button>
          <button
            class="flex-1 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors disabled:opacity-50"
            :disabled="using"
            @click="confirmUseItem"
          >
            {{ using ? '使用中...' : '确认使用' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getMyItems, useItem, type UserItemDto } from '../api/gift'

// 道具列表
const items = ref<UserItemDto[]>([])
const loading = ref(true)

// 使用道具
const showUseModal = ref(false)
const selectedItem = ref<UserItemDto | null>(null)
const using = ref(false)
const usingItemId = ref<number | null>(null)

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
  img.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="64" height="64"%3E%3Crect width="64" height="64" fill="%23E5E7EB"/%3E%3Ctext x="50%25" y="50%25" dominant-baseline="middle" text-anchor="middle" font-size="32"%3E🎁%3C/text%3E%3C/svg%3E'
}

// 加载道具列表
const loadItems = async () => {
  loading.value = true
  try {
    const res = await getMyItems()
    if (res.data.code === 200) {
      items.value = res.data.data
    }
  } catch (error) {
    console.error('加载道具列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 打开使用弹窗
const handleUseItem = (item: UserItemDto) => {
  selectedItem.value = item
  showUseModal.value = true
}

// 关闭使用弹窗
const closeUseModal = () => {
  showUseModal.value = false
  selectedItem.value = null
}

// 确认使用道具
const confirmUseItem = async () => {
  if (!selectedItem.value || using.value) return

  using.value = true
  usingItemId.value = selectedItem.value.id

  try {
    const res = await useItem(selectedItem.value.id)
    if (res.data.code === 200) {
      // 更新道具状态
      const item = items.value.find(i => i.id === selectedItem.value!.id)
      if (item) {
        item.status = 'USED' as any
        item.usedAt = new Date().toISOString()
      }
      alert('道具使用成功！')
    }
  } catch (error: any) {
    console.error('使用道具失败:', error)
    alert(error.response?.data?.message || '使用失败，请稍后重试')
  } finally {
    using.value = false
    usingItemId.value = null
    showUseModal.value = false
    selectedItem.value = null
  }
}

onMounted(() => {
  loadItems()
})
</script>