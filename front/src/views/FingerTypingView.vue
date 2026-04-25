<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <!-- 移动端提示 -->
    <div class="md:hidden bg-yellow-50 border border-yellow-200 rounded-xl p-6 text-center">
      <div class="text-4xl mb-3">⌨️</div>
      <h2 class="text-lg font-bold text-yellow-800 mb-2">需要物理键盘</h2>
      <p class="text-sm text-yellow-700">指法训练需要使用物理键盘，建议在电脑上进行练习。</p>
    </div>

    <!-- 桌面端主内容 -->
    <div class="hidden md:block">
      <!-- 标题栏 -->
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-bold text-gray-900">指法训练</h1>
          <p class="text-sm text-gray-500 mt-1">从基础键位开始，逐步解锁全键盘</p>
        </div>
        <div class="flex gap-2">
          <button
            @click="resetCurrentGroup"
            class="px-4 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition"
          >
            换一组
          </button>
          <button
            @click="confirmReset"
            class="px-4 py-2 text-sm bg-red-50 hover:bg-red-100 text-red-600 rounded-lg transition"
          >
            重置进度
          </button>
        </div>
      </div>

      <!-- 练习区域 -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
        <!-- 当前/下一个按键提示 -->
        <div class="flex items-center justify-center gap-8 mb-6">
          <div class="text-center">
            <div class="text-xs text-gray-500 mb-1">当前按键</div>
            <div class="w-14 h-14 bg-blue-500 text-white rounded-xl flex items-center justify-center text-2xl font-bold shadow-md">
              {{ currentKey === ' ' ? '␣' : currentKey.toUpperCase() }}
            </div>
            <div class="text-xs text-gray-500 mt-1">{{ FINGER_NAMES[currentKey] || '' }}</div>
          </div>
          <div class="text-gray-300 text-2xl">→</div>
          <div class="text-center">
            <div class="text-xs text-gray-500 mb-1">下一个</div>
            <div class="w-14 h-14 bg-gray-100 text-gray-600 rounded-xl flex items-center justify-center text-2xl font-bold">
              {{ nextKey === ' ' ? '␣' : nextKey.toUpperCase() }}
            </div>
            <div class="text-xs text-gray-500 mt-1">{{ FINGER_NAMES[nextKey] || '' }}</div>
          </div>
        </div>

        <!-- 练习序列显示 -->
        <div
          ref="sequenceRef"
          class="font-mono text-2xl tracking-widest text-center py-4 px-6 bg-gray-50 rounded-xl mb-4 min-h-16 flex items-center justify-center flex-wrap gap-1 cursor-text"
          @click="focusInput"
        >
          <span
            v-for="(char, idx) in sequence"
            :key="idx"
            :class="[
              'inline-block w-8 h-10 flex items-center justify-center rounded transition-all',
              idx < currentIndex ? 'text-green-500' :
              idx === currentIndex ? 'bg-blue-500 text-white rounded shadow' :
              'text-gray-400'
            ]"
          >{{ char === ' ' ? '␣' : char.toUpperCase() }}</span>
        </div>

        <!-- 错误提示 -->
        <div v-if="showError" class="text-center text-red-500 text-sm animate-bounce mb-2">
          ✗ 按键错误！
        </div>

        <!-- 隐藏输入框 -->
        <input
          ref="inputRef"
          class="sr-only"
          @keydown="handleKeyDown"
          @paste.prevent
          autocomplete="off"
          autocorrect="off"
          autocapitalize="off"
          spellcheck="false"
        />

        <p class="text-center text-xs text-gray-400">点击上方区域或按任意键开始练习</p>
      </div>

      <!-- 统计栏 -->
      <div class="grid grid-cols-4 gap-4 mb-6">
        <div class="bg-white rounded-xl border border-gray-100 p-4 text-center">
          <div class="text-2xl font-bold text-blue-600">{{ wpm }}</div>
          <div class="text-xs text-gray-500 mt-1">WPM</div>
        </div>
        <div class="bg-white rounded-xl border border-gray-100 p-4 text-center">
          <div class="text-2xl font-bold text-green-600">{{ accuracy }}%</div>
          <div class="text-xs text-gray-500 mt-1">准确率</div>
        </div>
        <div class="bg-white rounded-xl border border-gray-100 p-4 text-center">
          <div class="text-2xl font-bold text-purple-600">{{ timeDisplay }}</div>
          <div class="text-xs text-gray-500 mt-1">时间</div>
        </div>
        <div class="bg-white rounded-xl border border-gray-100 p-4 text-center">
          <div class="text-2xl font-bold text-orange-600">{{ progress.unlockedKeys.length }}/{{ ALL_KEYS.length }}</div>
          <div class="text-xs text-gray-500 mt-1">按键进度</div>
        </div>
      </div>

      <!-- 已解锁按键 & 下一个解锁 -->
      <div class="bg-white rounded-xl border border-gray-100 p-4 mb-6 flex items-center gap-4">
        <div class="text-sm text-gray-500">已解锁：</div>
        <div class="flex gap-1 flex-wrap">
          <span
            v-for="key in progress.unlockedKeys"
            :key="key"
            class="px-2 py-0.5 bg-blue-100 text-blue-700 rounded text-sm font-mono font-bold"
          >{{ key === ' ' ? 'Space' : key.toUpperCase() }}</span>
        </div>
        <div v-if="nextUnlockKey" class="ml-auto text-sm text-gray-500">
          下一个解锁：<span class="font-bold text-orange-600">{{ nextUnlockKey.toUpperCase() }}</span>
        </div>
      </div>

      <!-- 可视化键盘 -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <h2 class="text-sm font-semibold text-gray-500 mb-4">键盘热力图</h2>
        <KeyboardDisplay :progress="progress" :current-key="currentKey" />
      </div>
    </div>

    <!-- 重置确认弹窗 -->
    <div v-if="showResetConfirm" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="showResetConfirm = false">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-sm p-6 text-center">
        <div class="text-4xl mb-3">⚠️</div>
        <h3 class="text-lg font-bold text-gray-900 mb-2">确认重置全部进度？</h3>
        <p class="text-sm text-gray-600 mb-6">所有按键的练习记录将被清空，重新从 F、J 开始。</p>
        <div class="flex gap-3 justify-center">
          <button @click="showResetConfirm = false" class="px-5 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="doReset" class="px-5 py-2 text-sm bg-red-600 hover:bg-red-700 text-white rounded-lg transition">确认重置</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import KeyboardDisplay from '@/components/KeyboardDisplay.vue'
