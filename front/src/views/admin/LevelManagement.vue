<template>
  <div class="p-6">
    <div class="flex items-center justify-between mb-5">
      <h1 class="text-2xl font-bold text-gray-900">关卡管理</h1>
      <button @click="openModal()" class="flex items-center gap-1.5 px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm rounded-lg transition">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
        新建关卡
      </button>
    </div>

    <!-- 分类筛选 -->
      <div class="flex gap-2 flex-wrap mb-5">
        <button
          @click="selectedCategoryId = null; loadLevels()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', selectedCategoryId === null ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50']"
        >全部</button>
        <button
          v-for="cat in categories"
          :key="cat.id"
          @click="selectedCategoryId = cat.id; loadLevels()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', selectedCategoryId === cat.id ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50']"
        >{{ cat.name }}</button>
      </div>

      <div v-if="loading" class="flex justify-center py-16">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>

      <!-- 按分类分组展示 -->
      <div v-else class="space-y-6">
        <div v-for="group in groupedLevels" :key="group.categoryId">
          <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-3">{{ group.categoryName }}</h2>
          <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <table class="min-w-full divide-y divide-gray-100">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">关卡名称</th>
                  <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">顺序</th>
                  <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">标准时间</th>
                  <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">操作</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                <tr v-for="level in group.levels" :key="level.id" class="hover:bg-gray-50 transition">
                  <td class="px-4 py-3">
                    <div class="text-sm font-medium text-gray-900">{{ level.name }}</div>
                    <div class="text-xs text-gray-500 mt-0.5">{{ level.description }}</div>
                  </td>
                  <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">第 {{ level.orderIndex }} 关</td>
                  <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">{{ level.standardTime }}秒</td>
                  <td class="px-4 py-3 text-right whitespace-nowrap">
                    <button @click="openModal(level)" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded transition mr-1">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                    </button>
                    <button @click="openDeleteModal(level)" class="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded transition">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div v-if="groupedLevels.length === 0" class="text-center py-16 text-gray-500">暂无关卡数据</div>
    </div>

    <!-- 新建/编辑弹窗 -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="showModal = false">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
        <div class="flex items-center justify-between px-6 py-4 border-b">
          <h3 class="text-lg font-bold text-gray-900">{{ editTarget ? '编辑关卡' : '新建关卡' }}</h3>
          <button @click="showModal = false" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5 space-y-4">
          <div v-if="!editTarget">
            <label class="block text-sm font-medium text-gray-700 mb-1.5">所属分类</label>
            <select v-model="form.categoryId" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option value="">请选择分类</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">关卡名称</label>
            <input v-model="form.name" type="text" placeholder="如：第1关：基础单词" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">描述</label>
            <input v-model="form.description" type="text" placeholder="关卡描述..." class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">关卡顺序</label>
              <input v-model.number="form.levelOrder" type="number" min="1" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">标准时间（秒）</label>
              <input v-model.number="form.standardTimeSec" type="number" min="10" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
          </div>
          <p v-if="formError" class="text-sm text-red-500 bg-red-50 px-3 py-2 rounded-lg">{{ formError }}</p>
        </div>
        <div class="px-6 py-4 border-t flex gap-3 justify-end">
          <button @click="showModal = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleSave" :disabled="saving" class="px-5 py-2 text-sm bg-blue-600 hover:bg-blue-700 disabled:opacity-50 text-white rounded-lg transition">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认 -->
    <div v-if="deleteTarget" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="deleteTarget = null">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-sm p-6 text-center">
        <div class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" /></svg>
        </div>
        <h3 class="text-lg font-bold text-gray-900 mb-2">确认删除</h3>
        <p class="text-sm text-gray-600 mb-6">确认删除关卡 <strong>{{ deleteTarget.name }}</strong>？若有用户进度则无法删除。</p>
        <div class="flex gap-3 justify-center">
          <button @click="deleteTarget = null" class="px-5 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleDelete" class="px-5 py-2 text-sm bg-red-600 hover:bg-red-700 text-white rounded-lg transition">确认删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getAdminCategories, getAdminLevels, createLevel, updateLevel, deleteLevel } from '@/api/admin'
import type { CategoryDto, LevelDto } from '@/api/admin'

const loading = ref(false)
const levels = ref<LevelDto[]>([])
const categories = ref<CategoryDto[]>([])
const selectedCategoryId = ref<number | null>(null)
const showModal = ref(false)
const saving = ref(false)
const formError = ref('')
const editTarget = ref<LevelDto | null>(null)
const deleteTarget = ref<LevelDto | null>(null)

const form = reactive({
  categoryId: '' as number | '',
  name: '',
  description: '',
  levelOrder: 1,
  standardTimeSec: 60
})

// 按分类分组
const groupedLevels = computed(() => {
  const map = new Map<number, { categoryId: number; categoryName: string; levels: LevelDto[] }>()
  levels.value.forEach(level => {
    if (!map.has(level.categoryId)) {
      const cat = categories.value.find(c => c.id === level.categoryId)
      map.set(level.categoryId, { categoryId: level.categoryId, categoryName: cat?.name || `分类${level.categoryId}`, levels: [] })
    }
    map.get(level.categoryId)!.levels.push(level)
  })
  return Array.from(map.values())
})

const loadLevels = async () => {
  loading.value = true
  try {
    const res = await getAdminLevels(selectedCategoryId.value || undefined)
    levels.value = (res.data as any).data || res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  const res = await getAdminCategories()
  categories.value = (res.data as any).data || res.data
}

const openModal = (level?: LevelDto) => {
  editTarget.value = level || null
  form.categoryId = level?.categoryId || ''
  form.name = level?.name || ''
  form.description = level?.description || ''
  form.levelOrder = level?.orderIndex || 1
  form.standardTimeSec = level?.standardTime || 60
  formError.value = ''
  showModal.value = true
}

const openDeleteModal = (level: LevelDto) => { deleteTarget.value = level }

const handleSave = async () => {
  if (!form.name.trim()) { formError.value = '请输入关卡名称'; return }
  if (!editTarget.value && !form.categoryId) { formError.value = '请选择分类'; return }
  saving.value = true
  formError.value = ''
  try {
    if (editTarget.value) {
      await updateLevel(editTarget.value.id, {
        categoryId: editTarget.value.categoryId,
        name: form.name,
        description: form.description,
        levelOrder: form.levelOrder,
        standardTimeMs: form.standardTimeSec
      })
    } else {
      await createLevel({
        categoryId: form.categoryId as number,
        name: form.name,
        description: form.description,
        levelOrder: form.levelOrder,
        standardTimeMs: form.standardTimeSec  // 已经是秒，API层会处理
      })
    }
    showModal.value = false
    loadLevels()
  } catch (e: any) {
    formError.value = e.response?.data?.message || '保存失败'
  } finally {
    saving.value = false
  }
}

const handleDelete = async () => {
  if (!deleteTarget.value) return
  try {
    await deleteLevel(deleteTarget.value.id)
    deleteTarget.value = null
    loadLevels()
  } catch (e: any) {
    alert(e.response?.data?.message || '删除失败')
    deleteTarget.value = null
  }
}

onMounted(async () => {
  await loadCategories()
  await loadLevels()
})
</script>
