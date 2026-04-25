<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-gray-900 mb-6">排行榜</h1>

    <!-- 主 Tab：普通榜 / 天梯榜 -->
    <div class="flex gap-2 mb-6">
      <button
        @click="mainTab = 'normal'"
        :class="['px-5 py-2.5 rounded-xl font-semibold text-sm transition', mainTab === 'normal' ? 'bg-blue-600 text-white shadow' : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50']"
      >🏆 积分 / 打卡榜</button>
      <button
        @click="mainTab = 'challenge'"
        :class="['px-5 py-2.5 rounded-xl font-semibold text-sm transition', mainTab === 'challenge' ? 'bg-purple-600 text-white shadow' : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50']"
      >⚡ 天梯挑战榜</button>
    </div>

    <!-- ===== 普通排行榜 ===== -->
    <div v-if="mainTab === 'normal'">
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6 space-y-3">
        <div class="flex gap-2">
          <button v-for="t in typeOptions" :key="t.value" @click="selectedType = t.value; loadLeaderboard()"
            :class="['px-4 py-2 rounded-lg font-medium text-sm transition', selectedType === t.value ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
            {{ t.label }}
          </button>
        </div>
        <div class="flex gap-2">
          <button v-for="p in periodOptions" :key="p.value" @click="selectedPeriod = p.value; loadLeaderboard()"
            :class="['px-4 py-2 rounded-lg font-medium text-sm transition', selectedPeriod === p.value ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
            {{ p.label }}
          </button>
        </div>
        <div class="flex flex-wrap gap-2">
          <button @click="selectedCategory = null; loadLeaderboard()"
            :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', selectedCategory === null ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
            全部
          </button>
          <button v-for="cat in categories" :key="cat.id" @click="selectedCategory = cat.id; loadLeaderboard()"
            :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', selectedCategory === cat.id ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
            {{ cat.name }}
          </button>
        </div>
      </div>

      <div v-if="currentUserRank && !loading" class="bg-blue-50 border border-blue-200 rounded-xl p-4 mb-6 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center text-white font-bold">{{ currentUserRank.rank }}</div>
          <div>
            <div class="font-semibold text-gray-900">{{ currentUserRank.username }} <span class="text-xs text-blue-600">（我）</span></div>
            <div class="text-sm text-gray-500">我的排名</div>
          </div>
        </div>
        <div class="text-2xl font-bold text-blue-600">
          {{ selectedType === 'score' ? currentUserRank.score : currentUserRank.streak }}
          <span class="text-sm font-normal text-gray-500 ml-1">{{ selectedType === 'score' ? '分' : '天' }}</span>
        </div>
      </div>

      <div v-if="loading" class="flex justify-center py-16"><div class="animate-spin rounded-full h-10 w-10 border-b-2 border-blue-600"></div></div>
      <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div v-if="entries.length === 0" class="text-center py-16 text-gray-500">暂无数据</div>
        <table v-else class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase w-16">排名</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户名</th>
              <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">{{ selectedType === 'score' ? '积分' : '连续打卡' }}</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr v-for="entry in entries" :key="entry.userId" :class="['transition', entry.isCurrentUser ? 'bg-blue-50 font-semibold' : 'hover:bg-gray-50']">
              <td class="px-4 py-3 whitespace-nowrap">
                <div class="flex items-center justify-center w-8 h-8 rounded-full text-sm font-bold" :class="rankStyle(entry.rank)">
                  {{ entry.rank <= 3 ? ['🥇','🥈','🥉'][entry.rank - 1] : entry.rank }}
                </div>
              </td>
              <td class="px-4 py-3 whitespace-nowrap">
                <div class="flex items-center gap-2 cursor-pointer group" @click="goToUserProfile(entry)">
                  <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-white text-sm font-bold">
                    {{ entry.username.charAt(0).toUpperCase() }}
                  </div>
                  <span class="text-gray-900 group-hover:text-blue-600 transition">{{ entry.username }}</span>
                  <span v-if="entry.isCurrentUser" class="text-xs text-blue-600 bg-blue-100 px-1.5 py-0.5 rounded">我</span>
                </div>
              </td>
              <td class="px-4 py-3 whitespace-nowrap text-right">
                <span class="text-lg font-bold" :class="entry.isCurrentUser ? 'text-blue-600' : 'text-gray-900'">
                  {{ selectedType === 'score' ? entry.score : entry.streak }}
                </span>
                <span class="text-xs text-gray-500 ml-1">{{ selectedType === 'score' ? '分' : '天' }}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- ===== 天梯挑战排行榜 ===== -->
    <div v-else>
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6 flex flex-wrap gap-2">
        <button v-for="t in challengeContentTypes" :key="t.value" @click="clbType = t.value; loadChallengeLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', clbType === t.value ? 'bg-purple-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
          {{ t.icon }} {{ t.label }}
        </button>
        <div class="w-px bg-gray-200 mx-1"></div>
        <button @click="clbMode = 'TIMED'; clbTimeLimit = 60; loadChallengeLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', clbMode === 'TIMED' && clbTimeLimit === 60 ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">60秒</button>
        <button @click="clbMode = 'TIMED'; clbTimeLimit = 120; loadChallengeLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', clbMode === 'TIMED' && clbTimeLimit === 120 ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">120秒</button>
        <button @click="clbMode = 'TIMED'; clbTimeLimit = 300; loadChallengeLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', clbMode === 'TIMED' && clbTimeLimit === 300 ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">300秒</button>
        <button @click="clbMode = 'INFINITE'; clbTimeLimit = undefined; loadChallengeLeaderboard()"
          :class="['px-3 py-1.5 rounded-lg text-sm font-medium transition', clbMode === 'INFINITE' ? 'bg-orange-500 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">无限</button>
      </div>

      <div v-if="clbLoading" class="flex justify-center py-16"><div class="animate-spin rounded-full h-10 w-10 border-b-2 border-purple-600"></div></div>
      <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div v-if="challengeEntries.length === 0" class="text-center py-16 text-gray-500">暂无记录，快去挑战吧！</div>
        <table v-else class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">排名</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">通过数</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">正确率</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">{{ clbMode === 'INFINITE' ? '用时' : 'WPM' }}</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr v-for="entry in challengeEntries" :key="entry.userId"
              :class="['transition', entry.isCurrentUser ? 'bg-purple-50 font-semibold' : 'hover:bg-gray-50']">
              <td class="px-4 py-3 text-lg">{{ entry.rank <= 3 ? ['🥇','🥈','🥉'][entry.rank-1] : entry.rank }}</td>
              <td class="px-4 py-3 text-sm">
                {{ entry.username }}
                <span v-if="entry.isCurrentUser" class="ml-1 text-xs text-purple-600 bg-purple-100 px-1.5 py-0.5 rounded">我</span>
              </td>
              <td class="px-4 py-3 text-sm font-bold text-green-600">{{ entry.passedCount }}</td>
              <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">{{ entry.accuracy.toFixed(1) }}%</td>
              <td class="px-4 py-3 text-sm text-gray-600 hidden md:table-cell">
                {{ clbMode === 'INFINITE' ? formatTime(entry.timeMs || 0) : entry.wpm + ' WPM' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getLeaderboard, type LeaderboardEntryDto } from '@/api/leaderboard'
import { getLevels } from '@/api/level'
import { getChallengeLeaderboard, type ChallengeLeaderboardEntry } from '@/api/challenge'

const router = useRouter()

// 主 Tab
const mainTab = ref<'normal' | 'challenge'>('normal')

// 普通排行榜
const loading = ref(true)
const entries = ref<LeaderboardEntryDto[]>([])
const currentUserRank = ref<LeaderboardEntryDto | null>(null)
const selectedType = ref<'score' | 'streak'>('score')
const selectedPeriod = ref<'week' | 'all'>('all')
const selectedCategory = ref<number | null>(null)

const typeOptions = [
  { value: 'score' as const, label: '总积分榜' },
  { value: 'streak' as const, label: '连续打卡榜' }
]
const periodOptions = [
  { value: 'all' as const, label: '总榜' },
  { value: 'week' as const, label: '本周榜' }
]

interface Category { id: number; name: string }
const categories = ref<Category[]>([])

// 天梯排行榜
const clbLoading = ref(false)
const challengeEntries = ref<ChallengeLeaderboardEntry[]>([])
const clbType = ref<'WORD' | 'SENTENCE'>('WORD')
const clbMode = ref<'TIMED' | 'INFINITE'>('TIMED')
const clbTimeLimit = ref<number | undefined>(60)

const challengeContentTypes = [
  { value: 'WORD' as const, label: '单词榜', icon: '📝' },
  { value: 'SENTENCE' as const, label: '短句榜', icon: '📖' },
]

const formatTime = (ms: number) => {
  const s = Math.floor(ms / 1000)
  const m = Math.floor(s / 60)
  return `${m}:${(s % 60).toString().padStart(2, '0')}`
}

const rankStyle = (rank: number) => {
  if (rank === 1) return 'text-yellow-600'
  if (rank === 2) return 'text-gray-500'
  if (rank === 3) return 'text-orange-500'
  return 'text-gray-400 bg-gray-100'
}

const goToUserProfile = (entry: LeaderboardEntryDto) => {
  router.push(entry.isCurrentUser ? '/profile' : `/profile/${entry.username}`)
}

const loadLeaderboard = async () => {
  loading.value = true
  try {
    const params: { type: 'score' | 'streak'; period: 'week' | 'all'; category?: number } = {
      type: selectedType.value,
      period: selectedPeriod.value
    }
    if (selectedCategory.value !== null) params.category = selectedCategory.value
    const response = await getLeaderboard(params)
    entries.value = response.data.entries || []
    currentUserRank.value = response.data.currentUserRank || null
  } catch (error) {
    console.error('加载排行榜失败:', error)
  } finally {
    loading.value = false
  }
}

const loadChallengeLeaderboard = async () => {
  clbLoading.value = true
  try {
    const res = await getChallengeLeaderboard({
      type: clbType.value,
      mode: clbMode.value,
      timeLimit: clbMode.value === 'TIMED' ? clbTimeLimit.value : undefined
    })
    challengeEntries.value = (res.data as any).data || res.data
  } catch (e) {
    console.error(e)
  } finally {
    clbLoading.value = false
  }
}

const loadCategories = async () => {
  try {
    const response = await getLevels()
    const data = (response.data as any).data
    categories.value = data.map((item: any) => ({ id: item.id, name: item.name }))
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

onMounted(async () => {
  await Promise.all([loadLeaderboard(), loadCategories()])
})

watch(mainTab, (val) => {
  if (val === 'challenge') loadChallengeLeaderboard()
})
</script>
