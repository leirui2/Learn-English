<template>
  <div class="max-w-4xl mx-auto px-4 py-8">

    <!-- 配置页 -->
    <div v-if="phase === 'setup'">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">⚡ 天梯挑战</h1>
        <p class="text-gray-500">随机内容，极速打字，冲击排行榜</p>
      </div>

      <!-- 内容类型 -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-4">
        <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">内容类型</h2>
        <div class="grid grid-cols-2 gap-4">
          <button
            v-for="t in contentTypes"
            :key="t.value"
            @click="selectedType = t.value"
            :class="['p-5 rounded-xl border-2 transition text-left', selectedType === t.value ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-gray-300']"
          >
            <div class="text-2xl mb-2">{{ t.icon }}</div>
            <div class="font-bold text-gray-900">{{ t.label }}</div>
            <div class="text-sm text-gray-500 mt-1">{{ t.desc }}</div>
          </button>
        </div>
      </div>

      <!-- 时间模式 -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-6">
        <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">时间模式</h2>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
          <button
            v-for="m in timeModes"
            :key="m.key"
            @click="selectedMode = m.mode; selectedTimeLimit = m.timeLimit"
            :class="['p-4 rounded-xl border-2 transition text-center', selectedMode === m.mode && selectedTimeLimit === m.timeLimit ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-gray-300']"
          >
            <div class="text-xl mb-1">{{ m.icon }}</div>
            <div class="font-bold text-gray-900 text-sm">{{ m.label }}</div>
            <div class="text-xs text-gray-500">{{ m.desc }}</div>
          </button>
        </div>
      </div>

      <button
        @click="startChallenge"
        class="w-full py-4 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold text-lg rounded-2xl transition shadow-lg hover:shadow-xl"
      >
        开始挑战 🚀
      </button>

      <!-- 排行榜入口 -->
      <button @click="phase = 'leaderboard'" class="w-full mt-3 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium rounded-xl transition">
        查看排行榜 🏆
      </button>
    </div>

    <!-- 语音选择弹窗 -->
    <div v-if="showSpeakDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.self="confirmSpeakSetting(true)">
      <div class="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full mx-4">
        <div class="text-center">
          <div class="text-5xl mb-4">🔊</div>
          <h2 class="text-2xl font-bold text-gray-900 mb-2">开启语音播报？</h2>
          <p class="text-gray-500 mb-6">挑战时将自动播放单词/短语读音</p>
          
          <!-- 倒计时 -->
          <div class="mb-6">
            <div class="text-4xl font-bold text-blue-600 mb-2">{{ speakDialogCountdown }}</div>
            <div class="text-sm text-gray-400">秒后自动开启</div>
          </div>

          <!-- 按钮组 -->
          <div class="flex gap-3">
            <button 
              @click="confirmSpeakSetting(false)" 
              class="flex-1 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium rounded-xl transition"
            >
              关闭语音
            </button>
            <button 
              @click="confirmSpeakSetting(true)" 
              class="flex-1 py-3 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold rounded-xl transition shadow-lg"
            >
              开启语音 🔊
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 游戏页 -->
    <div v-else-if="phase === 'playing'">
      <!-- 顶部状态栏 -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-4 mb-4">
        <div class="grid grid-cols-4 gap-4 text-center">
          <div>
            <div class="text-2xl font-bold" :class="timeLeft <= 10 && selectedMode !== 'INFINITE' ? 'text-red-500 animate-pulse' : 'text-blue-600'">
              {{ selectedMode === 'INFINITE' ? formatTime(elapsedMs) : formatCountdown(timeLeft) }}
            </div>
            <div class="text-xs text-gray-500">{{ selectedMode === 'INFINITE' ? '已用时' : '剩余时间' }}</div>
          </div>
          <div>
            <div class="text-2xl font-bold text-green-600">{{ passedCount }}</div>
            <div class="text-xs text-gray-500">已通过</div>
          </div>
          <div>
            <div class="text-2xl font-bold text-purple-600">{{ currentAccuracy }}%</div>
            <div class="text-xs text-gray-500">正确率</div>
          </div>
          <div>
            <div class="text-2xl font-bold text-orange-600">{{ currentWpm }}</div>
            <div class="text-xs text-gray-500">WPM</div>
          </div>
        </div>
      </div>

      <!-- 打字区域 -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-4">
        <!-- 中文提示 + 读音控制 -->
        <div class="flex items-center justify-between mb-4">
          <div class="text-center flex-1">
            <span class="text-sm text-gray-500">中文释义：</span>
            <span class="text-lg font-medium text-gray-800">{{ currentExercise?.contentZh }}</span>
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


            <!-- 字符显示 -->
        <div
          class="font-mono text-2xl tracking-widest text-center py-4 px-4 bg-gray-50 rounded-xl mb-4 min-h-16 flex items-center justify-center flex-wrap cursor-text"
          @click="focusInput"
        >
          <span
            v-for="(char, idx) in targetChars"
            :key="idx"
            :class="[
              'inline-block transition-all px-0.5 py-1 rounded',
              char === ' ' ? 'mx-1 min-w-[0.6rem] text-center bg-transparent' : '',
              idx < userInput.length
                ? (userInput[idx] === char 
                    ? (char === ' ' ? 'text-green-600 border-b-2 border-green-500' : 'text-green-500') 
                    : (char === ' ' ? 'text-red-600 border-b-2 border-red-500' : 'text-red-500 bg-red-50'))
                : idx === userInput.length
                  ? (char === ' ' ? 'text-blue-600 border-b-3 border-blue-500 font-bold' : 'border-b-2 border-blue-500 text-gray-800')
                  : (char === ' ' ? 'text-gray-300 border-b-2 border-dashed border-gray-300' : 'text-gray-400')
            ]"
          >{{ char === ' ' ? '\u00A0' : char }}</span>
        </div>

        <input
          ref="inputRef"
          class="sr-only"
          @keydown="handleKeyDown"
          @paste.prevent
          autocomplete="off"
          autocorrect="off"
          autocapitalize="off"
          spellcheck="false"
          readonly
        />

        <!-- 操作按钮 -->
        <div class="flex gap-3 justify-center">
          <button @click="skipCurrent" class="px-6 py-2 bg-yellow-100 hover:bg-yellow-200 text-yellow-700 font-medium rounded-lg transition">
            跳过 (Enter)
          </button>
          <button v-if="selectedMode === 'INFINITE'" @click="endChallenge" class="px-6 py-2 bg-red-100 hover:bg-red-200 text-red-700 font-medium rounded-lg transition">
            停止挑战
          </button>
        </div>
      </div>

      <!-- 进度提示 -->
      <div class="text-center text-sm text-gray-400">
        剩余预加载：{{ exercises.length - currentExerciseIndex }} 条
      </div>
    </div>

    <!-- 结果页 -->
    <div v-else-if="phase === 'result'" class="flex items-center justify-center min-h-64">
      <div class="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md text-center">
        <div class="text-5xl mb-4">🎉</div>
        <h2 class="text-2xl font-bold text-gray-900 mb-6">挑战结束！</h2>

        <div class="space-y-3 mb-6">
          <div class="flex justify-between items-center py-2 border-b border-gray-100">
            <span class="text-gray-600">通过数量</span>
            <span class="text-2xl font-bold text-green-600">{{ passedCount }} 个</span>
          </div>
          <div class="flex justify-between items-center py-2 border-b border-gray-100">
            <span class="text-gray-600">正确率</span>
            <span class="text-xl font-bold text-blue-600">{{ currentAccuracy }}%</span>
          </div>
          <div class="flex justify-between items-center py-2 border-b border-gray-100">
            <span class="text-gray-600">打字速度</span>
            <span class="text-xl font-bold text-purple-600">{{ currentWpm }} WPM</span>
          </div>
          <div v-if="selectedMode === 'INFINITE'" class="flex justify-between items-center py-2 border-b border-gray-100">
            <span class="text-gray-600">用时</span>
            <span class="text-xl font-bold text-orange-600">{{ formatTime(elapsedMs) }}</span>
          </div>
        </div>

        <div class="flex gap-3">
          <button @click="phase = 'leaderboard'" class="flex-1 py-3 bg-yellow-500 hover:bg-yellow-600 text-white font-medium rounded-xl transition">
            查看排行榜
          </button>
          <button @click="startChallenge" class="flex-1 py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-xl transition">
            再来一次
          </button>
          <button @click="phase = 'setup'" class="flex-1 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium rounded-xl transition">
            返回
          </button>
        </div>
      </div>
    </div>

    <!-- 排行榜页 -->
    <div v-else-if="phase === 'leaderboard'">
      <div class="flex items-center gap-3 mb-6">
        <button @click="phase = 'setup'" class="text-gray-500 hover:text-gray-700">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" /></svg>
        </button>
        <h1 class="text-2xl font-bold text-gray-900">天梯排行榜</h1>
      </div>

      <!-- 筛选 -->
      <div class="bg-white rounded-xl border border-gray-100 p-4 mb-4 flex flex-wrap gap-2">
        <button v-for="t in contentTypes" :key="t.value" @click="lbType = t.value; loadLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', lbType === t.value ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
          {{ t.icon }} {{ t.label }}
        </button>
        <div class="w-px bg-gray-200 mx-1"></div>
        <button @click="lbMode = 'TIMED'; lbTimeLimit = 60; loadLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', lbMode === 'TIMED' && lbTimeLimit === 60 ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
          60秒
        </button>
        <button @click="lbMode = 'TIMED'; lbTimeLimit = 120; loadLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', lbMode === 'TIMED' && lbTimeLimit === 120 ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
          120秒
        </button>
        <button @click="lbMode = 'TIMED'; lbTimeLimit = 300; loadLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', lbMode === 'TIMED' && lbTimeLimit === 300 ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
          300秒
        </button>
        <button @click="lbMode = 'INFINITE'; lbTimeLimit = undefined; loadLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', lbMode === 'INFINITE' ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
          无限
        </button>
      </div>

      <!-- 排行榜表格 -->
      <div class="bg-white rounded-xl border border-gray-100 overflow-hidden mb-4">
        <div v-if="lbLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>
        <div v-else-if="leaderboard.length === 0" class="text-center py-12 text-gray-500">暂无记录，快去挑战吧！</div>
        <table v-else class="min-w-full divide-y divide-gray-100">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">排名</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">通过数</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">正确率</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">{{ lbMode === 'INFINITE' ? '用时' : 'WPM' }}</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-for="entry in leaderboard" :key="entry.userId"
              :class="['transition', entry.isCurrentUser ? 'bg-blue-50 font-semibold' : 'hover:bg-gray-50']">
              <td class="px-4 py-3">
                <span class="text-lg">{{ entry.rank <= 3 ? ['🥇','🥈','🥉'][entry.rank-1] : entry.rank }}</span>
              </td>
              <td class="px-4 py-3 text-sm">
                {{ entry.username }}
                <span v-if="entry.isCurrentUser" class="ml-1 text-xs text-blue-600 bg-blue-100 px-1.5 py-0.5 rounded">我</span>
              </td>
              <td class="px-4 py-3 text-sm font-bold text-green-600">{{ entry.passedCount }}</td>
              <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">{{ entry.accuracy.toFixed(1) }}%</td>
              <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">
                {{ lbMode === 'INFINITE' ? formatTime(entry.timeMs || 0) : entry.wpm + ' WPM' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 历史记录 -->
      <div class="bg-white rounded-xl border border-gray-100 p-4">
        <h3 class="text-sm font-semibold text-gray-700 mb-3">我的最近记录</h3>
        <div v-if="history.length === 0" class="text-sm text-gray-400 text-center py-4">暂无记录</div>
        <div v-else class="space-y-2">
          <div v-for="r in history" :key="r.id" class="flex items-center justify-between text-sm py-2 border-b border-gray-50 last:border-0">
            <div class="flex items-center gap-2">
              <span class="px-2 py-0.5 rounded text-xs font-medium" :class="r.contentType === 'WORD' ? 'bg-blue-100 text-blue-700' : 'bg-purple-100 text-purple-700'">
                {{ r.contentType === 'WORD' ? '单词' : '短句' }}
              </span>
              <span class="px-2 py-0.5 rounded text-xs font-medium" :class="r.timeMode === 'TIMED' ? 'bg-green-100 text-green-700' : 'bg-orange-100 text-orange-700'">
                {{ r.timeMode === 'TIMED' ? r.timeLimit + 's' : '无限' }}
              </span>
            </div>
            <div class="flex items-center gap-4 text-gray-600">
              <span class="font-bold text-green-600">{{ r.passedCount }} 个</span>
              <span>{{ r.accuracy.toFixed(1) }}%</span>
              <span class="text-xs text-gray-400">{{ new Date(r.createdAt).toLocaleDateString('zh-CN') }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { getRandomExercises, submitChallenge, getChallengeLeaderboard, getChallengeHistory } from '@/api/challenge'
import type { ChallengeExercise, ChallengeLeaderboardEntry, ChallengeRecord } from '@/api/challenge'

type Phase = 'setup' | 'playing' | 'result' | 'leaderboard'
type ContentType = 'WORD' | 'SENTENCE'
type TimeMode = 'TIMED' | 'INFINITE'

const phase = ref<Phase>('setup')
const inputRef = ref<HTMLInputElement | null>(null)

// 配置
const selectedType = ref<ContentType>('WORD')
const selectedMode = ref<TimeMode>('TIMED')
const selectedTimeLimit = ref<number>(60)

// 游戏状态
const exercises = ref<ChallengeExercise[]>([])
const currentExerciseIndex = ref(0)
const userInput = ref('')
const passedCount = ref(0)
const totalTyped = ref(0)
const correctTyped = ref(0)
const startTime = ref<number>(0)
const elapsedMs = ref(0)
const timeLeft = ref(60)

// 语音播放
const autoSpeak = ref(true)
const showSpeakDialog = ref(false)
const speakDialogCountdown = ref(5)
let speakDialogTimer: ReturnType<typeof setInterval> | null = null

const currentExercise = computed(() => exercises.value[currentExerciseIndex.value])
const targetChars = computed(() => currentExercise.value?.contentEn.split('') || [])

const currentAccuracy = computed(() => {
  if (totalTyped.value === 0) return 100
  return Math.round(correctTyped.value / totalTyped.value * 100)
})

// 语音播放方法
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
        setTimeout(() => speakCurrent(), 100)
    }
})

