<template>
  <div class="p-6">
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900">积分记录查询</h1>
      <p class="text-gray-500 text-sm mt-1">查看所有用户的积分变动记录，用于对账和审计</p>
    </div>

    <!-- 筛选栏 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3">
        <input
          v-model="filters.username"
          type="text"
          placeholder="用户名搜索..."
          class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          @input="debouncedSearch"
        />
        <input
          v-model="filters.userId"
          type="text"
          placeholder="用户ID..."
          class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          @input="debouncedSearch"
        />
        <select
          v-model="filters.type"
          @change="loadRecords"
          class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">全部类型</option>
          <option value="LEVEL_COMPLETE">关卡完成</option>
          <option value="CHALLENGE">天梯挑战</option>
          <option value="CHECKIN_BONUS">打卡奖励</option>
          <option value="GIFT_EXCHANGE">道具兑换</option>
          <option value="ADMIN_GRANT">管理员发放</option>
          <option value="ACTIVITY_BONUS">活动奖励</option>
        </select>
        <div class="flex gap-2">
          <input
            v-model="filters.startDate"
            type="date"
            class="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            @change="loadRecords"
          />
          <input
            v-model="filters.endDate"
            type="date"
            class="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            @change="loadRecords"
          />
        </div>
      </div>
      <div class="flex gap-2 mt-3">
        <button
          @click="loadRecords"
          class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm rounded-lg transition"
        >
          搜索
        </button>
        <button
          @click="resetFilters"
          class="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm rounded-lg transition"
        >
          重置
        </button>
      </div>
    </div>

    <!-- 记录表格 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div v-if="loading" class="flex justify-center py-16">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>

      <div v-else-if="records.length === 0" class="text-center py-16 text-gray-500">
        暂无积分记录
      </div>

      <template v-else>
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden lg:table-cell">用户ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">类型</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">积分变动</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">变动后余额</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden lg:table-cell">描述</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden xl:table-cell">关联ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">时间</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr v-for="record in records" :key="record.id" class="hover:bg-gray-50 transition">
                <td class="px-4 py-3">
                  <div class="flex items-center gap-2">
                    <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-white text-xs font-bold flex-shrink-0">
                      {{ record.username?.charAt(0)?.toUpperCase() || '?' }}
                    </div>
                    <span class="text-sm font-medium text-gray-900">{{ record.username }}</span>
                  </div>
                </td>
                <td class="px-4 py-3 text-sm text-gray-500 hidden lg:table-cell font-mono">{{ record.userId?.substring(0, 8) }}...</td>
                <td class="px-4 py-3">
                  <span :class="getTypeClass(record.type)" class="px-2 py-0.5 rounded text-xs font-medium whitespace-nowrap">
                    {{ getTypeLabel(record.type) }}
                  </span>
                </td>
                <td class="px-4 py-3">
                  <span
                    class="text-sm font-bold"
                    :class="record.points > 0 ? 'text-green-600' : 'text-red-500'"
                  >
                    {{ record.points > 0 ? '+' : '' }}{{ record.points }}
                  </span>
                </td>
                <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">{{ record.balanceAfter }}</td>
                <td class="px-4 py-3 text-sm text-gray-500 hidden lg:table-cell max-w-xs truncate">{{ record.description || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500 hidden xl:table-cell">
                  <span v-if="record.relatedId" class="text-blue-600 cursor-pointer hover:underline" @click="goToRelated(record)">
                    {{ record.relatedId }}
                  </span>
                  <span v-else>-</span>
                </td>
                <td class="px-4 py-3 text-sm text-gray-500 whitespace-nowrap">{{ formatDate(record.createdAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 分页 -->
        <div v-if="totalPages > 1" class="px-4 py-3 border-t border-gray-100 flex items-center justify-between">
          <span class="text-sm text-gray-500">共 {{ total }} 条</span>
          <div class="flex gap-1">
            <button
              @click="goToPage(currentPage - 1)"
              :disabled="currentPage === 0"
              class="px-3 py-1 text-sm rounded transition"
              :class="currentPage === 0 ? 'bg-gray-50 text-gray-300 cursor-not-allowed' : 'bg-gray-100 hover:bg-gray-200 text-gray-700'"
            >
              上一页
            </button>
            <button
              v-for="p in visiblePages"
              :key="p"
              @click="goToPage(p)"
              class="px-3 py-1 text-sm rounded transition"
              :class="p === currentPage ? 'bg-blue-600 text-white' : 'bg-gray-100 hover:bg-gray-200 text-gray-700'"
            >
              {{ p + 1 }}
            </button>
            <button
              @click="goToPage(currentPage + 1)"
              :disabled="currentPage >= totalPages - 1"
              class="px-3 py-1 text-sm rounded transition"
              :class="currentPage >= totalPages - 1 ? 'bg-gray-50 text-gray-300 cursor-not-allowed' : 'bg-gray-100 hover:bg-gray-200 text-gray-700'"
            >
              下一页
            </button>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAdminPointsRecords, type AdminPointsRecordDto } from '@/api/admin'

const router = useRouter()

const loading = ref(false)
const records = ref<AdminPointsRecordDto[]>([])
const total = ref(0)
const currentPage = ref(0)
const totalPages = ref(0)
const pageSize = ref(20)

const filters = reactive({
  username: '',
  userId: '',
  type: '',
  startDate: '',
  endDate: ''
})

// 计算可见的页码
const visiblePages = computed(() => {
  const pages: number[] = []
  const start = Math.max(0, currentPage.value - 2)
  const end = Math.min(totalPages.value - 1, currentPage.value + 2)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  })
}

const getTypeLabel = (type: string): string => {
  const labels: Record<string, string> = {
    LEVEL_COMPLETE: '关卡完成',
    CHALLENGE: '天梯挑战',
    CHECKIN_BONUS: '打卡奖励',
    GIFT_EXCHANGE: '道具兑换',
    ADMIN_GRANT: '管理员发放',
    ACTIVITY_BONUS: '活动奖励'
  }
  return labels[type] || type
}

const getTypeClass = (type: string): string => {
  const classes: Record<string, string> = {
    LEVEL_COMPLETE: 'bg-blue-100 text-blue-700',
    CHALLENGE: 'bg-orange-100 text-orange-700',
    CHECKIN_BONUS: 'bg-green-100 text-green-700',
    GIFT_EXCHANGE: 'bg-purple-100 text-purple-700',
    ADMIN_GRANT: 'bg-yellow-100 text-yellow-700',
    ACTIVITY_BONUS: 'bg-pink-100 text-pink-700'
  }
  return classes[type] || 'bg-gray-100 text-gray-700'
}

const goToRelated = (record: AdminPointsRecordDto) => {
  if (!record.relatedId) return
  // 根据类型跳转到不同的详情页
  if (record.type === 'LEVEL_COMPLETE') {
    router.push(`/levels/${record.relatedId}`)
  } else if (record.type === 'CHALLENGE') {
    // 挑战记录暂无详情页，可以跳转到天梯挑战页
    router.push('/challenge')
  }
}

const loadRecords = async () => {
  loading.value = true
  try {
    const params: Record<string, any> = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (filters.username) params.username = filters.username
    if (filters.userId) params.userId = filters.userId
    if (filters.type) params.type = filters.type
    if (filters.startDate) params.startDate = filters.startDate + ' 00:00:00'
    if (filters.endDate) params.endDate = filters.endDate + ' 23:59:59'

    const res = await getAdminPointsRecords(params)
    const data = (res.data as any).data
    records.value = data.records || []
    total.value = data.total || 0
    totalPages.value = data.totalPages || 0
  } catch (e) {
    console.error('加载积分记录失败:', e)
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  filters.username = ''
  filters.userId = ''
  filters.type = ''
  filters.startDate = ''
  filters.endDate = ''
  currentPage.value = 0
  loadRecords()
}

const goToPage = (page: number) => {
  if (page < 0 || page >= totalPages.value) return
  currentPage.value = page
  loadRecords()
}

let searchTimer: ReturnType<typeof setTimeout> | null = null
const debouncedSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    currentPage.value = 0
    loadRecords()
  }, 400)
}

onMounted(loadRecords)
</script>
