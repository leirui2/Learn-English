<template>
  <div class="char-display">
    <!-- 字符容器 -->
    <div
      :class="[
        'char-container',
        isMobile ? 'char-container-mobile' : 'char-container-desktop'
      ]"
    >
      <span
        v-for="(charState, index) in charStates"
        :key="index"
        :class="[
          'char',
          `char-${charState.status}`,
          charState.status === 'current' && 'char-current-blink'
        ]"
      >
        {{ charState.char === ' ' ? '\u00A0' : charState.char }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import type { CharState } from '@/utils/typing'

/**
 * Props
 */
interface Props {
  charStates: CharState[]
}

defineProps<Props>()

// 检测是否为移动端
const isMobile = ref(false)

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.char-display {
  width: 100%;
}

.char-container {
  font-family: 'Courier New', Courier, monospace;
  line-height: 1.8;
  letter-spacing: 0.05em;
}

/* 桌面端：多行展示 */
.char-container-desktop {
  font-size: 1.5rem;
  word-wrap: break-word;
  white-space: pre-wrap;
}

/* 移动端：单行横向滚动 */
.char-container-mobile {
  font-size: 1.25rem;
  white-space: nowrap;
  overflow-x: auto;
  overflow-y: hidden;
  padding-bottom: 0.5rem;
  
  /* 滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #cbd5e0 #f7fafc;
}

.char-container-mobile::-webkit-scrollbar {
  height: 6px;
}

.char-container-mobile::-webkit-scrollbar-track {
  background: #f7fafc;
  border-radius: 3px;
}

.char-container-mobile::-webkit-scrollbar-thumb {
  background: #cbd5e0;
  border-radius: 3px;
}

.char-container-mobile::-webkit-scrollbar-thumb:hover {
  background: #a0aec0;
}

/* 字符基础样式 */
.char {
  display: inline-block;
  padding: 0.125rem 0.25rem;
  border-radius: 0.25rem;
  transition: all 0.15s ease;
}

/* 正确的字符：绿色 */
.char-correct {
  color: #10b981;
  background-color: #d1fae5;
}

/* 错误的字符：红色 */
.char-incorrect {
  color: #ef4444;
  background-color: #fee2e2;
  font-weight: 600;
}

/* 待输入的字符：灰色 */
.char-pending {
  color: #9ca3af;
}

/* 当前字符：蓝色边框 + 闪烁动画 */
.char-current {
  color: #3b82f6;
  background-color: #dbeafe;
  border: 2px solid #3b82f6;
  font-weight: 600;
}

/* 当前字符闪烁动画 */
.char-current-blink {
  animation: blink 1s ease-in-out infinite;
}

@keyframes blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

/* 空格字符特殊处理 */
.char:has(> .space) {
  min-width: 0.5rem;
}
</style>