// 排行榜
const leaderboard = ref<ChallengeLeaderboardEntry[]>([])
const history = ref<ChallengeRecord[]>([])
const lbLoading = ref(false)
const lbType = ref<ContentType>('WORD')
const lbMode = ref<TimeMode>('TIMED')
const lbTimeLimit = ref<number | undefined>(60)

const contentTypes = [
  { value: 'WORD' as ContentType, label: '单词挑战', icon: '📝', desc: '随机单词，速度优先' },
  { value: 'SENTENCE' as ContentType, label: '短句挑战', icon: '📖', desc: '随机短句，准确为王' },
]

const timeModes = [
  { key: 'timed-60', mode: 'TIMED' as TimeMode, label: '60秒', icon: '⏱', desc: '限时挑战', timeLimit: 60 },
  { key: 'timed-120', mode: 'TIMED' as TimeMode, label: '120秒', icon: '⏱', desc: '限时挑战', timeLimit: 120 },
  { key: 'timed-300', mode: 'TIMED' as TimeMode, label: '300秒', icon: '⏱', desc: '限时挑战', timeLimit: 300 },
  { key: 'infinite', mode: 'INFINITE' as TimeMode, label: '无限', icon: '♾️', desc: '自由挑战', timeLimit: 0 },
]

const currentWpm = computed(() => {
  const elapsed = elapsedMs.value / 1000 / 60
  if (elapsed === 0) return 0
  return Math.round(correctTyped.value / 5 / elapsed)
})

