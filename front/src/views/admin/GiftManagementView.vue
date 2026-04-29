<template>
  <div class="p-6">
    <!-- 页面标题 -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">🎁 道具管理</h1>
        <p class="text-gray-500 mt-1">管理商城道具、库存和上下架状态</p>
      </div>
      <button
        class="px-4 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors flex items-center gap-2"
        @click="openCreateModal"
      >
        <span>➕</span>
        <span>添加道具</span>
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-500"></div>
    </div>

    <!-- 道具列表 -->
    <div v-else class="bg-white rounded-xl shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">道具</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">所需积分</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">库存</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
            <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="gift in gifts" :key="gift.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">
              <div class="flex items-center gap-3">
                <img
                  :src="gift.icon"
                  :alt="gift.name"
                  class="w-10 h-10 rounded-lg object-cover bg-purple-100"
                  @error="handleImageError"
                />
                <div>
                  <div class="font-medium text-gray-900">{{ gift.name }}</div>
                  <div class="text-sm text-gray-500 truncate max-w-xs">{{ gift.description }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4">
              <span class="font-medium text-purple-600">{{ gift.pointsCost }}</span>
            </td>
            <td class="px-6 py-4">
              <span :class="gift.stock > 0 ? 'text-green-600' : 'text-red-500'">
                {{ gift.stock }}
              </span>
            </td>
            <td class="px-6 py-4">
              <span
                class="px-2 py-1 text-xs rounded-full"
                :class="gift.status === 'ON_SHELF' ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-500'"
              >
                {{ gift.status === 'ON_SHELF' ? '上架中' : '已下架' }}
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">
              {{ formatDate(gift.createdAt) }}
            </td>
            <td class="px-6 py-4 text-right">
              <div class="flex items-center justify-end gap-2">
                <button
                  class="px-3 py-1 text-sm text-blue-600 hover:bg-blue-50 rounded transition-colors"
                  @click="openEditModal(gift)"
                >
                  编辑
                </button>
                <button
                  v-if="gift.status === 'ON_SHELF'"
                  class="px-3 py-1 text-sm text-orange-600 hover:bg-orange-50 rounded transition-colors"
                  @click="handleOffShelf(gift)"
                >
                  下架
                </button>
                <button
                  v-else
                  class="px-3 py-1 text-sm text-green-600 hover:bg-green-50 rounded transition-colors"
                  @click="handleOnShelf(gift)"
                >
                  上架
                </button>
                <button
                  class="px-3 py-1 text-sm text-red-600 hover:bg-red-50 rounded transition-colors"
                  @click="handleDelete(gift)"
                >
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 空状态 -->
      <div v-if="gifts.length === 0" class="text-center py-12">
        <span class="text-4xl">📭</span>
        <p class="text-gray-500 mt-2">暂无道具</p>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="mt-6 flex justify-center gap-2">
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

    <!-- 创建/编辑弹窗 -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      @click.self="closeModal"
    >
      <div class="bg-white rounded-2xl max-w-lg w-full p-6">
        <h2 class="text-xl font-bold text-gray-900">
          {{ isEdit ? '编辑道具' : '添加道具' }}
        </h2>

        <form @submit.prevent="handleSubmit" class="mt-4 space-y-4">
          <!-- 道具名称 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">道具名称 *</label>
            <input
              v-model="form.name"
              type="text"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              placeholder="输入道具名称"
            />
          </div>

          <!-- 道具描述 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">道具描述</label>
            <textarea
              v-model="form.description"
              rows="3"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              placeholder="输入道具描述"
            ></textarea>
          </div>

          <!-- 道具图片上传 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">道具图片</label>
            <div class="flex items-center gap-4">
              <!-- 图片预览 -->
              <div class="w-20 h-20 rounded-lg bg-purple-100 flex items-center justify-center overflow-hidden">
                <img
                  v-if="imagePreview"
                  :src="imagePreview"
                  :alt="form.name"
                  class="w-full h-full object-cover"
                />
                <span v-else class="text-3xl">🎁</span>
              </div>
              
              <!-- 上传按钮 -->
              <div class="flex-1">
                <input
                  ref="fileInput"
                  type="file"
                  accept="image/*"
                  class="hidden"
                  @change="handleFileChange"
                />
                <button
                  type="button"
                  class="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
                  @click="fileInput?.click()"
                >
                  选择图片
                </button>
                <p class="text-xs text-gray-500 mt-1">支持 JPG、PNG、GIF 格式，建议尺寸 200x200</p>
              </div>
            </div>
          </div>

          <!-- 所需积分 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">所需积分 *</label>
            <input
              v-model.number="form.pointsCost"
              type="number"
              required
              min="0"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              placeholder="输入所需积分"
            />
          </div>

          <!-- 库存 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">库存数量 *</label>
            <input
              v-model.number="form.stock"
              type="number"
              required
              min="0"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              placeholder="输入库存数量"
            />
          </div>

          <!-- 操作按钮 -->
          <div class="flex gap-3 pt-4">
            <button
              type="button"
              class="flex-1 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
              @click="closeModal"
            >
              取消
            </button>
            <button
              type="submit"
              class="flex-1 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors disabled:opacity-50"
              :disabled="submitting"
            >
              {{ submitting ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  getAdminGifts,
  createGift,
  updateGift,
  deleteGift,
  onShelfGift,
  offShelfGift,
  uploadGiftImage,
  type GiftDto,
  type CreateGiftRequest,
  type UpdateGiftRequest
} from '../../api/gift'

// 道具列表
const gifts = ref<GiftDto[]>([])
const loading = ref(true)
const page = ref(0)
const totalPages = ref(0)

// 弹窗
const showModal = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)

// 表单
const form = ref<CreateGiftRequest>({
  name: '',
  description: '',
  icon: '',
  pointsCost: 100,
  stock: 10
})

// 图片上传
const fileInput = ref<HTMLInputElement | null>(null)
const imagePreview = ref<string>('')
const selectedFile = ref<File | null>(null)

// 格式化日期
const formatDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 图片加载失败处理
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="40" height="40"%3E%3Crect width="40" height="40" fill="%23E5E7EB"/%3E%3Ctext x="50%25" y="50%25" dominant-baseline="middle" text-anchor="middle" font-size="20"%3E🎁%3C/text%3E%3C/svg%3E'
}

