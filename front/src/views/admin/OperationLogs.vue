<template>
  <div class="p-6">
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900">操作日志</h1>
      <p class="text-gray-500 text-sm mt-1">查看所有管理员的操作记录</p>
    </div>

    <!-- 筛选栏 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6 flex flex-wrap gap-3">
      <input
        v-model="filters.operatorId"
        type="text"
        placeholder="操作人ID..."
        class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 w-40"
        @input="debouncedSearch"
      />
      <select v-model="filters.operationType" @change="loadLogs" class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
        <option value="">全部操作类型</option>
        <option value="CREATE">创建</option>
        <option value="UPDATE">更新</option>
        <option value="DELETE">删除</option>
        <option value="BAN">封禁</option>
        <option value="UNBAN">解封</option>
        <option value="RESET_PASSWORD">重置密码</option>
      </select>
      <select v-model="filters.targetType" @change="loadLogs" class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
        <option value="">全部对象类型</option>
        <option value="USER">用户</option>
        <option value="CATEGORY">分类</option>
        <option value="LEVEL">关卡</option>
        <option value="EXERCISE">练习内容</option>
      </select>
      <input v-model="filters.startDate" type="date" class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" @change="loadLogs" />
      <input v-model="filters.endDate" type="date" class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" @change="loadLogs" />
      <button @click="resetFilters" class="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm rounded-lg transition">重置</button>
    </div>

    <!-- 日志表格 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div v-if="loading" class="flex justify-center py-16">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>

      <div v-else-if="logs.length === 0" class="text-center py-16 text-gray-500">暂无日志数据</div>

      <table v-else class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作人</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作类型</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">对象类型</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden lg:table-cell">详情</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">IP</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">时间</th>
            <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="log in logs" :key="log.id" class="hover:bg-gray-50 transition">
            <td class="px-4 py-3 text-sm font-medium text-gray-900">{{ log.operatorName }}</td>
            <td class="px-4 py-3">
              <span :class="getOperationTypeClass(log.operationType)" class="px-2 py-0.5 rounded text-xs font-medium whitespace-nowrap">
                {{ getOperationTypeLabel(log.operationType) }}
              </span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">{{ getTargetTypeLabel(log.targetType) }}</td>
            <td class="px-4 py-3 text-sm text-gray-500 hidden lg:table-cell max-w-xs truncate">{{ log.operationDetail }}</td>
            <td class="px-4 py-3 text-sm text-gray-500 hidden md:table-cell">{{ log.ipAddress }}</td>
            <td class="px-4 py-3 text-sm text-gray-500 whitespace-nowrap">{{ formatDate(log.createdAt) }}</td>
            <td class="px-4 py-3 text-right">
              <button @click="openDetail(log)" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="px-4 py-3 border-t border-gray-100 flex items-center justify-between">
        <span class="text-sm text-gray-500">共 {{ totalElements }} 条</span>
        <div class="flex gap-1">
          <button
            v-for="p in pageNumbers"
            :key="p"
            @click="currentPage = p; loadLogs()"
            :class="['px-3 py-1 text-sm rounded transition', currentPage === p ? 'bg-blue-600 text-white' : 'bg-gray-100 hover:bg-gray-200 text-gray-700']"
          >{{ p + 1 }}</button>
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="detailLog" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="detailLog = null">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-lg">
        <div class="flex items-center justify-between px-6 py-4 border-b">
          <h3 class="text-lg font-bold text-gray-900">操作详情</h3>
          <button @click="detailLog = null" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5 space-y-3">
          <div class="flex justify-between"><span class="text-sm text-gray-500">操作人</span><span class="text-sm font-medium">{{ detailLog.operatorName }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">操作类型</span><span :class="getOperationTypeClass(detailLog.operationType)" class="px-2 py-0.5 rounded text-xs font-medium">{{ getOperationTypeLabel(detailLog.operationType) }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">对象类型</span><span class="text-sm font-medium">{{ getTargetTypeLabel(detailLog.targetType) }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">对象ID</span><span class="text-sm font-medium">{{ detailLog.targetId }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">IP地址</span><span class="text-sm font-medium">{{ detailLog.ipAddress }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">操作时间</span><span class="text-sm font-medium">{{ formatDate(detailLog.createdAt) }}</span></div>
          <div v-if="detailLog.operationDetail">
            <span class="text-sm text-gray-500 block mb-1">操作详情</span>
            <pre class="text-xs bg-gray-50 rounded-lg p-3 overflow-auto max-h-40 text-gray-700">{{ formatDetail(detailLog.operationDetail) }}</pre>
          </div>
        </div>
        <div class="px-6 py-4 border-t flex justify-end">
          <button @click="detailLog = null" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getAdminLogs } from '@/api/admin'
import type { AdminOperationLogDto } from '@/api/admin'

const loading = ref(false)
const logs = ref<AdminOperationLogDto[]>([])
const totalElements = ref(0)
const totalPages = ref(0)
const currentPage = ref(0)
const detailLog = ref<AdminOperationLogDto | null>(null)

const filters = reactive({
  operatorId: '',
  operationType: '',
  targetType: '',
  startDate: '',
  endDate: ''
})

const pageNumbers = computed(() => Array.from({ length: Math.min(totalPages.value, 10) }, (_, i) => i))

const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN', { hour12: false })
}

const formatDetail = (detail: string) => {
  try {
    return JSON.stringify(JSON.parse(detail), null, 2)
  } catch {
    return detail
  }
}

const getOperationTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    CREATE: '创建', UPDATE: '更新', DELETE: '删除',
    BAN: '封禁', UNBAN: '解封', RESET_PASSWORD: '重置密码'
  }
  return map[type] || type
}

const getOperationTypeClass = (type: string) => {
  const map: Record<string, string> = {
    CREATE: 'bg-green-100 text-green-700',
    UPDATE: 'bg-blue-100 text-blue-700',
    DELETE: 'bg-red-100 text-red-700',
    BAN: 'bg-red-100 text-red-700',
    UNBAN: 'bg-green-100 text-green-700',
    RESET_PASSWORD: 'bg-yellow-100 text-yellow-700'
  }
  return map[type] || 'bg-gray-100 text-gray-700'
}

const getTargetTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    USER: '用户', CATEGORY: '分类', LEVEL: '关卡', EXERCISE: '练习内容'
  }
  return map[type] || type
}

const loadLogs = async () => {
  loading.value = true
  try {
    const params: Record<string, any> = {
      page: currentPage.value,
      size: 20
    }
    if (filters.operatorId) params.operatorId = filters.operatorId
    if (filters.operationType) params.operationType = filters.operationType
    if (filters.targetType) params.targetType = filters.targetType
    if (filters.startDate) params.startDate = filters.startDate + 'T00:00:00'
    if (filters.endDate) params.endDate = filters.endDate + 'T23:59:59'

    const res = await getAdminLogs(params)
    const data = (res.data as any).data
    logs.value = data.content
    totalElements.value = data.totalElements
    totalPages.value = data.totalPages
  } catch (e) {
    console.error('加载日志失败:', e)
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  filters.operatorId = ''
  filters.operationType = ''
  filters.targetType = ''
  filters.startDate = ''
  filters.endDate = ''
  currentPage.value = 0
  loadLogs()
}

const openDetail = (log: AdminOperationLogDto) => { detailLog.value = log }

let searchTimer: ReturnType<typeof setTimeout> | null = null
const debouncedSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => { currentPage.value = 0; loadLogs() }, 400)
}

onMounted(loadLogs)
</script>