let gameTimer: ReturnType<typeof setInterval> | null = null

const focusInput = () => inputRef.value?.focus()

const formatTime = (ms: number) => {
  const s = Math.floor(ms / 1000)
  const m = Math.floor(s / 60)
  return `${m}:${(s % 60).toString().padStart(2, '0')}`
}

const formatCountdown = (s: number) => {
  const m = Math.floor(s / 60)
  return m > 0 ? `${m}:${(s % 60).toString().padStart(2, '0')}` : `${s}s`
}

const startChallenge = async () => {
  // 显示语音选择弹窗
  showSpeakDialog.value = true
  speakDialogCountdown.value = 5
  
  // 启动5秒倒计时
  if (speakDialogTimer) clearInterval(speakDialogTimer)
  speakDialogTimer = setInterval(() => {
    speakDialogCountdown.value--
    if (speakDialogCountdown.value <= 0) {
      clearInterval(speakDialogTimer!)
      // 5秒后默认开启语音，进入正式挑战
      autoSpeak.value = true
      showSpeakDialog.value = false
      startGame()
    }
  }, 1000)
}

const confirmSpeakSetting = (enabled: boolean) => {
  if (speakDialogTimer) {
    clearInterval(speakDialogTimer)
    speakDialogTimer = null
  }
  autoSpeak.value = enabled
  showSpeakDialog.value = false
  startGame()
}

