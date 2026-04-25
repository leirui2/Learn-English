<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <!-- 页面标题 -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900">关卡列表</h1>
      <p class="mt-2 text-gray-600">选择难度分类，开始你的英语打字练习</p>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center items-center py-20">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
    </div>

    <!-- 错误提示 -->
    <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 rounded-lg">
      <p class="text-sm text-red-600">{{ error }}</p>
    </div>

    <!-- 分类 Tab -->
    <div v-else class="space-y-8">
        <!-- Tab 导航 -->
        <div class="border-b border-gray-200">
          <nav class="-mb-px flex space-x-8 overflow-x-auto" aria-label="Tabs">
            <button
              v-for="(item, index) in categoriesWithLevels"
              :key="item.category.id"
              @click="activeTab = index"
              :class="[
                activeTab === index
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300',
                'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition'
              ]"
            >
              {{ item.category.name }}
            </button>
          </nav>
        </div>

        <!-- 关卡网格 -->
        <div
          v-if="categoriesWithLevels[activeTab]"
          class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
        >
          <div
            v-for="level in categoriesWithLevels[activeTab].levels"
            :key="level.id"
            @click="handleLevelClick(level)"
            :class="[
              'relative p-6 rounded-lg border-2 transition cursor-pointer',
              level.unlocked
                ? 'border-blue-200 bg-white hover:border-blue-400 hover:shadow-md'
                : 'border-gray-200 bg-gray-50 cursor-not-allowed opacity-60'
            ]"
          >
            <!-- 完成标记 -->
            <div
              v-if="level.completed"
              class="absolute top-4 right-4 w-8 h-8 bg-green-500 rounded-full flex items-center justify-center"
            >
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>

            <!-- 锁定图标 -->
            <div
              v-else-if="!level.unlocked"
              class="absolute top-4 right-4 w-8 h-8 bg-gray-400 rounded-full flex items-center justify-center"
            >
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>

            <!-- 关卡信息 -->
            <div class="pr-10">
              <h3 class="text-lg font-semibold text-gray-900 mb-2">
                {{ level.name }}
              </h3>
              <p class="text-sm text-gray-600 mb-4">
                {{ level.description }}
              </p>

              <!-- 最佳成绩 -->
              <div v-if="level.bestAccuracy !== undefined && level.bestAccuracy > 0" class="flex items-center text-sm text-gray-500">
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                最佳正确率：{{ level.bestAccuracy.toFixed(1) }}%
              </div>

              <!-- 状态标签 -->
              <div class="mt-3">
                <span
                  v-if="level.completed"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800"
                >
                  已完成
                </span>
                <span
                  v-else-if="level.unlocked"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                >
                  可练习
                </span>
                <span
                  v-else
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800"
                >
                  未解锁
                </span>
              </div>
            </div>
          </div>
        </div>

      <!-- 空状态 -->
      <div
        v-if="categoriesWithLevels[activeTab]?.levels.length === 0"
        class="text-center py-12"
      >
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
        </svg>
        <h3 class="mt-2 text-sm font-medium text-gray-900">暂无关卡</h3>
        <p class="mt-1 text-sm text-gray-500">该分类下还没有关卡</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getLevels, type CategoryWithLevels } from '@/api/level'

const router = useRouter()

// 状态
const loading = ref(true)
const error = ref('')
const categoriesWithLevels = ref<CategoryWithLevels[]>([])
const activeTab = ref(0)

/**
 * 加载关卡列表
 */
const loadLevels = async () => {
  loading.value = true
  error.value = ''

  try {
    const response = await getLevels()
    //console.log('完整响应:', response)
    //console.log('response.data:', response.data)
    
    // 后端返回的数据结构：ApiResult<CategoryDto[]>
    // response.data = { code: 200, message: "success", data: [...] }
    const data = (response.data as any).data
    
    // 将后端返回的分类数据转换为 CategoryWithLevels 格式
    categoriesWithLevels.value = data.map((item: any) => ({
      category: {
        id: item.id,
        name: item.name,
        description: item.description,
        difficulty: item.difficulty
      },
      levels: item.levels || []
    }))
    
    //console.log('categoriesWithLevels 赋值后:', categoriesWithLevels.value)
    //console.log('categoriesWithLevels 长度:', categoriesWithLevels.value?.length)
    
    // 检查第一个分类的第一个关卡
    // if (categoriesWithLevels.value.length > 0 && categoriesWithLevels.value[0].levels.length > 0) {
    //   console.log('第一个关卡数据:', categoriesWithLevels.value[0].levels[0])
    // }
  } catch (err: any) {
    console.error('捕获错误:', err)
    error.value = err.response?.data?.message || '加载关卡列表失败，请稍后重试'
  } finally {
    loading.value = false
    console.log('loading 状态:', loading.value)
  }
}

/**
 * 处理关卡点击
 */
const handleLevelClick = (level: any) => {
  console.log('点击关卡:', level)
  console.log('关卡是否解锁:', level.unlocked)
  
  if (!level.unlocked) {
    console.log('关卡未解锁，无法进入')
    return
  }
  
  console.log('跳转到关卡详情:', `/levels/${level.id}`)
  router.push(`/levels/${level.id}`)
}

// 组件挂载时加载数据
onMounted(() => {
  loadLevels()
})
</script>
