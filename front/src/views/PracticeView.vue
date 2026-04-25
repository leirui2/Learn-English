<template>
  <div class="min-h-screen bg-gradient-to-br from-green-50 via-teal-50 to-blue-50">
    <!-- 顶部装饰 -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-teal-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-green-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-2000"></div>
      <div class="absolute top-1/2 left-1/2 w-80 h-80 bg-blue-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-4000"></div>
    </div>

    <div class="max-w-4xl mx-auto px-4 py-12 relative">
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
            返回关卡详情
          </button>
        </div>
      </div>

      <!-- 语音选择弹窗 -->
      <div v-if="showSpeakDialog" class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex items-center justify-center z-50" @click.self="confirmSpeakSetting(true)">
        <div class="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full mx-4 transform animate-bounce-in">
          <div class="text-center">
            <div class="text-6xl mb-4">🔊</div>
            <h2 class="text-2xl font-bold text-gray-900 mb-3">开启语音播报？</h2>
            <p class="text-gray-600 mb-6">练习时将自动播放单词/短语读音</p>
            
            <!-- 倒计时 -->
            <div class="mb-6">
              <div class="text-5xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent mb-2">{{ speakDialogCountdown }}</div>
              <div class="text-sm text-gray-500">秒后自动开启</div>
            </div>

            <!-- 按钮组 -->
            <div class="flex gap-3">
              <button 
                @click="confirmSpeakSetting(false)" 
                class="flex-1 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium rounded-xl transition transform hover:scale-105"
              >
                关闭语音
              </button>
              <button 
                @click="confirmSpeakSetting(true)" 
                class="flex-1 py-3 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold rounded-xl transition shadow-lg transform hover:scale-105"
              >
                开启语音 🔊
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 打字练习界面 -->
      <div v-else-if="levelDetail" class="space-y-6">
        <!-- 顶部信息栏 -->
        <div class="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
          <div class="flex justify-between items-center">
            <div>
              <h1 class="text-2xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">{{ levelDetail.name }}</h1>
              <p class="text-sm text-gray-600 mt-2 font-medium">
                📝 练习进度：<span class="text-blue-600 font-bold">{{ currentExerciseIndex + 1 }}</span> / {{ levelDetail.exercises.length }}
              </p>
            </div>
            <div class="text-right">
              <div class="text-3xl font-bold bg-gradient-to-r from-green-600 to-teal-600 bg-clip-text text-transparent">{{ accuracy.toFixed(1) }}%</div>
              <div class="text-xs text-gray-600 mt-1 font-medium">正确率</div>
            </div>
          </div>
        </div>

        <!-- 中文提示 + 读音控制 -->
        <div class="bg-gradient-to-r from-blue-50 to-purple-50 rounded-2xl p-6 border border-blue-100 shadow-md">
          <div class="flex items-center justify-between">
            <div class="text-center flex-1">
              <p class="text-sm text-gray-600 mb-2 font-medium">📖 中文释义</p>
              <p class="text-xl text-gray-900 font-bold">{{ currentExercise?.contentZh }}</p>
            </div>
          <div class="flex items-center gap-2 ml-4">
            <!-- 手动播放按钮 -->
            <button
              @click="speakCurrent"
              class="p-2 bg-blue-100 hover:bg-blue-200 text-blue-600 rounded-lg transition"
              title="播放读音"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.536 8.464a5 5 0 010 7.072M12 6v12m0 0l-3-3m3 3l3-3M9 9H5a2 2 0 00-2 2v2a2 2 0 002 2h4l5 5V4L9 9z" />
              </svg>
            </button>
            <!-- 自动播放开关 -->
            <button
              @click="autoSpeak = !autoSpeak"
              :class="['p-2 rounded-lg transition text-xs font-medium flex items-center gap-1', autoSpeak ? 'bg-green-100 text-green-700 hover:bg-green-200' : 'bg-gray-100 text-gray-500 hover:bg-gray-200']"
              :title="autoSpeak ? '关闭自动播放' : '开启自动播放'"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              {{ autoSpeak ? '自动' : '手动' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 打字区域 -->
      <div class="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
        <TypingEngine
          v-if="!showResult"
          ref="typingEngineRef"
          :exercises="levelDetail.exercises"
          :current-index="currentExerciseIndex"
          @exercise-completed="handleExerciseCompleted"
          @exercise-skipped="handleExerciseSkipped"
          @all-completed="handleAllCompleted"
          @update:current-index="currentExerciseIndex = $event"
        >
          <template #default="{ charStates, accuracy: currentAccuracy }">
            <CharDisplay :char-states="charStates" />
            <div class="mt-4 text-center">
              <p class="text-sm text-gray-600">点击任意位置开始输入</p>
              <p class="text-xs text-gray-500 mt-1">提示：支持退格键修改，禁止粘贴</p>
              <p class="text-xs text-blue-600 mt-1">按 Enter 键或点击下方按钮跳过当前单词</p>
            </div>
            <!-- 更新本地 accuracy -->
            <div class="hidden">{{ updateAccuracy(currentAccuracy) }}</div>
          </template>
        </TypingEngine>

        <!-- 跳过按钮 -->
        <div v-if="!showResult" class="mt-6 flex justify-center">
          <button
            @click="handleSkipClick"
            class="px-6 py-2 bg-yellow-500 hover:bg-yellow-600 text-white font-medium rounded-lg transition shadow-md hover:shadow-lg flex items-center gap-2"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 5l7 7-tt7 7M5 5l7 7-7 7" />
            </svg>
            跳过 (Enter)
          </button>
        </div>
        </div>
      </div>

      <!-- 烟花效果 -->
      <FireworksEffect v-if="showFireworks" />

      <!-- 结果弹窗 -->
      <div
        v-if="showResult"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        @click.self="closeResult"
      >
        <div class="bg-white rounded-lg shadow-xl p-8 max-w-md w-full mx-4">
          <div class="text-center">
            <!-- 成功图标 -->
            <div class="mx-auto w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mb-4">
              <svg class="w-10 h-10 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>

            <h2 class="text-2xl font-bold text-gray-900 mb-2">练习完成！</h2>
            
            <!-- 统计信息 -->
            <div class="space-y-3 my-6">
              <div class="flex justify-between items-center py-2 border-b">
                <span class="text-gray-600">正确率</span>
                <span class="text-xl font-bold text-blue-600">{{ finalAccuracy.toFixed(1) }}%</span>
              </div>
              <div class="flex justify-between items-center py-2 border-b">
                <span class="text-gray-600">用时</span>
                <span class="text-xl font-bold text-gray-900">{{ formatTime(elapsedTime) }}</span>
              </div>
              <div class="flex justify-between items-center py-2 border-b">
                <span class="text-gray-600">获得积分</span>
                <span class="text-xl font-bold text-green-600">{{ earnedScore }}</span>
              </div>
            </div>

            <!-- 解锁提示 -->
            <div v-if="finalAccuracy >= 80" class="bg-green-50 border border-green-200 rounded-lg p-3 mb-4">
              <p class="text-sm text-green-800">
                🎉 恭喜通关！正确率达到 80%！
              </p>
              <p v-if="unlockedNext && nextLevelId" class="text-sm text-green-700 mt-1">
                已解锁下一关！
              </p>
            </div>
            <div v-else class="bg-yellow-50 border border-yellow-200 rounded-lg p-3 mb-4">
              <p class="text-sm text-yellow-800">
                正确率未达到 80%，继续加油！
              </p>
            </div>

            <!-- 操作按钮 -->
            <div class="flex gap-3">
              <button
                @click="retryLevel"
                class="flex-1 px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-700 font-medium rounded-lg transition"
              >
                重新练习
              </button>
              <button
                v-if="finalAccuracy >= 80 && nextLevelId"
                @click="goToNextLevel"
                class="flex-1 px-4 py-2 bg-green-600 hover:bg-green-700 text-white font-medium rounded-lg transition"
              >
                进入下一关 →
              </button>
              <button
                @click="backToLevels"
                class="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition"
              >
                返回关卡
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getLevelDetail, completeLevel, type LevelDetail } from '@/api/level'
import TypingEngine from '@/components/TypingEngine.vue'
import CharDisplay from '@/components/CharDisplay.vue'
import FireworksEffect from '@/components/FireworksEffect.vue'

const route = useRoute()
const router = useRouter()

// 状态
const loading = ref(true)
const error = ref('')
const levelDetail = ref<LevelDetail | null>(null)
const currentExerciseIndex = ref(0)
const accuracy = ref(0)
const showResult = ref(false)
const showFireworks = ref(false)
const startTime = ref(0)
const elapsedTime = ref(0)
const finalAccuracy = ref(0)
const earnedScore = ref(0)
const unlockedNext = ref(false)
const nextLevelId = ref<number | null>(null)
const errorCharsMap = ref<Record<string, number>>({})  // 错误字符统计
const totalCharsTyped = ref(0)  // 总输入字符数
const typingEngineRef = ref<InstanceType<typeof TypingEngine> | null>(null)

// 语音播放
const autoSpeak = ref(true)
const showSpeakDialog = ref(false)
const speakDialogCountdown = ref(5)
let speakDialogTimer: ReturnType<typeof setInterval> | null = null

// 当前练习
const currentExercise = computed(() => {
    return levelDetail.value?.exercises[currentExerciseIndex.value]
})

const speakCurrent = () => {
  const text = currentExercise.value?.contentEn
  if (!text || !window.speechSynthesis) return
  window.speechSynthesis.cancel()
  const utterance = new SpeechSynthesisUtterance(text)
  utterance.lang = 'en-US'
  utterance.rate = 0.9
  window.speechSynthesis.speak(utterance)
}

// 监听当前练习变化，自动播放
watch(currentExercise, (exercise) => {
  if (autoSpeak.value && exercise?.contentEn) {
    setTimeout(() => speakCurrent(), 500)
  }
})



/**
 * 更新正确率（从子组件传递）
 */
const updateAccuracy = (value: number) => {
  accuracy.value = value
  return '' // 返回空字符串，避免在模板中显示
}

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
    // 加载完成后显示语音选择弹窗
    if (levelDetail.value) {
      showSpeakDialog.value = true
      speakDialogCountdown.value = 5
      
      // 启动5秒倒计时
      if (speakDialogTimer) clearInterval(speakDialogTimer)
      speakDialogTimer = setInterval(() => {
        speakDialogCountdown.value--
        if (speakDialogCountdown.value <= 0) {
          clearInterval(speakDialogTimer!)
          // 5秒后默认开启语音，开始练习
          autoSpeak.value = true
          showSpeakDialog.value = false
          startPractice()
        }
      }, 1000)
    }
  }
}