// 文件选择
const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  
  if (!file) return

  // 校验文件类型
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }

  // 校验文件大小（2MB）
  if (file.size > 2 * 1024 * 1024) {
    alert('图片大小不能超过 2MB')
    return
  }

  selectedFile.value = file
  
  // 生成预览
  const reader = new FileReader()
  reader.onload = (e) => {
    imagePreview.value = e.target?.result as string
  }
  reader.readAsDataURL(file)
}

// 加载道具列表
const loadGifts = async (p: number = 0) => {
  loading.value = true
  try {
    const res = await getAdminGifts(p, 20)
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

// 打开创建弹窗
const openCreateModal = () => {
  isEdit.value = false
  editingId.value = null
  form.value = {
    name: '',
    description: '',
    icon: '',
    pointsCost: 100,
    stock: 10
  }
  imagePreview.value = ''
  selectedFile.value = null
  showModal.value = true
}

// 打开编辑弹窗
const openEditModal = (gift: GiftDto) => {
  isEdit.value = true
  editingId.value = gift.id
  form.value = {
    name: gift.name,
    description: gift.description,
    icon: gift.icon || '',
    pointsCost: gift.pointsCost,
    stock: gift.stock
  }
  imagePreview.value = gift.icon || ''
  selectedFile.value = null
  showModal.value = true
}

// 关闭弹窗
const closeModal = () => {
  showModal.value = false
  isEdit.value = false
  editingId.value = null
  imagePreview.value = ''
  selectedFile.value = null
}

// 提交表单
const handleSubmit = async () => {
  if (submitting.value) return

  submitting.value = true
  try {
    if (isEdit.value && editingId.value) {
      // 编辑
      const updateData: UpdateGiftRequest = {}
      if (form.value.name) updateData.name = form.value.name
      if (form.value.description) updateData.description = form.value.description
      if (form.value.icon) updateData.icon = form.value.icon
      if (form.value.pointsCost !== undefined) updateData.pointsCost = form.value.pointsCost
      if (form.value.stock !== undefined) updateData.stock = form.value.stock

      await updateGift(editingId.value, updateData)

      // 如果选择了新图片，上传图片
      if (selectedFile.value) {
        await uploadGiftImage(editingId.value, selectedFile.value)
      }
    } else {
      // 创建
      await createGift(form.value)
    }

    await loadGifts(page.value)
    closeModal()
  } catch (error: any) {
    console.error('保存失败:', error)
    alert(error.response?.data?.message || '保存失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 上架
const handleOnShelf = async (gift: GiftDto) => {
  if (!confirm(`确定要上架道具「${gift.name}」吗？`)) return

  try {
    await onShelfGift(gift.id)
    await loadGifts(page.value)
  } catch (error: any) {
    console.error('上架失败:', error)
    alert(error.response?.data?.message || '上架失败')
  }
}

// 下架
const handleOffShelf = async (gift: GiftDto) => {
  if (!confirm(`确定要下架道具「${gift.name}」吗？`)) return

  try {
    await offShelfGift(gift.id)
    await loadGifts(page.value)
  } catch (error: any) {
    console.error('下架失败:', error)
    alert(error.response?.data?.message || '下架失败')
  }
}

// 删除
const handleDelete = async (gift: GiftDto) => {
  if (!confirm(`确定要删除道具「${gift.name}」吗？此操作不可恢复。`)) return

  try {
    await deleteGift(gift.id)
    await loadGifts(page.value)
  } catch (error: any) {
    console.error('删除失败:', error)
    alert(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadGifts()
})
</script>