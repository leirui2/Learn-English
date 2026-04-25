<template>
  <div class="typing-engine" @click="focusInput">
    <!-- 隐藏的输入框，用于捕获键盘事件 -->
    <input
      ref="inputRef"
      v-model="userInput"
      type="text"
      class="sr-only"
      @keydown="handleKeyDown"
      @paste.prevent
      autocomplete="off"
      autocorrect="off"
      autocapitalize="off"
      spellcheck="false"
    />

    <!-- 插槽：用于显示字符 -->
    <slot
      :char-states="charStates"
      :current-exercise="currentExercise"
      :accuracy="accuracy"
      :is-completed="isCurrentExerciseCompleted"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted, nextTick } from 'vue'
import { compareTyping, type CharState } from '@/utils/typing'

/**
 * 练习内容接口
 */
export interface Exercise {
  id: number
  contentEn: string
  contentZh: string
}

/**
 * Props
 */
interface Props {
  exercises: Exercise[]
  currentIndex?: number
}

const props = withDefaults(defineProps<Props>(), {
  currentIndex: 0
})

/**
 * Emits
 */
interface Emits {
  (e: 'exercise-completed', index: number, accuracy: number): void
  (e: 'exercise-skipped', index: number): void
  (e: 'all-completed', data: { 
    overallAccuracy: number
    totalCharsTyped: number
    errorCharsMap: Record<string, number>
  }): void
  (e: 'update:currentIndex', index: number): void
}

const emit = defineEmits<Emits>()

// 状态
const inputRef = ref<HTMLInputElement | null>(null)
const userInput = ref('')
const internalCurrentIndex = ref(props.currentIndex)

// 正确率统计（记录首次输入）
const firstInputCorrect = ref<boolean[]>([]) // 每个字符首次输入是否正确
const totalCharsTyped = ref(0) // 总共输入的字符数（包括所有练习）
const correctCharsTyped = ref(0) // 首次输入正确的字符数
const errorCharsMap = ref<Record<string, number>>({}) // 错误单词统计（单词 -> 错误次数）

// 当前练习内容
const currentExercise = computed(() => {
  return props.exercises[internalCurrentIndex.value]
})

// 目标字符串
const targetText = computed(() => {
  return currentExercise.value?.contentEn || ''
})

// 字符状态
const charStates = ref<CharState[]>([])

// 实时正确率（基于首次输入）
const accuracy = computed(() => {
  if (totalCharsTyped.value === 0) return 0
  return (correctCharsTyped.value / totalCharsTyped.value) * 100
})

// 当前练习是否完成
const isCurrentExerciseCompleted = computed(() => {
  return userInput.value.length === targetText.value.length &&
         charStates.value.every(state => state.status === 'correct')
})

/**
 * 更新字符状态
 */
const updateCharStates = () => {
  const result = compareTyping(targetText.value, userInput.value)
  charStates.value = result.charStates
}

/**
 * 处理键盘按下事件
 */
const handleKeyDown = (event: KeyboardEvent) => {
  // Enter 键：跳过当前练习
  if (event.key === 'Enter') {
    event.preventDefault()
    skipCurrentExercise()
    return
  }

  // 退格键：删除最后一个字符
  if (event.key === 'Backspace') {
    event.preventDefault()
    if (userInput.value.length > 0) {
      userInput.value = userInput.value.slice(0, -1)
    }
    return
  }

  // 只处理可打印字符（长度为 1 的字符）
  if (event.key.length === 1) {
    // 如果已经完成当前练习，不再接受输入
    if (userInput.value.length >= targetText.value.length) {
      event.preventDefault()
      return
    }

    // 记录首次输入的正确性
    const currentPosition = userInput.value.length
    if (currentPosition < targetText.value.length) {
      // 检查这个位置是否是首次输入
      if (firstInputCorrect.value[currentPosition] === undefined) {
        const isCorrect = event.key === targetText.value[currentPosition]
        firstInputCorrect.value[currentPosition] = isCorrect
        
        totalCharsTyped.value++
        if (isCorrect) {
          correctCharsTyped.value++
        }
      }
    }
  }
}

/**
 * 聚焦输入框
 */
const focusInput = () => {
  inputRef.value?.focus()
}

/**
 * 跳过当前练习
 */
