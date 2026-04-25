<template>
  <div class="min-h-screen bg-gradient-to-br from-green-50 via-teal-50 to-blue-50">
    <div class="max-w-5xl mx-auto px-4 py-12">
      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center items-center py-20">
        <div class="relative">
          <div class="animate-spin rounded-full h-16 w-16 border-4 border-blue-200"></div>
          <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 absolute top-0 left-0"></div>
        </div>
      </div>

      <!-- 错误提示 -->
      <div v-else-if="error" class="bg-white rounded-2xl shadow-xl p-8 border border-red-100">
        <div class="text-center">
          <div class="text-6xl mb-4">❌</div>
          <p class="text-lg text-red-600 mb-6">{{ error }}</p>
          <button
            @click="$router.back()"
            class="px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold rounded-xl transition shadow-lg hover:shadow-xl"
          >
            返回关卡列表
          </button>
        </div>
      </div>

      <!-- 关卡详情 -->
      <div v-else-if="levelDetail" class="space-y-8">
        <!-- 返回按钮 -->
        <button
          @click="$router.back()"
          class="flex items-center text-gray-600 hover:text-blue-600 transition group"
        >
          <svg class="w-5 h-5 mr-2 transform group-hover:-translate-x-1 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          <span class="font-medium">返回关卡列表</span>
        </button>

        <!-- 关卡标题 -->
        <div class="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <div class="flex items-start gap-4 mb-6">
            <div class="text-6xl">🎯</div>
            <div class="flex-1">
              <h1 class="text-3xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent mb-3">
                {{ levelDetail.name }}
              </h1>
              <p class="text-gray-600 text-lg leading-relaxed">
                {{ levelDetail.description }}
              </p>
            </div>
          </div>

          <!-- 关卡统计 -->
          <div class="flex flex-wrap gap-4">
            <div class="flex items-center bg-blue-50 rounded-xl px-5 py-3">
              <div class="text-2xl mr-3">📝</div>
              <div>
                <div class="text-xs text-gray-500">练习内容</div>
                <div class="text-xl font-bold text-blue-600">{{ levelDetail.exercises.length }} 条</div>
              </div>
            </div>
            <div v-if="levelDetail.bestAccuracy !== undefined && levelDetail.bestAccuracy > 0" class="flex items-center bg-green-50 rounded-xl px-5 py-3">
              <div class="text-2xl mr-3">🏆</div>
              <div>
                <div class="text-xs text-gray-500">最佳正确率</div>
                <div class="text-xl font-bold text-green-600">{{ levelDetail.bestAccuracy.toFixed(1) }}%</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 练习内容预览 -->
        <div class="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <h2 class="text-2xl font-bold text-gray-900 mb-6 flex items-center gap-2">
            <span>📚</span>
            <span>练习内容预览</span>
          </h2>
          
          <div class="space-y-4 max-h-96 overflow-y-auto pr-2">
            <div
              v-for="(exercise, index) in levelDetail.exercises"
              :key="exercise.id"
              class="p-5 border-2 border-gray-100 rounded-xl hover:border-blue-300 hover:shadow-lg transition bg-gradient-to-r from-white to-blue-50"
            >
              <div class="flex items-start gap-4">
                <span class="flex-shrink-0 w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-500 text-white rounded-full flex items-center justify-center text-sm font-bold shadow-lg">
                  {{ index + 1 }}
                </span>
                <div class="flex-1">
                  <p class="text-lg font-semibold text-gray-900 mb-2">
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
            class="px-10 py-4 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold text-lg rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center gap-3"
          >
            <span>🚀</span>
            <span>开始练习</span>
          </button>
        </div>
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
