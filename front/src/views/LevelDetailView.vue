<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center items-center py-20">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
    </div>

    <!-- 错误提示 -->
    <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 rounded-lg">
      <p class="text-sm text-red-600">{{ error }}</p>
      <button
        @click="$router.back()"
        class="mt-4 text-sm text-blue-600 hover:text-blue-500"
      >
        返回关卡列表
      </button>
    </div>

    <!-- 关卡详情 -->
    <div v-else-if="levelDetail" class="space-y-6">
      <!-- 返回按钮 -->
      <button
        @click="$router.back()"
        class="flex items-center text-gray-600 hover:text-gray-900 transition"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
        返回关卡列表
      </button>

      <!-- 关卡标题 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold text-gray-900 mb-2">
          {{ levelDetail.name }}
        </h1>
        <p class="text-gray-600 mb-4">
          {{ levelDetail.description }}
        </p>

        <!-- 关卡统计 -->
        <div class="flex flex-wrap gap-4 text-sm">
          <div class="flex items-center text-gray-600">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            练习内容：{{ levelDetail.exercises.length }} 条
          </div>
          <div v-if="levelDetail.bestAccuracy !== undefined && levelDetail.bestAccuracy > 0" class="flex items-center text-gray-600">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            最佳正确率：{{ levelDetail.bestAccuracy.toFixed(1) }}%
          </div>
        </div>
      </div>

      <!-- 练习内容预览 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">练习内容预览</h2>
        
        <div class="space-y-4 max-h-96 overflow-y-auto">
          <div
            v-for="(exercise, index) in levelDetail.exercises"
            :key="exercise.id"
            class="p-4 border border-gray-200 rounded-lg hover:border-blue-300 transition"
          >
            <div class="flex items-start">
              <span class="flex-shrink-0 w-8 h-8 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center text-sm font-medium mr-3">
                {{ index + 1 }}
              </span>
              <div class="flex-1">
                <p class="text-lg font-medium text-gray-900 mb-2">
                  {{ exercise.contentEn }}
                </p>
                <p class="text-sm text-gray-600">
                  {{ exercise.contentZh }}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 开始练习按钮 -->
      <div class="flex justify-center">
        <button
          @click="startPractice"
          class="px-8 py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition shadow-md hover:shadow-lg"
        >
          开始练习
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getLevelDetail, type LevelDetail } from '@/api/level'

const route = useRoute()
const router = useRouter()

// 状态
const loading = ref(true)
const error = ref('')
const levelDetail = ref<LevelDetail | null>(null)

/**
 * 加载关卡详情
 */
const loadLevelDetail = async () => {
  loading.value = true
  error.value = ''

  try {
    const levelId = Number(route.params.id)
    if (isNaN(levelId)) {
      error.value = '无效的关卡 ID'
      return
    }

    const response = await getLevelDetail(levelId)
    levelDetail.value = response.data.data
  } catch (err: any) {
    if (err.response?.status === 403) {
      error.value = '该关卡尚未解锁，请先完成前面的关卡'
    } else {
      error.value = err.response?.data?.message || '加载关卡详情失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}

/**
 * 开始练习
 */
const startPractice = () => {
  if (levelDetail.value) {
    router.push(`/practice/${levelDetail.value.id}`)
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadLevelDetail()
})
</script>
