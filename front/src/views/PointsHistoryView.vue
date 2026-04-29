<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50">
    <!-- 顶部装饰 -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-purple-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-blue-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-2000"></div>
    </div>

    <div class="max-w-4xl mx-auto px-4 py-8 relative">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center gap-3 mb-2">
          <router-link to="/profile" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
            </svg>
          </router-link>
          <h1 class="text-3xl font-bold text-gray-900">积分记录</h1>
        </div>
        <p class="text-gray-500 ml-8">查看你的积分变动明细</p>
      </div>

      <!-- 筛选区域 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6">
        <div class="flex flex-col sm:flex-row gap-4 items-start sm:items-center">
          <div class="flex items-center gap-2">
            <span class="text-sm text-gray-500">类型筛选：</span>
            <select
              v-model="selectedType"
              @change="handleTypeChange"
              class="px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white"
            >
              <option value="">全部</option>
              <option v-for="(label, type) in PointsTypeLabels" :key="type" :value="type">
                {{ label }}
              </option>
            </select>
          </div>
          <div class="text-sm text-gray-500">
            共 <span class="font-semibold text-gray-900">{{ total }}</span> 条记录
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center items-center py-20">
        <div class="relative">
          <div class="animate-spin rounded-full h-12 w-12 border-4 border-blue-200"></div>
          <div class="animate-spin rounded-full h-12 w-12 border-t-4 border-blue-600 absolute top-0 left-0"></div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else-if="records.length === 0" class="bg-white rounded-xl shadow-sm border border-gray-100 p-12 text-center">
        <div class="text-6xl mb-4">📝</div>
        <h3 class="text-xl font-semibold text-gray-700 mb-2">暂无积分记录</h3>
        <p class="text-gray-500 mb-6">完成关卡或参与活动来获得积分吧！</p>
        <router-link
          to="/levels"
          class="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-medium rounded-lg hover:from-blue-700 hover:to-purple-700 transition"
        >
          <span>去练习</span>
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
          </svg>
        </router-link>
      </div>

      <!-- 积分记录列表 -->
      <div v-else class="space-y-3">
        <div
          v-for="record in records"
          :key="record.id"
          class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 hover:shadow-md transition"
        >
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <!-- 图标 -->
              <div
                class="w-12 h-12 rounded-xl flex items-center justify-center text-xl"
                :class="getIconBg(record.type)"
              >
                {{ getTypeIcon(record.type) }}
              </div>
              <div>
                <div class="flex items-center gap-2 mb-1">
                  <span class="font-semibold text-gray-900">{{ getTypeLabel(record.type) }}</span>
                  <span
                    class="text-xs px-2 py-0.5 rounded-full"
                    :class="record.points > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'"
                  >
                    {{ record.points > 0 ? '获得' : '消耗' }}
                  </span>
                </div>
                <p class="text-sm text-gray-500">{{ record.description || '—' }}</p>
                <p class="text-xs text-gray-400 mt-1">{{ formatTime(record.createdAt) }}</p>
              </div>
            </div>
            <div class="text-right">
              <div
                class="text-2xl font-bold"
                :class="record.points > 0 ? 'text-green-600' : 'text-red-500'"
              >
                {{ record.points > 0 ? '+' : '' }}{{ record.points }}
              </div>
              <div class="text-xs text-gray-400 mt-1">余额: {{ record.balanceAfter }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="flex justify-center items-center gap-2 mt-8">
        <button
          @click="goToPage(page - 1)"
          :disabled="page === 0"
          class="px-4 py-2 rounded-lg border border-gray-200 text-sm font-medium transition"
          :class="page === 0 ? 'bg-gray-50 text-gray-300 cursor-not-allowed' : 'bg-white text-gray-700 hover:bg-gray-50'"
        >
          上一页
        </button>
        <div class="flex items-center gap-1">
          <button
            v-for="p in visiblePages"
            :key="p"
            @click="goToPage(p)"
            class="w-10 h-10 rounded-lg text-sm font-medium transition"
            :class="p === page ? 'bg-blue-600 text-white' : 'bg-white border border-gray-200 text-gray-700 hover:bg-gray-50'"
          >
            {{ p + 1 }}
          </button>
        </div>
        <button
          @click="goToPage(page + 1)"
          :disabled="page >= totalPages - 1"
          class="px-4 py-2 rounded-lg border border-gray-200 text-sm font-medium transition"
          :class="page >= totalPages - 1 ? 'bg-gray-50 text-gray-300 cursor-not-allowed' : 'bg-white text-gray-700 hover:bg-gray-50'"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getMyPointsRecords, PointsType, PointsTypeLabels, type PointsRecordDto } from '@/api/points'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const records = ref<PointsRecordDto[]>([])