const skipCurrentExercise = async () => {
  // 将当前练习的所有未输入字符标记为错误（用于正确率统计）
  for (let i = userInput.value.length; i < targetText.value.length; i++) {
    if (firstInputCorrect.value[i] === undefined) {
      firstInputCorrect.value[i] = false
      totalCharsTyped.value++
    }
  }

  // 记录跳过的单词为错误
  const word = targetText.value
  errorCharsMap.value[word] = (errorCharsMap.value[word] || 0) + 1

  // 触发跳过事件
  emit('exercise-skipped', internalCurrentIndex.value)

  // 切换到下一个练习
  if (internalCurrentIndex.value < props.exercises.length - 1) {
    // 先更新索引
    internalCurrentIndex.value++
    emit('update:currentIndex', internalCurrentIndex.value)
    
    // 重置当前练习的状态
    userInput.value = ''
    firstInputCorrect.value = []
    
    // 等待 Vue 更新 DOM（确保 targetText 已经更新）
    await nextTick()
    
    // 强制更新字符状态
    updateCharStates()
    
    // 聚焦输入框
    focusInput()
  } else {
    // 所有练习完成
    emit('all-completed', {
      overallAccuracy: accuracy.value,
      totalCharsTyped: totalCharsTyped.value,
      errorCharsMap: errorCharsMap.value
    })
  }
}

/**
 * 切换到下一个练习
 */
const moveToNextExercise = () => {
  // 计算当前练习的正确率
  const currentExerciseAccuracy = firstInputCorrect.value.length > 0
    ? (firstInputCorrect.value.filter(Boolean).length / firstInputCorrect.value.length) * 100
    : 0

  // 如果当前练习有错误，记录到错误统计中
  if (currentExerciseAccuracy < 100) {
    const word = targetText.value
    errorCharsMap.value[word] = (errorCharsMap.value[word] || 0) + 1
  }

  if (internalCurrentIndex.value < props.exercises.length - 1) {
    // 触发当前练习完成事件
    emit('exercise-completed', internalCurrentIndex.value, currentExerciseAccuracy)
    
    // 切换到下一个练习
    internalCurrentIndex.value++
    emit('update:currentIndex', internalCurrentIndex.value)
    
    // 重置当前练习的状态
    userInput.value = ''
    firstInputCorrect.value = []
    
    // 聚焦输入框
    focusInput()
  } else {
    // 所有练习完成
    emit('exercise-completed', internalCurrentIndex.value, currentExerciseAccuracy)
    emit('all-completed', {
      overallAccuracy: accuracy.value,
      totalCharsTyped: totalCharsTyped.value,
      errorCharsMap: errorCharsMap.value
    })
  }
}

/**
 * 监听用户输入变化，更新字符状态
 */
watch(userInput, () => {
  updateCharStates()
  
  // 如果当前练习完成，自动切换到下一个
  if (isCurrentExerciseCompleted.value) {
    // 延迟一小段时间，让用户看到完成状态
    setTimeout(() => {
      moveToNextExercise()
    }, 300)
  }
})

/**
 * 监听练习内容变化，重置状态
 */
watch(() => props.exercises, () => {
  userInput.value = ''
  internalCurrentIndex.value = props.currentIndex
  firstInputCorrect.value = []
  totalCharsTyped.value = 0
  correctCharsTyped.value = 0
  errorCharsMap.value = {}
  updateCharStates()
}, { deep: true })

/**
 * 监听当前练习变化，更新字符状态
 */
watch(currentExercise, () => {
  updateCharStates()
})

/**
 * 监听 currentIndex prop 变化
 */
watch(() => props.currentIndex, (newIndex) => {
  if (newIndex !== internalCurrentIndex.value) {
    internalCurrentIndex.value = newIndex
    userInput.value = ''
    firstInputCorrect.value = []
    updateCharStates()
  }
})

/**
 * 组件挂载时初始化
 */
onMounted(() => {
  updateCharStates()
  focusInput()
  
  // 监听全局点击事件，确保输入框始终聚焦
  window.addEventListener('click', focusInput)
})

/**
 * 组件卸载时清理
 */
onUnmounted(() => {
  window.removeEventListener('click', focusInput)
})

/**
 * 暴露方法给父组件
 */
defineExpose({
  focusInput,
  skipCurrentExercise,
  reset: () => {
    userInput.value = ''
    internalCurrentIndex.value = 0
    firstInputCorrect.value = []
    totalCharsTyped.value = 0
    correctCharsTyped.value = 0
    errorCharsMap.value = {}
    updateCharStates()
  }
})
</script>

<style scoped>
.typing-engine {
  cursor: text;
}

/* 屏幕阅读器专用类 */
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
}
</style>