const confirmSpeakSetting = (enabled: boolean) => {
  if (speakDialogTimer) {
    clearInterval(speakDialogTimer)
    speakDialogTimer = null
  }
  autoSpeak.value = enabled
  showSpeakDialog.value = false
  startPractice()
}

const startPractice = () => {
  // 记录开始时间
  startTime.value = Date.now()
}

/**
 * 处理单个练习完成
 */
const handleExerciseCompleted = (index: number, exerciseAccuracy: number) => {
  console.log(`练习 ${index + 1} 完成，正确率：${exerciseAccuracy.toFixed(1)}%`)
}

/**
 * 处理练习跳过
 */
const handleExerciseSkipped = (index: number) => {
  console.log(`练习 ${index + 1} 已跳过`)
}

/**
 * 处理跳过按钮点击
 */
const handleSkipClick = () => {
  typingEngineRef.value?.skipCurrentExercise()
}

/**
 * 处理所有练习完成
 */
const handleAllCompleted = async (data: { 
  overallAccuracy: number
  totalCharsTyped: number
  errorCharsMap: Record<string, number>
}) => {
  // 计算用时
  elapsedTime.value = Date.now() - startTime.value
  finalAccuracy.value = data.overallAccuracy
  totalCharsTyped.value = data.totalCharsTyped
  errorCharsMap.value = data.errorCharsMap

  // 提交结果到后端
  if (levelDetail.value) {
    try {
      console.log('提交数据:', {
        accuracy: finalAccuracy.value,
        timeMs: elapsedTime.value,
        charactersTyped: totalCharsTyped.value,
        errorChars: errorCharsMap.value
      })

      const response = await completeLevel(levelDetail.value.id, {
        accuracy: finalAccuracy.value,
        timeMs: elapsedTime.value,
        charactersTyped: totalCharsTyped.value,
        errorChars: errorCharsMap.value
      })

      console.log('后端响应:', response.data)

      const result = response.data.data
      earnedScore.value = result.score
      unlockedNext.value = result.nextLevelUnlocked || false
      nextLevelId.value = result.nextLevelId || null
    } catch (err: any) {
      console.error('提交结果失败', err)
      // 即使提交失败，也显示本地结果
      earnedScore.value = 0
      unlockedNext.value = false
    }
  }

  // 显示结果弹窗
  if (finalAccuracy.value >= 80) {
    showFireworks.value = true
    setTimeout(() => {
      showFireworks.value = false
      showResult.value = true
    }, 2500)
  } else {
    showResult.value = true
  }
}

