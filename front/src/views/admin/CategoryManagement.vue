<template>
  <div class="p-6">
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-bold text-gray-900">分类管理</h1>
      <button @click="openModal()" class="flex items-center gap-1.5 px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm rounded-lg transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
          新建分类
        </button>
    </div>

    <div v-if="loading" class="flex justify-center py-16">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="bg-white rounded-xl shadow-sm border border-gray-100 p-5 hover:shadow-md transition"
        >
          <div class="flex items-start justify-between mb-3">
            <div>
              <h3 class="text-base font-bold text-gray-900">{{ cat.name }}</h3>
              <div class="flex items-center gap-1 mt-1">
                <span v-for="i in 5" :key="i" :class="i <= (cat.difficultyLevel || cat.difficulty || 0) ? 'text-yellow-400' : 'text-gray-200'" class="text-sm">★</span>
                <span class="text-xs text-gray-500 ml-1">难度 {{ cat.difficultyLevel || cat.difficulty }}</span>
              </div>
            </div>
            <div class="flex gap-1">
              <button @click="openModal(cat)" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
              </button>
              <button @click="openDeleteModal(cat)" class="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
              </button>
            </div>
          </div>
          <p class="text-sm text-gray-500">{{ cat.description || '暂无描述' }}</p>
        </div>
      </div>

    <!-- 新建/编辑弹窗 -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="showModal = false">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
        <div class="flex items-center justify-between px-6 py-4 border-b">
          <h3 class="text-lg font-bold text-gray-900">{{ editTarget ? '编辑分类' : '新建分类' }}</h3>
          <button @click="showModal = false" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5 space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">分类名称</label>
            <input v-model="form.name" type="text" placeholder="如：小学、初中..." class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">描述</label>
            <textarea v-model="form.description" rows="2" placeholder="分类描述..." class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">难度</label>
            <div class="flex gap-1">
              <button
                v-for="i in 5"
                :key="i"
                type="button"
                @click="form.difficulty = i"
                class="text-2xl transition hover:scale-110 focus:outline-none"
              >
                <span :class="i <= form.difficulty ? 'text-yellow-400' : 'text-gray-300'">★</span>
              </button>
              <span class="ml-2 text-sm text-gray-500 self-center">{{ form.difficulty }} 级</span>
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
        <p class="text-sm text-gray-600 mb-6">确认删除分类 <strong>{{ deleteTarget.name }}</strong>？若该分类下有关卡则无法删除。</p>
        <div class="flex gap-3 justify-center">
          <button @click="deleteTarget = null" class="px-5 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleDelete" class="px-5 py-2 text-sm bg-red-600 hover:bg-red-700 text-white rounded-lg transition">确认删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getAdminCategories, createCategory, updateCategory, deleteCategory } from '@/api/admin'
import type { CategoryDto } from '@/api/admin'

const loading = ref(false)
const categories = ref<CategoryDto[]>([])
const showModal = ref(false)
const saving = ref(false)
const formError = ref('')
const editTarget = ref<CategoryDto | null>(null)
const deleteTarget = ref<CategoryDto | null>(null)

const form = reactive({ name: '', description: '', difficulty: 1 })

const loadCategories = async () => {
  loading.value = true
  try {
    const res = await getAdminCategories()
    categories.value = (res.data as any).data || res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const openModal = (cat?: CategoryDto) => {
  editTarget.value = cat || null
  form.name = cat?.name || ''
  form.description = cat?.description || ''
  form.difficulty = cat?.difficultyLevel || cat?.difficulty || 1
  formError.value = ''
  showModal.value = true
}

const openDeleteModal = (cat: CategoryDto) => { deleteTarget.value = cat }

const handleSave = async () => {
  if (!form.name.trim()) { formError.value = '请输入分类名称'; return }
  saving.value = true
  formError.value = ''
  try {
    if (editTarget.value) {
      await updateCategory(editTarget.value.id, { name: form.name, description: form.description, difficulty: form.difficulty })
    } else {
      await createCategory({ name: form.name, description: form.description, difficulty: form.difficulty })
    }
    showModal.value = false
    loadCategories()
  } catch (e: any) {
    formError.value = e.response?.data?.message || '保存失败'
  } finally {
    saving.value = false
  }
}

const handleDelete = async () => {
  if (!deleteTarget.value) return
  try {
    await deleteCategory(deleteTarget.value.id)
    deleteTarget.value = null
    loadCategories()
  } catch (e: any) {
    alert(e.response?.data?.message || '删除失败，该分类下可能存在关卡')
    deleteTarget.value = null
  }
}

onMounted(loadCategories)
</script>
