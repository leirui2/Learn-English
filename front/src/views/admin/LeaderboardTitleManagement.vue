<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">称号管理</h1>
      <button
        @click="showCreateModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center gap-2"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
        </svg>
        添加称号
      </button>
    </div>

    <!-- 称号列表表格 -->
    <div class="bg-white rounded-lg shadow overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                称号名称
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                排名区间
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                图标
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                颜色
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                排序
              </th>
              <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                操作
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="title in titles" :key="title.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <span v-if="title.icon" class="text-lg mr-2">{{ title.icon }}</span>
                  <span class="text-sm font-medium text-gray-900">{{ title.name }}</span>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ title.minRank }} - {{ title.maxRank }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span class="text-lg">{{ title.icon || '无' }}</span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div 
                    v-if="title.color"
                    class="w-4 h-4 rounded mr-2 border border-gray-300"
                    :style="{ backgroundColor: title.color }"
                  ></div>
                  <span class="text-sm text-gray-500">{{ title.color || '默认' }}</span>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ title.sortOrder }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button
                  @click="editTitle(title)"
                  class="text-blue-600 hover:text-blue-900 mr-3"
                >
                  编辑
                </button>
                <button
                  @click="confirmDelete(title)"
                  class="text-red-600 hover:text-red-900"
                >
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-if="titles.length === 0" class="text-center py-12">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" />
        </svg>
        <h3 class="mt-2 text-sm font-medium text-gray-900">暂无称号</h3>
        <p class="mt-1 text-sm text-gray-500">开始创建第一个排行榜称号</p>
      </div>
    </div>

    <!-- 创建/编辑称号弹窗 -->
    <div v-if="showCreateModal || showEditModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <h3 class="text-lg font-medium text-gray-900 mb-4">
            {{ showCreateModal ? '添加称号' : '编辑称号' }}
          </h3>
          
          <form @submit.prevent="submitForm" class="space-y-4">
            <!-- 称号名称 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">称号名称 *</label>
              <input
                v-model="form.name"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="请输入称号名称"
              />
            </div>

            <!-- 排名区间 -->
            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">最小排名 *</label>
                <input
                  v-model.number="form.minRank"
                  type="number"
                  min="1"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="1"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">最大排名 *</label>
                <input
                  v-model.number="form.maxRank"
                  type="number"
                  min="1"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="10"
                />
              </div>
            </div>

            <!-- 图标 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">图标 (Emoji)</label>
              <input
                v-model="form.icon"
                type="text"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="🏆"
                maxlength="2"
              />
            </div>

            <!-- 颜色 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
              <div class="flex items-center gap-2">
                <input
                  v-model="form.color"
                  type="color"
                  class="w-12 h-10 border border-gray-300 rounded cursor-pointer"
                />
                <input
                  v-model="form.color"
                  type="text"
                  class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="#FFD700"
                />
              </div>
            </div>

            <!-- 排序 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">排序 *</label>
              <input
                v-model.number="form.sortOrder"
                type="number"
                min="0"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="0"
              />
              <p class="text-xs text-gray-500 mt-1">数字越小排序越靠前</p>
            </div>

            <!-- 按钮 -->
            <div class="flex justify-end gap-3 pt-4">
              <button
                type="button"
                @click="closeModal"
                class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-md"
              >
                取消
              </button>
              <button
                type="submit"
                :disabled="loading"
                class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md disabled:opacity-50"
              >
                {{ loading ? '保存中...' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
          <svg class="mx-auto mb-4 w-14 h-14 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">确认删除</h3>
          <p class="text-sm text-gray-500 mb-4">
            确定要删除称号 "{{ titleToDelete?.name }}" 吗？此操作不可撤销。
          </p>
          <div class="flex justify-center gap-3">
            <button
              @click="showDeleteModal = false"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-md"
            >
              取消
            </button>
            <button
              @click="deleteTitle"
              :disabled="loading"
              class="px-4 py-2 text-sm font-medium text-white bg-red-600 hover:bg-red-700 rounded-md disabled:opacity-50"
            >
              {{ loading ? '删除中...' : '确认删除' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { 
  getLeaderboardTitles, 
  createLeaderboardTitle, 
  updateLeaderboardTitle, 
  deleteLeaderboardTitle,
  type LeaderboardTitleDto,
  type CreateLeaderboardTitleRequest,
  type UpdateLeaderboardTitleRequest
} from '@/api/admin'

// 响应式数据
const titles = ref<LeaderboardTitleDto[]>([])
const loading = ref(false)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const showDeleteModal = ref(false)
const titleToDelete = ref<LeaderboardTitleDto | null>(null)
const editingTitle = ref<LeaderboardTitleDto | null>(null)

// 表单数据
const form = ref<CreateLeaderboardTitleRequest>({
  name: '',
  minRank: 1,
  maxRank: 1,
  icon: '',
  color: '#FFD700',
  sortOrder: 0
})

// 加载称号列表
const loadTitles = async () => {
  try {
    loading.value = true
    const response = await getLeaderboardTitles()
    titles.value = response.data.data
  } catch (error) {
    console.error('加载称号列表失败:', error)
    alert('加载称号列表失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.value = {
    name: '',
    minRank: 1,
    maxRank: 1,
    icon: '',
    color: '#FFD700',
    sortOrder: 0
  }
}

// 关闭弹窗
const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingTitle.value = null
  resetForm()
}

// 编辑称号
const editTitle = (title: LeaderboardTitleDto) => {
  editingTitle.value = title
  form.value = {
    name: title.name,
    minRank: title.minRank,
    maxRank: title.maxRank,
    icon: title.icon || '',
    color: title.color || '#FFD700',
    sortOrder: title.sortOrder
  }
  showEditModal.value = true
}

// 提交表单
const submitForm = async () => {
  // 验证排名区间
  if (form.value.minRank > form.value.maxRank) {
    alert('最小排名不能大于最大排名')
    return
  }

  try {
    loading.value = true
    
    if (showCreateModal.value) {
      // 创建称号
      await createLeaderboardTitle(form.value)
      alert('创建成功')
    } else if (showEditModal.value && editingTitle.value) {
      // 更新称号
      const updateData: UpdateLeaderboardTitleRequest = {
        name: form.value.name,
        minRank: form.value.minRank,
        maxRank: form.value.maxRank,
        icon: form.value.icon || undefined,
        color: form.value.color || undefined,
        sortOrder: form.value.sortOrder
      }
      await updateLeaderboardTitle(editingTitle.value.id, updateData)
      alert('更新成功')
    }
    
    closeModal()
    await loadTitles()
  } catch (error: any) {
    console.error('操作失败:', error)
    const message = error.response?.data?.message || '操作失败'
    alert(message)
  } finally {
    loading.value = false
  }
}

// 确认删除
const confirmDelete = (title: LeaderboardTitleDto) => {
  titleToDelete.value = title
  showDeleteModal.value = true
}

// 删除称号
const deleteTitle = async () => {
  if (!titleToDelete.value) return
  
  try {
    loading.value = true
    await deleteLeaderboardTitle(titleToDelete.value.id)
    alert('删除成功')
    showDeleteModal.value = false
    titleToDelete.value = null
    await loadTitles()
  } catch (error: any) {
    console.error('删除失败:', error)
    const message = error.response?.data?.message || '删除失败'
    alert(message)
  } finally {
    loading.value = false
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadTitles()
})
</script>