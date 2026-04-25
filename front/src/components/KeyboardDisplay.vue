<template>
  <div class="select-none">
    <!-- 键盘行 -->
    <div v-for="(row, rowIdx) in keyboardRows" :key="rowIdx" class="flex justify-center gap-1 mb-1">
      <!-- 行缩进 -->
      <div v-if="rowIdx === 1" class="w-6"></div>
      <div v-if="rowIdx === 2" class="w-10"></div>

      <div
        v-for="key in row"
        :key="key"
        :class="[
          'relative flex flex-col items-center justify-center rounded-lg border-2 font-bold transition-all duration-150 cursor-default',
          key === ' ' ? 'w-48 h-10 text-xs' : 'w-10 h-10 text-sm',
          getKeyClass(key)
        ]"
      >
        <span>{{ key === ' ' ? 'Space' : key.toUpperCase() }}</span>
        <!-- 锁图标 -->
        <span v-if="!isUnlocked(key)" class="absolute top-0.5 right-0.5 text-xs">🔒</span>
        <!-- 置信度小点 -->
        <span
          v-else-if="getConfidence(key) > 0"
          class="absolute bottom-0.5 text-xs leading-none"
          :class="getConfidenceDotClass(key)"
        >●</span>
      </div>
    </div>

    <!-- 图例 -->
    <div class="flex justify-center gap-4 mt-3 text-xs text-gray-500">
      <span class="flex items-center gap-1"><span class="w-3 h-3 rounded bg-green-400 inline-block"></span>已达标</span>
      <span class="flex items-center gap-1"><span class="w-3 h-3 rounded bg-yellow-400 inline-block"></span>接近达标</span>
      <span class="flex items-center gap-1"><span class="w-3 h-3 rounded bg-red-400 inline-block"></span>需要练习</span>
      <span class="flex items-center gap-1"><span class="w-3 h-3 rounded bg-gray-200 inline-block"></span>未解锁</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FingerTypingProgress } from '@/utils/fingerTyping'
import { FINGER_COLORS, getKeyStatusClass } from '@/utils/fingerTyping'

const props = defineProps<{
  progress: FingerTypingProgress
  currentKey: string
}>()

const keyboardRows = [
  ['q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'],
  ['a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';'],
  ['z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/'],
]

const spaceKey = ' '

const isUnlocked = (key: string) => props.progress.unlockedKeys.includes(key)

const getConfidence = (key: string) => props.progress.keyStats[key]?.confidence || 0

const getConfidenceDotClass = (key: string) => {
  const c = getConfidence(key)
  if (c >= 90) return 'text-green-600'
  if (c >= 60) return 'text-yellow-600'
  return 'text-red-600'
}

const getKeyClass = (key: string) => {
  if (key === props.currentKey) {
    return 'ring-2 ring-blue-500 bg-blue-500 text-white border-blue-600 scale-110 shadow-lg'
  }
  if (!isUnlocked(key)) {
    return 'bg-gray-100 text-gray-400 border-gray-200'
  }
  const c = getConfidence(key)
  const fingerColor = FINGER_COLORS[key] || 'bg-white'
  if (c >= 90) return `bg-green-400 text-white border-green-500`
  if (c >= 60) return `bg-yellow-400 text-white border-yellow-500`
  if (c > 0) return `bg-red-400 text-white border-red-500`
  return `${fingerColor} border-gray-300 text-gray-700`
}
</script>
