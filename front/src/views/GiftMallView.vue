<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-6xl mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">🎁 积分商城</h1>
        <p class="mt-2 text-gray-600">使用积分兑换精美道具</p>
      </div>

      <!-- 积分余额卡片 -->
      <div class="bg-gradient-to-r from-purple-500 to-indigo-600 rounded-2xl p-6 mb-8 text-white shadow-lg">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-purple-100 text-sm">当前积分</p>
            <p class="text-4xl font-bold mt-1">{{ userStore.userInfo?.totalScore ?? 0 }}</p>
          </div>
          <div class="flex gap-3">
            <router-link
              to="/my-items"
              class="px-4 py-2 bg-white/20 hover:bg-white/30 rounded-lg transition-colors"
            >
              🎒 我的背包
            </router-link>
            <router-link
              to="/exchange-records"
              class="px-4 py-2 bg-white/20 hover:bg-white/30 rounded-lg transition-colors"
            >
              📋 兑换记录
            </router-link>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-500"></div>
      </div>

      <!-- 道具列表 -->
      <div v-else-if="gifts.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div
          v-for="gift in gifts"
          :key="gift.id"
          class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow cursor-pointer"
          @click="openExchangeModal(gift)"
        >
          <!-- 道具图标 -->
          <div class="h-40 bg-gradient-to-br from-purple-100 to-indigo-100 flex items-center justify-center">
            <img
              :src="gift.icon"
              :alt="gift.name"
              class="w-24 h-24 object-contain"
              @error="handleImageError"
            />
          </div>

          <!-- 道具信息 -->
          <div class="p-4">
            <h3 class="font-bold text-lg text-gray-900 truncate">{{ gift.name }}</h3>
            <p class="text-gray-500 text-sm mt-1 line-clamp-2 h-10">{{ gift.description }}</p>

            <!-- 积分和库存 -->
            <div class="mt-4 flex items-center justify-between">
              <div class="flex items-center text-purple-600 font-bold">
                <span class="text-xl">{{ gift.pointsCost }}</span>
                <span class="text-sm ml-1">积分</span>
              </div>
              <div class="text-sm">
                <span v-if="gift.stock > 0" class="text-green-600">
                  库存: {{ gift.stock }}
                </span>
                <span v-else class="text-red-500">暂时缺货</span>
              </div>
            </div>

            <!-- 兑换按钮 -->
            <button
              class="mt-4 w-full py-2 rounded-lg font-medium transition-colors"
              :class="getButtonClass(gift)"
              :disabled="!canExchange(gift)"
              @click.stop="openExchangeModal(gift)"
            >
              {{ getButtonText(gift) }}
            </button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="text-center py-12">
        <span class="text-6xl">📭</span>
        <p class="text-gray-500 mt-4">暂无可兑换的道具</p>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="mt-8 flex justify-center gap-2">
        <button
          v-for="p in totalPages"
          :key="p"
          class="px-4 py-2 rounded-lg transition-colors"
          :class="p - 1 === page ? 'bg-purple-500 text-white' : 'bg-white text-gray-700 hover:bg-gray-100'"
          @click="loadGifts(p - 1)"
        >
          {{ p }}
        </button>
      </div>
    </div>

    <!-- 兑换确认弹窗 -->
    <div
      v-if="showExchangeModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      @click.self="closeExchangeModal"
    >
      <div class="bg-white rounded-2xl max-w-md w-full p-6">
        <h2 class="text-xl font-bold text-gray-900">确认兑换</h2>

        <div v-if="selectedGift" class="mt-4">
          <!-- 道具预览 -->
          <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
            <div class="w-16 h-16 bg-purple-100 rounded-lg flex items-center justify-center overflow-hidden">
              <img
                :src="selectedGift.icon"
                :alt="selectedGift.name"
                class="w-full h-full object-contain"
                @error="handleImageError"
              />
            </div>
            <div class="flex-1">
              <h3 class="font-bold text-gray-900">{{ selectedGift.name }}</h3>
              <p class="text-sm text-gray-500">{{ selectedGift.description }}</p>
            </div>
          </div>

          <!-- 兑换信息 -->
          <div class="mt-4 space-y-2 text-sm">
            <div class="flex justify-between">
              <span class="text-gray-500">所需积分</span>
              <span class="font-bold text-purple-600">{{ selectedGift.pointsCost }} 积分</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">当前积分</span>
              <span class="font-bold">{{ userStore.userInfo?.totalScore ?? 0 }} 积分</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">兑换后余额</span>
              <span class="font-bold" :class="remainingPoints >= 0 ? 'text-green-600' : 'text-red-500'">
                {{ remainingPoints }} 积分
              </span>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="mt-6 flex gap-3">
          <button
            class="flex-1 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
            @click="closeExchangeModal"
          >
            取消
          </button>
          <button
            class="flex-1 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            :disabled="exchanging || remainingPoints < 0"
            @click="confirmExchange"
          >
            {{ exchanging ? '兑换中...' : '确认兑换' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 兑换结果弹窗 -->
    <div
      v-if="showResultModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      @click.self="closeResultModal"
    >
      <div class="bg-white rounded-2xl max-w-md w-full p-6 text-center">
        <div class="text-6xl mb-4">
          {{ exchangeResult?.success ? '🎉' : '😢' }}
        </div>
        <h2 class="text-xl font-bold text-gray-900">
          {{ exchangeResult?.success ? '兑换成功' : '兑换失败' }}
        </h2>
        <p class="mt-2 text-gray-600">{{ exchangeResult?.message }}</p>

        <div v-if="exchangeResult?.success" class="mt-4 p-4 bg-green-50 rounded-xl">
          <p class="text-sm text-green-600">
            消耗 {{ exchangeResult?.pointsSpent }} 积分，剩余 {{ exchangeResult?.remainingPoints }} 积分
          </p>
        </div>

        <button
          class="mt-6 w-full py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors"
          @click="closeResultModal"
        >
          确定
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import {
  getGifts,
  exchangeGift,
  type GiftDto,
  type ExchangeGiftResponse
} from '../api/gift'

const userStore = useAuthStore()

// 道具列表
const gifts = ref<GiftDto[]>([])
const loading = ref(true)
const page = ref(0)
const totalPages = ref(0)

// 兑换弹窗
const showExchangeModal = ref(false)
const selectedGift = ref<GiftDto | null>(null)
const exchanging = ref(false)

// 兑换结果
const showResultModal = ref(false)
const exchangeResult = ref<ExchangeGiftResponse | null>(null)

// 计算兑换后剩余积分
const remainingPoints = computed(() => {
  if (!selectedGift.value) return 0
  return (userStore.userInfo?.totalScore ?? 0) - selectedGift.value.pointsCost
})

// 图片加载失败处理
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="96" height="96"%3E%3Crect width="96" height="96" fill="%23E5E7EB"/%3E%3Ctext x="50%25" y="50%25" dominant-baseline="middle" text-anchor="middle" font-size="48"%3E🎁%3C/text%3E%3C/svg%3E'
}

// 加载道具列表
const loadGifts = async (p: number = 0) => {
  loading.value = true
  try {
    const res = await getGifts(p, 20)
    if (res.data.code === 200) {
      gifts.value = res.data.data.content
      totalPages.value = res.data.data.totalPages
      page.value = res.data.data.number
    }
  } catch (error) {
    console.error('加载道具列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 判断是否可以兑换
const canExchange = (gift: GiftDto) => {
  return gift.stock > 0 && (userStore.userInfo?.totalScore ?? 0) >= gift.pointsCost
}

// 获取按钮样式
const getButtonClass = (gift: GiftDto) => {
  if (gift.stock <= 0) {
    return 'bg-gray-200 text-gray-400 cursor-not-allowed'
  }
  if ((userStore.userInfo?.totalScore ?? 0) < gift.pointsCost) {
    return 'bg-gray-200 text-gray-500 cursor-not-allowed'
  }
  return 'bg-purple-500 text-white hover:bg-purple-600'
}

// 获取按钮文本
const getButtonText = (gift: GiftDto) => {
  if (gift.stock <= 0) return '暂时缺货'
  if ((userStore.userInfo?.totalScore ?? 0) < gift.pointsCost) return '积分不足'
  return '立即兑换'
}

// 打开兑换弹窗
const openExchangeModal = (gift: GiftDto) => {
  if (!canExchange(gift)) return
  selectedGift.value = gift
  showExchangeModal.value = true
}

// 关闭兑换弹窗
const closeExchangeModal = () => {
  showExchangeModal.value = false
  selectedGift.value = null
}

// 确认兑换
const confirmExchange = async () => {
  if (!selectedGift.value || exchanging.value) return

  exchanging.value = true
  try {
    const res = await exchangeGift(selectedGift.value.id)
    if (res.data.code === 200) {
      exchangeResult.value = res.data.data

      // 兑换成功，更新用户积分
      if (exchangeResult.value.success) {
        if (userStore.userInfo) {
          userStore.userInfo.totalScore = exchangeResult.value.remainingPoints
        }
        // 刷新道具列表
        await loadGifts(page.value)
      }
    }
  } catch (error) {
    console.error('兑换失败:', error)
    exchangeResult.value = {
      success: false,
      giftName: selectedGift.value.name,
      pointsSpent: 0,
      remainingPoints: userStore.userInfo?.totalScore ?? 0,
      message: '网络错误，请稍后重试'
    }
  } finally {
    exchanging.value = false
    showExchangeModal.value = false
    showResultModal.value = true
  }
}

// 关闭结果弹窗
const closeResultModal = () => {
  showResultModal.value = false
  exchangeResult.value = null
}

onMounted(() => {
  loadGifts()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>