const total = ref(0)
const page = ref(0)
const size = ref(20)
const totalPages = ref(0)
const selectedType = ref<PointsType | ''>('')

// 计算可见的页码
const visiblePages = computed(() => {
  const pages: number[] = []
  const start = Math.max(0, page.value - 2)
  const end = Math.min(totalPages.value - 1, page.value + 2)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// 获取类型图标
const getTypeIcon = (type: PointsType): string => {
  const icons: Record<PointsType, string> = {
    [PointsType.LEVEL_COMPLETE]: '🎯',
    [PointsType.CHALLENGE]: '⚡',
    [PointsType.CHECKIN_BONUS]: '🔥',
    [PointsType.GIFT_EXCHANGE]: '🎁',
    [PointsType.ADMIN_GRANT]: '👑',
    [PointsType.ACTIVITY_BONUS]: '🎉'
  }
  return icons[type] || '💰'
}

// 获取图标背景色
const getIconBg = (type: PointsType): string => {
  const bgs: Record<PointsType, string> = {
    [PointsType.LEVEL_COMPLETE]: 'bg-blue-100',
    [PointsType.CHALLENGE]: 'bg-orange-100',
    [PointsType.CHECKIN_BONUS]: 'bg-green-100',
    [PointsType.GIFT_EXCHANGE]: 'bg-purple-100',
    [PointsType.ADMIN_GRANT]: 'bg-yellow-100',
    [PointsType.ACTIVITY_BONUS]: 'bg-pink-100'
  }
  return bgs[type] || 'bg-gray-100'
}

// 获取类型标签
const getTypeLabel = (type: PointsType): string => {
  return PointsTypeLabels[type] || type
}

// 格式化时间
const formatTime = (dateStr: string): string => {
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes} 分钟前`
  if (hours < 24) return `${hours} 小时前`
  if (days < 7) return `${days} 天前`

  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getMyPointsRecords(
      page.value,
      size.value,
      selectedType.value || undefined
    )
    const data = res.data.data
    records.value = data.records
    total.value = data.total
    totalPages.value = data.totalPages
  } catch (error) {
    console.error('加载积分记录失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理类型变化
const handleTypeChange = () => {
  page.value = 0
  updateUrl()
  loadData()
}

// 跳转到指定页
const goToPage = (p: number) => {
  if (p < 0 || p >= totalPages.value) return
  page.value = p
  updateUrl()
  loadData()
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 更新 URL 参数
const updateUrl = () => {
  const query: Record<string, string> = {}
  if (page.value > 0) query.page = String(page.value)
  if (selectedType.value) query.type = selectedType.value
  router.replace({ query })
}

// 从 URL 读取参数
const readParamsFromUrl = () => {
  const { page: p, type } = route.query
  if (p) {
    const pageNum = parseInt(p as string, 10)
    if (!isNaN(pageNum) && pageNum >= 0) {
      page.value = pageNum
    }
  }
  if (type && Object.values(PointsType).includes(type as PointsType)) {
    selectedType.value = type as PointsType
  }
}

// 监听路由变化
watch(() => route.query, readParamsFromUrl, { immediate: true })

onMounted(() => {
  readParamsFromUrl()
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
</style>
