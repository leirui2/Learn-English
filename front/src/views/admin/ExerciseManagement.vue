<template>
  <div class="p-6">
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-bold text-gray-900">练习内容管理</h1>
      <div class="flex gap-2">
        <button @click="openBatchModal()" class="flex items-center gap-1.5 px-3 py-2 bg-green-600 hover:bg-green-700 text-white text-sm rounded-lg transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" /></svg>
          批量导入
        </button>
        <button @click="openModal()" class="flex items-center gap-1.5 px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm rounded-lg transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
          新建练习
        </button>
      </div>
    </div>

    <!-- 关卡筛选 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6 flex items-center gap-3">
      <label class="text-sm font-medium text-gray-700 whitespace-nowrap">选择关卡</label>
      <select
        v-model="selectedLevelId"
        @change="loadExercises"
        class="flex-1 max-w-xs px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option :value="null">-- 请选择关卡 --</option>
        <option v-for="level in levels" :key="level.id" :value="level.id">{{ level.name }}</option>
      </select>
      <span v-if="selectedLevelId" class="text-sm text-gray-500">共 {{ exercises.length }} 条</span>
    </div>

    <div v-if="loading" class="flex justify-center py-16">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
    </div>

    <!-- 练习内容表格 -->
    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div v-if="!selectedLevelId" class="text-center py-16 text-gray-500">请先选择关卡</div>
      <div v-else-if="exercises.length === 0" class="text-center py-16 text-gray-500">该关卡暂无练习内容</div>
      <table v-else class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">序号</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">英文内容</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">中文释义</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">所属关卡</th>
            <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="ex in exercises" :key="ex.id" class="hover:bg-gray-50 transition">
            <td class="px-4 py-3 text-sm text-gray-600">{{ ex.orderIndex }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-900">{{ ex.contentEn }}</td>
            <td class="px-4 py-3 text-sm text-gray-600">{{ ex.contentZh }}</td>
            <td class="px-4 py-3 text-sm text-gray-500 hidden md:table-cell">{{ ex.levelName }}</td>
            <td class="px-4 py-3 text-right whitespace-nowrap">
              <button @click="openModal(ex)" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded transition mr-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
              </button>
              <button @click="openDeleteModal(ex)" class="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新建/编辑弹窗 -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="showModal = false">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
        <div class="flex items-center justify-between px-6 py-4 border-b">
          <h3 class="text-lg font-bold text-gray-900">{{ editTarget ? '编辑练习' : '新建练习' }}</h3>
          <button @click="showModal = false" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5 space-y-4">
          <div v-if="!editTarget">
            <label class="block text-sm font-medium text-gray-700 mb-1.5">所属关卡</label>
            <select v-model="form.levelId" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option value="">请选择关卡</option>
              <option v-for="level in levels" :key="level.id" :value="level.id">{{ level.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">英文内容</label>
            <input v-model="form.contentEn" type="text" placeholder="如：hello" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">中文释义</label>
            <input v-model="form.contentZh" type="text" placeholder="如：你好" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">序号</label>
            <input v-model.number="form.orderIndex" type="number" min="1" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <p v-if="formError" class="text-sm text-red-500 bg-red-50 px-3 py-2 rounded-lg">{{ formError }}</p>
        </div>
        <div class="px-6 py-4 border-t flex gap-3 justify-end">
          <button @click="showModal = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleSave" :disabled="saving" class="px-5 py-2 text-sm bg-blue-600 hover:bg-blue-700 disabled:opacity-50 text-white rounded-lg transition">{{ saving ? '保存中...' : '保存' }}</button>
        </div>
      </div>
    </div>

    <!-- 批量导入弹窗 -->
    <div v-if="showBatchModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="showBatchModal = false">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-2xl">
        <div class="flex items-center justify-between px-6 py-4 border-b">
          <h3 class="text-lg font-bold text-gray-900">批量导入练习内容</h3>
          <button @click="showBatchModal = false" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5 space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">所属关卡</label>
            <select v-model="batchForm.levelId" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option value="">请选择关卡</option>
              <option v-for="level in levels" :key="level.id" :value="level.id">{{ level.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">批量内容（每行格式：英文|中文|序号）</label>
            <!-- CSV 上传 -->
            <div class="mb-2 flex items-center gap-2">
              <label class="flex items-center gap-1.5 px-3 py-1.5 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm rounded-lg cursor-pointer transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" /></svg>
                导入 CSV 文件
                <input type="file" accept=".csv" class="hidden" @change="handleCsvUpload" />
              </label>
              <span class="text-xs text-gray-400">CSV 格式：英文,中文（第一行为标题行会自动跳过）</span>
            </div>
            <textarea v-model="batchForm.content" rows="10" placeholder="示例：&#10;hello|你好|1&#10;goodbye|再见|2&#10;thank you|谢谢|3" class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none font-mono" />
            <p class="text-xs text-gray-500 mt-1">每行一条，用竖线 | 分隔英文、中文、序号（序号可省略，自动递增）</p>
          </div>
          <p v-if="batchError" class="text-sm text-red-500 bg-red-50 px-3 py-2 rounded-lg">{{ batchError }}</p>
        </div>
        <div class="px-6 py-4 border-t flex gap-3 justify-end">
          <button @click="showBatchModal = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleBatchImport" :disabled="batchSaving" class="px-5 py-2 text-sm bg-green-600 hover:bg-green-700 disabled:opacity-50 text-white rounded-lg transition">{{ batchSaving ? '导入中...' : '导入' }}</button>
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
        <p class="text-sm text-gray-600 mb-6">确认删除练习内容 <strong>{{ deleteTarget.contentEn }}</strong>？</p>
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
import { getAdminLevels, getAdminExercises, createExercise, batchCreateExercises, updateExercise, deleteExercise } from '@/api/admin'
import type { LevelDto, ExerciseDto } from '@/api/admin'

const loading = ref(false)
const exercises = ref<ExerciseDto[]>([])
const levels = ref<LevelDto[]>([])
const selectedLevelId = ref<number | null>(null)
const showModal = ref(false)
const showBatchModal = ref(false)
const saving = ref(false)
const batchSaving = ref(false)
const formError = ref('')
const batchError = ref('')
const editTarget = ref<ExerciseDto | null>(null)
const deleteTarget = ref<ExerciseDto | null>(null)

const form = reactive({ levelId: '' as number | '', contentEn: '', contentZh: '', orderIndex: 1 })
const batchForm = reactive({ levelId: '' as number | '', content: '' })

const loadExercises = async () => {
  if (!selectedLevelId.value) {
    exercises.value = []
    return
  }
  loading.value = true
  try {
    const res = await getAdminExercises(selectedLevelId.value)
    exercises.value = (res.data as any).data || res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const loadLevels = async () => {
  const res = await getAdminLevels()
  levels.value = (res.data as any).data || res.data
}

const openModal = (ex?: ExerciseDto) => {
  editTarget.value = ex || null
  form.levelId = ex?.levelId || ''
  form.contentEn = ex?.contentEn || ''
  form.contentZh = ex?.contentZh || ''
  form.orderIndex = ex?.orderIndex || 1
  formError.value = ''
  showModal.value = true
}

const openBatchModal = () => {
  batchForm.levelId = ''
  batchForm.content = ''
  batchError.value = ''
  showBatchModal.value = true
}

const openDeleteModal = (ex: ExerciseDto) => { deleteTarget.value = ex }

const handleSave = async () => {
  if (!form.contentEn.trim() || !form.contentZh.trim()) { formError.value = '请填写完整'; return }
  if (!editTarget.value && !form.levelId) { formError.value = '请选择关卡'; return }
  saving.value = true
  formError.value = ''
  try {
    if (editTarget.value) {
      await updateExercise(editTarget.value.id, { levelId: editTarget.value.levelId, contentEn: form.contentEn, contentZh: form.contentZh, orderIndex: form.orderIndex })
    } else {
      await createExercise({ levelId: form.levelId as number, contentEn: form.contentEn, contentZh: form.contentZh, orderIndex: form.orderIndex })
    }
    showModal.value = false
    loadExercises()
  } catch (e: any) {
    formError.value = e.response?.data?.message || '保存失败'
  } finally {
    saving.value = false
  }
}

const handleCsvUpload = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = (ev) => {
    const text = ev.target?.result as string
    const lines = text.split('\n').map(l => l.trim()).filter(l => l)
    const result: string[] = []
    let orderIndex = 1
    for (let i = 0; i < lines.length; i++) {
      const line = lines[i]
      // 跳过标题行（包含中文"英文"或"english"等关键词）
      if (i === 0 && (line.toLowerCase().includes('english') || line.includes('英文'))) continue
      // CSV 格式：英文,中文 或 英文,中文,序号
      const parts = line.split(',').map(p => p.replace(/^"|"$/g, '').trim())
      if (parts.length >= 2 && parts[0] && parts[1]) {
        const order = parts[2] ? parts[2] : String(orderIndex)
        result.push(`${parts[0]}|${parts[1]}|${order}`)
        orderIndex++
      }
    }
    batchForm.content = result.join('\n')
  }
  reader.readAsText(file, 'UTF-8')
  // 清空 input，允许重复选同一文件
  ;(e.target as HTMLInputElement).value = ''
}

const handleBatchImport = async () => {
  if (!batchForm.levelId) { batchError.value = '请选择关卡'; return }
  if (!batchForm.content.trim()) { batchError.value = '请输入内容'; return }
  
  const lines = batchForm.content.trim().split('\n').filter(l => l.trim())
  const items = []
  for (let i = 0; i < lines.length; i++) {
    const parts = lines[i].split('|').map(p => p.trim())
    if (parts.length < 2) {
      batchError.value = `第 ${i + 1} 行格式错误，应为：英文|中文 或 英文|中文|序号`
      return
    }
    const order = parts[2] ? parseInt(parts[2]) : i + 1
    items.push({ contentEn: parts[0], contentZh: parts[1], orderIndex: order })
  }

  batchSaving.value = true
  batchError.value = ''
  try {
    await batchCreateExercises(batchForm.levelId as number, items)
    showBatchModal.value = false
    loadExercises()
  } catch (e: any) {
    batchError.value = e.response?.data?.message || '导入失败'
  } finally {
    batchSaving.value = false
  }
}

const handleDelete = async () => {
  if (!deleteTarget.value) return
  try {
    await deleteExercise(deleteTarget.value.id)
    deleteTarget.value = null
    loadExercises()
  } catch (e: any) {
    alert(e.response?.data?.message || '删除失败')
    deleteTarget.value = null
  }
}

onMounted(async () => {
  await loadLevels()
  await loadExercises()
})
</script>