import {
  loadProgress, saveProgress, resetProgress,
  generateSequence, recordKeyInput, checkUnlock,
  ALL_KEYS, UNLOCK_GROUPS, FINGER_NAMES,
  type FingerTypingProgress
} from '@/utils/fingerTyping'

const inputRef = ref<HTMLInputElement | null>(null)
const sequenceRef = ref<HTMLDivElement | null>(null)
const progress = ref<FingerTypingProgress>(loadProgress())
const sequence = ref<string[]>([])
const currentIndex = ref(0)
const showError = ref(false)
const showResetConfirm = ref(false)

// 统计
const totalTyped = ref(0)
const correctTyped = ref(0)
const startTime = ref<number | null>(null)
const elapsed = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

const currentKey = computed(() => sequence.value[currentIndex.value] || 'f')
const nextKey = computed(() => sequence.value[currentIndex.value + 1] || '')

const wpm = computed(() => {
  if (!startTime.value || elapsed.value === 0) return 0
  return Math.round(correctTyped.value / 5 / (elapsed.value / 60))
})

const accuracy = computed(() => {
  if (totalTyped.value === 0) return 100
  return Math.round(correctTyped.value / totalTyped.value * 100)
})

const timeDisplay = computed(() => {
  const m = Math.floor(elapsed.value / 60)
  const s = elapsed.value % 60
  return `${m}:${s.toString().padStart(2, '0')}`
})

const nextUnlockKey = computed(() => {
  const unlocked = progress.value.unlockedKeys
  const nextGroup = UNLOCK_GROUPS.find(group => group.some(k => !unlocked.includes(k)))
  return nextGroup ? nextGroup.find(k => !unlocked.includes(k)) || null : null
})

const focusInput = () => inputRef.value?.focus()

const startTimer = () => {
  if (timer) return
  startTime.value = Date.now()
  timer = setInterval(() => { elapsed.value++ }, 1000)
}

const handleKeyDown = (e: KeyboardEvent) => {
  // 忽略修饰键
  if (e.ctrlKey || e.altKey || e.metaKey) return
  if (e.key.length > 1 && e.key !== ' ') return

  const typed = e.key === ' ' ? ' ' : e.key.toLowerCase()
  const expected = currentKey.value

  startTimer()
  totalTyped.value++

  if (typed === expected) {
    correctTyped.value++
    showError.value = false

    // 记录正确输入
    progress.value = recordKeyInput(progress.value, expected, true)
    progress.value = checkUnlock(progress.value)
    saveProgress(progress.value)

    currentIndex.value++

    // 序列完成，生成新序列
    if (currentIndex.value >= sequence.value.length) {
      generateNewSequence()
    }
  } else {
    // 记录错误输入
    progress.value = recordKeyInput(progress.value, expected, false)
    saveProgress(progress.value)
    showError.value = true
    setTimeout(() => { showError.value = false }, 500)
  }
}

const generateNewSequence = () => {
  const seq = generateSequence(progress.value, 25)
  sequence.value = seq.split('')
  currentIndex.value = 0
}

const resetCurrentGroup = () => {
  generateNewSequence()
  totalTyped.value = 0
  correctTyped.value = 0
  startTime.value = null
  elapsed.value = 0
  if (timer) { clearInterval(timer); timer = null }
  focusInput()
}

const confirmReset = () => { showResetConfirm.value = true }

const doReset = () => {
  progress.value = resetProgress()
  showResetConfirm.value = false
  resetCurrentGroup()
}

onMounted(() => {
  generateNewSequence()
  focusInput()
  window.addEventListener('click', focusInput)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
  window.removeEventListener('click', focusInput)
})
</script>

<style scoped>
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
