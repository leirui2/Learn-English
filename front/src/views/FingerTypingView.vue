<template>
  <div class="min-h-screen bg-gradient-to-br from-indigo-50 via-purple-50 to-pink-50">
    <!-- 顶部装饰 -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-purple-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-indigo-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-2000"></div>
      <div class="absolute top-1/2 left-1/2 w-80 h-80 bg-pink-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-4000"></div>
    </div>

    <div class="max-w-4xl mx-auto px-4 py-12 relative">
      <!-- 移动端提示 -->
      <div class="md:hidden bg-gradient-to-r from-yellow-50 to-orange-50 border-2 border-yellow-200 rounded-2xl p-8 text-center shadow-lg">
        <div class="text-5xl mb-4">⌨️</div>
        <h2 class="text-xl font-bold text-yellow-800 mb-3">需要物理键盘</h2>
        <p class="text-base text-yellow-700">指法训练需要使用物理键盘，建议在电脑上进行练习。</p>
      </div>

      <!-- 桌面端主内容 -->
      <div class="hidden md:block">
        <!-- 标题栏 -->
        <div class="text-center mb-8">
          <div class="inline-block mb-4">
            <div class="text-7xl animate-bounce">⌨️</div>
          </div>
          <h1 class="text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2">指法训练</h1>
          <p class="text-lg text-gray-600">从基础键位开始，逐步解锁全键盘 🚀</p>
        </div>

        <div class="flex justify-center gap-3 mb-8">
          <button
            @click="resetCurrentGroup"
            class="px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-medium rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center gap-2"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" /></svg>
            换一组
          </button>
          <button
            @click="confirmReset"
            class="px-6 py-3 bg-gradient-to-r from-red-500 to-pink-500 hover:from-red-600 hover:to-pink-600 text-white font-medium rounded-xl transition shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center gap-2"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
            重置进度
          </button>
        </div>

        <!-- 练习区域 -->
        <div class="bg-white rounded-2xl shadow-xl border border-gray-100 p-8 mb-8">
          <!-- 当前/下一个按键提示 -->
          <div class="flex items-center justify-center gap-8 mb-8">
            <div class="text-center transform hover:scale-110 transition">
              <div class="text-sm text-gray-500 mb-2 font-medium">当前按键</div>
              <div class="w-20 h-20 bg-gradient-to-br from-blue-500 to-purple-600 text-white rounded-2xl flex items-center justify-center text-3xl font-bold shadow-xl">
                {{ currentKey === ' ' ? '␣' : currentKey.toUpperCase() }}
              </div>
              <div class="text-sm text-gray-600 mt-2 font-medium">{{ FINGER_NAMES[currentKey] || '' }}</div>
            </div>
            <div class="text-gray-300 text-3xl animate-pulse">→</div>
            <div class="text-center transform hover:scale-110 transition">
              <div class="text-sm text-gray-500 mb-2 font-medium">下一个</div>
              <div class="w-20 h-20 bg-gradient-to-br from-gray-100 to-gray-200 text-gray-700 rounded-2xl flex items-center justify-center text-3xl font-bold shadow-lg">
                {{ nextKey === ' ' ? '␣' : nextKey.toUpperCase() }}
              </div>
              <div class="text-sm text-gray-600 mt-2 font-medium">{{ FINGER_NAMES[nextKey] || '' }}</div>
            </div>
          </div>

          <!-- 练习序列显示 -->
          <div
            ref="sequenceRef"
            class="font-mono text-3xl tracking-widest text-center py-6 px-8 bg-gradient-to-r from-gray-50 to-blue-50 rounded-2xl mb-4 min-h-20 flex items-center justify-center flex-wrap gap-2 cursor-pointer border-2 border-gray-100 hover:border-blue-300 transition"
            @click="focusInput"
          >
            <span
              v-for="(char, idx) in sequence"
              :key="idx"
              :class="[
                'inline-block w-10 h-12 flex items-center justify-center rounded-xl transition-all',
                idx < currentIndex ? 'text-green-500 font-bold' :
                idx === currentIndex ? 'bg-gradient-to-br from-blue-500 to-purple-600 text-white rounded-xl shadow-lg transform scale-110' :
                'text-gray-400'
              ]"
            >{{ char === ' ' ? '␣' : char.toUpperCase() }}</span>
          </div>

          <!-- 错误提示 -->
          <div v-if="showError" class="text-center text-red-500 text-base animate-bounce mb-4 font-bold">
            ✗ 按键错误！请使用正确的手指按键！
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

          <p class="text-center text-sm text-gray-500 mt-4">点击上方区域或按任意键开始练习 ⌨️</p>
        </div>

        <!-- 统计栏 -->
        <div class="grid grid-cols-4 gap-4 mb-8">
          <div class="bg-white rounded-2xl border-2 border-blue-100 p-6 text-center transform hover:scale-105 transition shadow-lg hover:shadow-xl">
            <div class="text-4xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">{{ wpm }}</div>
            <div class="text-sm text-gray-600 mt-2 font-medium">WPM</div>
          </div>
          <div class="bg-white rounded-2xl border-2 border-green-100 p-6 text-center transform hover:scale-105 transition shadow-lg hover:shadow-xl">
            <div class="text-4xl font-bold bg-gradient-to-r from-green-600 to-teal-600 bg-clip-text text-transparent">{{ accuracy }}%</div>
            <div class="text-sm text-gray-600 mt-2 font-medium">准确率</div>
          </div>
          <div class="bg-white rounded-2xl border-2 border-purple-100 p-6 text-center transform hover:scale-105 transition shadow-lg hover:shadow-xl">
            <div class="text-4xl font-bold bg-gradient-to-r from-purple-600 to-pink-600 bg-clip-text text-transparent">{{ timeDisplay }}</div>
            <div class="text-sm text-gray-600 mt-2 font-medium">时间</div>
          </div>
          <div class="bg-white rounded-2xl border-2 border-orange-100 p-6 text-center transform hover:scale-105 transition shadow-lg hover:shadow-xl">
            <div class="text-4xl font-bold bg-gradient-to-r from-orange-600 to-red-600 bg-clip-text text-transparent">{{ progress.unlockedKeys.length }}/{{ ALL_KEYS.length }}</div>
            <div class="text-sm text-gray-600 mt-2 font-medium">按键进度</div>
          </div>
        </div>

        <!-- 已解锁按键 & 下一个解锁 -->
        <div class="bg-white rounded-2xl border-2 border-gray-100 p-6 mb-8 flex items-center gap-4 shadow-lg">
          <div class="text-base text-gray-600 font-bold">已解锁：</div>
          <div class="flex gap-2 flex-wrap flex-1">
            <span
              v-for="key in progress.unlockedKeys"
              :key="key"
              class="px-3 py-1 bg-gradient-to-r from-blue-500 to-purple-500 text-white rounded-lg text-sm font-mono font-bold shadow-md"
            >{{ key === ' ' ? 'Space' : key.toUpperCase() }}</span>
          </div>
          <div v-if="nextUnlockKey" class="text-base text-gray-600 font-bold">
            下一个解锁：<span class="bg-gradient-to-r from-orange-600 to-red-600 bg-clip-text text-transparent text-lg">{{ nextUnlockKey.toUpperCase() }}</span>
          </div>
        </div>

        <!-- 可视化键盘 -->
        <div class="bg-white rounded-2xl shadow-xl border border-gray-100 p-8">
          <h2 class="text-lg font-bold text-gray-700 mb-6 flex items-center gap-2">
            <span class="text-2xl">🎹</span>
            <span>键盘热力图</span>
          </h2>
          <KeyboardDisplay :progress="progress" :current-key="currentKey" />
        </div>
        <!-- 重置确认弹窗 -->
        <div v-if="showResetConfirm" class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 px-4" @click.self="showResetConfirm = false">
          <div class="bg-white rounded-2xl shadow-2xl w-full max-w-sm p-8 text-center transform animate-bounce-in">
            <div class="text-6xl mb-4">⚠️</div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">确认重置全部进度？</h3>
            <p class="text-base text-gray-600 mb-6">所有按键的练习记录将被清空，重新从 F、J 开始。</p>
            <div class="flex gap-3 justify-center">
              <button @click="showResetConfirm = false" class="px-6 py-3 text-base text-gray-600 hover:bg-gray-100 rounded-xl transition font-medium">取消</button>
              <button @click="doReset" class="px-6 py-3 text-base bg-gradient-to-r from-red-600 to-pink-600 hover:from-red-700 hover:to-pink-700 text-white rounded-xl transition font-medium shadow-lg">确认重置</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
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