const startGame = async () => {
  // 重置状态
  exercises.value = []
  currentExerciseIndex.value = 0
  userInput.value = ''
  passedCount.value = 0
  totalTyped.value = 0
  correctTyped.value = 0
  elapsedMs.value = 0
  timeLeft.value = selectedTimeLimit.value

  // 加载练习内容
  const res = await getRandomExercises(selectedType.value, 30)
  exercises.value = (res.data as any).data || res.data

  phase.value = 'playing'
  startTime.value = Date.now()

  // 启动计时器
  if (gameTimer) clearInterval(gameTimer)
  gameTimer = setInterval(() => {
    elapsedMs.value = Date.now() - startTime.value
    if (selectedMode.value === 'TIMED') {
      timeLeft.value = Math.max(0, selectedTimeLimit.value - Math.floor(elapsedMs.value / 1000))
      if (timeLeft.value === 0) endChallenge()
    }
  }, 100)

  setTimeout(focusInput, 100)
}

const handleKeyDown = (e: KeyboardEvent) => {
  if (e.key === 'Enter') { skipCurrent(); return }
  if (e.key === 'Backspace') {
    userInput.value = userInput.value.slice(0, -1)
    return
  }
  if (e.key.length !== 1) return

  const pos = userInput.value.length
  const expected = targetChars.value[pos]
  if (!expected) return

  totalTyped.value++
  if (e.key === expected) correctTyped.value++

  userInput.value += e.key

  // 检查是否完成（全部正确）
  if (userInput.value === currentExercise.value?.contentEn) {
    passedCount.value++
    nextExercise()
  }
}