/**
 * 格式化时间
 */
const formatTime = (ms: number): string => {
  const seconds = Math.floor(ms / 1000)
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60

  if (minutes > 0) {
    return `${minutes}分${remainingSeconds}秒`
  }
  return `${remainingSeconds}秒`
}

/**
 * 重新练习
 */
const retryLevel = () => {
  showResult.value = false
  showFireworks.value = false
  currentExerciseIndex.value = 0
  accuracy.value = 0
  startTime.value = Date.now()
  errorCharsMap.value = {}
  totalCharsTyped.value = 0
}

/**
 * 进入下一关
 */
const goToNextLevel = () => {
  if (nextLevelId.value) {
    router.push(`/levels/${nextLevelId.value}`)
  }
}

/**
 * 返回关卡列表
 */
const backToLevels = () => {
  router.push('/levels')
}

/**
 * 关闭结果弹窗
 */
const closeResult = () => {
  // 点击背景不关闭，只能通过按钮关闭
}

// 组件挂载时加载数据
onMounted(() => {
  loadLevelDetail()
})

// 组件卸载时清理定时器
onUnmounted(() => {
  if (speakDialogTimer) clearInterval(speakDialogTimer)
})
</script>

<style scoped>
@keyframes blob {
  0% {
    transform: translate(0px, 0px) scale(1);
  }
  33% {
    transform: translate(30px, -50px) scale(1.1);
  }
  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }
  100% {
    transform: translate(0px, 0px) scale(1);
  }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}

.animation-delay-4000 {
  animation-delay: 4s;
}

@keyframes bounce-in {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
  70% {
    transform: scale(0.9);
  }
  100% {
    transform: scale(1);
  }
}

.animate-bounce-in {
  animation: bounce-in 0.5s ease-out;
}
</style>