const skipCurrent = () => {
  nextExercise()
}

const nextExercise = async () => {
  userInput.value = ''
  currentExerciseIndex.value++

  // 预加载：剩余5条时补充
  if (exercises.value.length - currentExerciseIndex.value <= 5) {
    const res = await getRandomExercises(selectedType.value, 20)
    const newExercises = (res.data as any).data || res.data
    exercises.value = [...exercises.value, ...newExercises]
  }
}

const endChallenge = async () => {
  if (gameTimer) { clearInterval(gameTimer); gameTimer = null }
  phase.value = 'result'

  // 提交结果
  try {
    await submitChallenge({
      contentType: selectedType.value,
      timeMode: selectedMode.value,
      timeLimit: selectedMode.value === 'TIMED' ? selectedTimeLimit.value : undefined,
      passedCount: passedCount.value,
      accuracy: currentAccuracy.value,
      timeMs: selectedMode.value === 'INFINITE' ? elapsedMs.value : undefined,
      wpm: currentWpm.value
    })
  } catch (e) {
    console.error('提交失败:', e)
  }
}

const loadLeaderboard = async () => {
  lbLoading.value = true
  try {
    const res = await getChallengeLeaderboard({
      type: lbType.value,
      mode: lbMode.value,
      timeLimit: lbMode.value === 'TIMED' ? lbTimeLimit.value : undefined
    })
    leaderboard.value = (res.data as any).data || res.data

    const histRes = await getChallengeHistory()
    history.value = (histRes.data as any).data || histRes.data
  } catch (e) {
    console.error(e)
  } finally {
    lbLoading.value = false
  }
}

watch(phase, (val) => {
  if (val === 'leaderboard') loadLeaderboard()
})

onMounted(() => window.addEventListener('click', focusInput))
onUnmounted(() => {
  if (gameTimer) clearInterval(gameTimer)
  if (speakDialogTimer) clearInterval(speakDialogTimer)
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