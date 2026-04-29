<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50">
    <!-- 顶部装饰 -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-purple-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-blue-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-2000"></div>
      <div class="absolute top-1/2 left-1/2 w-80 h-80 bg-indigo-300 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-blob animation-delay-4000"></div>
    </div>

    <div v-if="loading" class="flex justify-center items-center py-32 relative">
      <div class="relative">
        <div class="animate-spin rounded-full h-16 w-16 border-4 border-blue-200"></div>
        <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 absolute top-0 left-0"></div>
      </div>
    </div>

    <div v-else>
      <!-- 顶部 Banner -->
      <div class="bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-700 text-white">
        <div class="max-w-5xl mx-auto px-4 py-10">
          <div class="flex flex-col md:flex-row items-center md:items-end gap-6">
            <!-- 头像 -->
            <div class="relative group flex-shrink-0">
              <img
                v-if="profile?.avatarUrl"
                :src="profile.avatarUrl"
                class="w-24 h-24 rounded-full object-cover shadow-lg border-4 border-white/40"
              />
              <div
                v-else
                class="w-24 h-24 rounded-full bg-white/20 backdrop-blur flex items-center justify-center text-4xl font-bold shadow-lg border-4 border-white/40"
              >
                {{ displayName.charAt(0).toUpperCase() }}
              </div>
              <label
                v-if="isOwnProfile"
                class="absolute inset-0 rounded-full bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition cursor-pointer"
              >
                <span class="text-white text-xs font-medium">换头像</span>
                <input type="file" accept="image/*" class="hidden" @change="handleAvatarChange" />
              </label>
            </div>

            <!-- 用户名 & 简介 -->
            <div class="text-center md:text-left flex-1">
              <div class="flex items-center gap-2 justify-center md:justify-start flex-wrap">
                <h1 class="text-3xl font-bold">{{ displayName }}</h1>
                <span v-if="isOwnProfile" class="text-xs bg-white/20 px-2 py-0.5 rounded-full">我的主页</span>
                <span v-if="profile?.gender === 'MALE'" class="text-blue-200 text-sm">♂</span>
                <span v-else-if="profile?.gender === 'FEMALE'" class="text-pink-200 text-sm">♀</span>
              </div>
              <p v-if="profile?.bio" class="text-blue-100 mt-1 text-sm max-w-md">{{ profile.bio }}</p>
              <p class="text-blue-200 mt-1 text-sm">连续打卡 {{ stats?.streak || 0 }} 天 🔥</p>
            </div>

            <!-- 右侧：编辑按钮 + 统计 -->
            <div class="md:ml-auto flex flex-col items-center md:items-end gap-3">
              <button
                v-if="isOwnProfile"
                @click="openEditModal"
                class="flex items-center gap-2 px-4 py-2 bg-white/20 hover:bg-white/30 text-white rounded-lg transition text-sm font-medium"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
                编辑资料
              </button>
              <div class="flex gap-6 text-center">
                <div><div class="text-2xl font-bold">{{ stats?.totalScore || 0 }}</div><div class="text-xs text-blue-200">总积分</div></div>
                <div><div class="text-2xl font-bold">{{ stats?.totalCompletedLevels || 0 }}</div><div class="text-xs text-blue-200">完成关卡</div></div>
                <div><div class="text-2xl font-bold">{{ stats?.streak || 0 }}</div><div class="text-xs text-blue-200">连续打卡</div></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 主体内容 -->
      <div class="max-w-5xl mx-auto px-4 py-8 space-y-6">
        <!-- 统计卡片 -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-blue-100 hover:shadow-2xl transition transform hover:scale-105">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-12 h-12 bg-gradient-to-r from-blue-500 to-purple-500 rounded-xl flex items-center justify-center text-2xl shadow-lg">⏱️</div>
              <span class="text-sm text-gray-600 font-medium">练习时长</span>
            </div>
            <div class="text-3xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">{{ formatTime(stats?.totalPracticeTimeMs || 0) }}</div>
          </div>
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-green-100 hover:shadow-2xl transition transform hover:scale-105">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-12 h-12 bg-gradient-to-r from-green-500 to-teal-500 rounded-xl flex items-center justify-center text-2xl shadow-lg">⌨️</div>
              <span class="text-sm text-gray-600 font-medium">输入字符</span>
            </div>
            <div class="text-3xl font-bold bg-gradient-to-r from-green-600 to-teal-600 bg-clip-text text-transparent">{{ formatNumber(stats?.totalCharactersTyped || 0) }}</div>
          </div>
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-purple-100 hover:shadow-2xl transition transform hover:scale-105">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-12 h-12 bg-gradient-to-r from-purple-500 to-pink-500 rounded-xl flex items-center justify-center text-2xl shadow-lg">🏆</div>
              <span class="text-sm text-gray-600 font-medium">完成关卡</span>
            </div>
            <div class="text-3xl font-bold bg-gradient-to-r from-purple-600 to-pink-600 bg-clip-text text-transparent">{{ stats?.totalCompletedLevels || 0 }}</div>
          </div>
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-orange-100 hover:shadow-2xl transition transform hover:scale-105 cursor-pointer" @click="goToPointsHistory">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-12 h-12 bg-gradient-to-r from-orange-500 to-red-500 rounded-xl flex items-center justify-center text-2xl shadow-lg">⭐</div>
              <span class="text-sm text-gray-600 font-medium">总积分</span>
            </div>
            <div class="text-3xl font-bold bg-gradient-to-r from-orange-600 to-red-600 bg-clip-text text-transparent">{{ stats?.totalScore || 0 }}</div>
            <div class="text-xs text-blue-500 mt-2 flex items-center gap-1">
              <span>查看明细</span>
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </div>
          </div>
        </div>

        <!-- 关卡历史最高正确率 -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
          <div class="px-6 py-4 border-b border-gray-100 flex items-center gap-2">
            <span class="text-lg">📊</span>
            <h2 class="text-lg font-bold text-gray-900">各关卡历史最高正确率</h2>
          </div>
          <div class="p-6">
            <div v-if="!isOwnProfile" class="text-center py-12">
              <div class="text-5xl mb-3">🔒</div>
              <p class="text-gray-500">关卡详情仅本人可见</p>
            </div>
            <div v-else-if="loadingLevels" class="text-center text-gray-400 py-8">加载中...</div>
            <div v-else-if="categoriesWithCompletedLevels.length === 0" class="text-center py-12">
              <div class="text-5xl mb-3">🎯</div>
              <p class="text-gray-500">还没有完成任何关卡，快去练习吧！</p>
            </div>
            <div v-else class="space-y-6">
              <div v-for="category in categoriesWithCompletedLevels" :key="category.id">
                <h3 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-3">{{ category.name }}</h3>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
                  <div v-for="level in category.levels" :key="level.id" class="flex items-center justify-between bg-gray-50 rounded-lg px-4 py-3 border border-gray-100">
                    <span class="text-sm text-gray-700 truncate flex-1">{{ level.name }}</span>
                    <div class="ml-3 flex items-center gap-1">
                      <div class="w-16 h-2 bg-gray-200 rounded-full overflow-hidden">
                        <div class="h-full rounded-full transition-all" :class="getAccuracyBg(level.bestAccuracy || 0)" :style="{ width: `${level.bestAccuracy || 0}%` }" />
                      </div>
                      <span class="text-sm font-bold ml-1" :class="getAccuracyColor(level.bestAccuracy || 0)">{{ (level.bestAccuracy || 0).toFixed(1) }}%</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 易错词汇 -->
        <div v-if="isOwnProfile" class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
          <div class="px-6 py-4 border-b border-gray-100 flex items-center gap-2">
            <span class="text-lg">📝</span>
            <h2 class="text-lg font-bold text-gray-900">易错词汇 Top 10</h2>
          </div>
          <div class="p-6">
            <div v-if="loadingWeakWords" class="text-center text-gray-400 py-8">加载中...</div>
            <div v-else-if="weakWords.length === 0" class="text-center py-12">
              <div class="text-5xl mb-3">✨</div>
              <p class="text-gray-500">暂无易错词汇，继续保持！</p>
            </div>
            <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div v-for="(word, index) in weakWords" :key="word.word" class="flex items-center gap-3 bg-gray-50 rounded-lg px-4 py-3 border border-gray-100">
                <span class="w-6 h-6 rounded-full bg-red-100 text-red-600 text-xs font-bold flex items-center justify-center flex-shrink-0">{{ index + 1 }}</span>
                <span class="flex-1 font-medium text-gray-900">{{ word.word }}</span>
                <span class="text-sm text-red-500 font-semibold">错误 {{ word.errorCount }} 次</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑资料弹窗 -->
    <Transition name="modal">
      <div v-if="showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="closeEditModal">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
          <!-- 头部 -->
          <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100">
            <h3 class="text-lg font-bold text-gray-900">编辑个人资料</h3>
            <button @click="closeEditModal" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 transition text-gray-400 hover:text-gray-600">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- 内容 -->
          <div class="px-6 py-5 space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">昵称</label>
              <input
                v-model="editForm.username"
                type="text"
                maxlength="50"
                placeholder="请输入昵称（不填则显示用户名）"
                class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">个人简介</label>
              <textarea
                v-model="editForm.bio"
                rows="3"
                maxlength="500"
                placeholder="介绍一下自己吧..."
                class="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition resize-none"
              />
              <p class="text-xs text-gray-400 mt-1 text-right">{{ editForm.bio?.length || 0 }}/500</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">性别</label>
              <div class="flex gap-4">
                <label v-for="g in genderOptions" :key="g.value" class="flex items-center gap-2 cursor-pointer">
                  <input type="radio" v-model="editForm.gender" :value="g.value" class="accent-blue-600" />
                  <span class="text-sm text-gray-700">{{ g.label }}</span>
                </label>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">手机号</label>
              <input
                v-model="editForm.phone"
                type="tel"
                maxlength="20"
                placeholder="请输入手机号（选填）"
                class="w-full px-3 py-2.5 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                :class="phoneError ? 'border-red-400' : 'border-gray-300'"
              />
              <p v-if="phoneError" class="text-xs text-red-500 mt-1">{{ phoneError }}</p>
            </div>

            <p v-if="editError" class="text-sm text-red-500 bg-red-50 px-3 py-2 rounded-lg">{{ editError }}</p>
          </div>

          <!-- 底部按钮 -->
          <div class="px-6 py-4 border-t border-gray-100 flex gap-3 justify-end">
            <button @click="closeEditModal" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition font-medium">
              取消
            </button>
            <button
              @click="saveProfile"
              :disabled="saving"
              class="px-5 py-2 text-sm bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed text-white rounded-lg transition font-medium flex items-center gap-2"
            >
              <svg v-if="saving" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ saving ? '保存中...' : '保存' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getUserStats, getUserStatsByUsername, getWeakWords } from '@/api/stats'
import { getLevels } from '@/api/level'
import { getMyProfile, updateProfile, uploadAvatar } from '@/api/user'
import { useAuthStore } from '@/stores/auth'
import type { UserStatsResponse, WeakWordDto } from '@/api/stats'
import type { CategoryWithLevels, Level } from '@/api/level'
import type { UserProfileDto } from '@/api/user'

const route = useRoute()
const authStore = useAuthStore()

const loading = ref(true)
const loadingLevels = ref(true)
const loadingWeakWords = ref(true)
const stats = ref<UserStatsResponse | null>(null)
const profile = ref<UserProfileDto | null>(null)
const weakWords = ref<WeakWordDto[]>([])
const allCategories = ref<CategoryWithLevels[]>([])

// 编辑弹窗
const showEditModal = ref(false)
const saving = ref(false)
const editError = ref('')
const phoneError = ref('')
const editForm = ref({ username: '', bio: '', gender: '', phone: '' })

const genderOptions = [
  { value: 'MALE', label: '男' },
  { value: 'FEMALE', label: '女' },
  { value: 'OTHER', label: '保密' }
]

const targetUsername = computed(() => route.params.userId as string | undefined)
const isOwnProfile = computed(() => !targetUsername.value)
const displayName = computed(() => {
  // 优先显示昵称，没有昵称则显示用户名
  if (profile.value?.nickname) return profile.value.nickname
  if (profile.value?.username) return profile.value.username
  if (stats.value?.username) return stats.value.username
  if (isOwnProfile.value) return authStore.userInfo?.username || '我'
  return targetUsername.value || '用户'
})

const openEditModal = () => {
  editForm.value = {
    username: profile.value?.nickname || '',
    bio: profile.value?.bio || '',
    gender: profile.value?.gender || '',
    phone: profile.value?.phone || ''
  }
  editError.value = ''
  showEditModal.value = true
}

const closeEditModal = () => {
  showEditModal.value = false
  phoneError.value = ''
}

const saveProfile = async () => {
  editError.value = ''
  phoneError.value = ''

  // 手机号格式验证（填了才验证）
  if (editForm.value.phone) {
    const phoneReg = /^1[3-9]\d{9}$/
    if (!phoneReg.test(editForm.value.phone)) {
      phoneError.value = '请输入正确的手机号格式（11位数字，1开头）'
      return
    }
  }

  saving.value = true
  try {
    const res = await updateProfile({
      nickname: editForm.value.username || undefined,
      bio: editForm.value.bio || undefined,
      gender: editForm.value.gender || undefined,
      phone: editForm.value.phone !== undefined ? editForm.value.phone : undefined
    })
    profile.value = (res.data as any).data || res.data
    if (stats.value) stats.value.username = res.data.username
    closeEditModal()
  } catch (e: any) {
    editError.value = e.response?.data?.message || '保存失败，请重试'
  } finally {
    saving.value = false
  }
}

const handleAvatarChange = async (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  try {
    const res = await uploadAvatar(file)
    const url = (res.data as any).data || res.data
    if (profile.value) {
      profile.value = { ...profile.value, avatarUrl: url }
    }
  } catch (e: any) {
    alert(e.response?.data?.message || '头像上传失败')
  }
}

const formatTime = (ms: number): string => {
  const totalSeconds = Math.floor(ms / 1000)
  const hours = Math.floor(totalSeconds / 3600)
  const minutes = Math.floor((totalSeconds % 3600) / 60)
  if (hours > 0) return `${hours}h${minutes}m`
  if (minutes > 0) return `${minutes}分钟`
  return `${totalSeconds}秒`
}

const formatNumber = (n: number): string => {
  if (n >= 10000) return `${(n / 10000).toFixed(1)}万`
  return n.toLocaleString()
}

const getAccuracyColor = (accuracy: number) => {
  if (accuracy >= 95) return 'text-green-600'
  if (accuracy >= 85) return 'text-blue-600'
  if (accuracy >= 75) return 'text-yellow-600'
  return 'text-red-500'
}

const getAccuracyBg = (accuracy: number) => {
  if (accuracy >= 95) return 'bg-green-500'
  if (accuracy >= 85) return 'bg-blue-500'
  if (accuracy >= 75) return 'bg-yellow-500'
  return 'bg-red-500'
}

const goToPointsHistory = () => {
  window.location.href = '/points/history'
}

const completedLevels = computed(() => {
  const levels: Level[] = []
  allCategories.value.forEach(cat => {
    cat.levels.forEach(level => {
      if (level.completed && level.bestAccuracy != null) levels.push(level)
    })
  })
  return levels
})

const categoriesWithCompletedLevels = computed(() => {
  return allCategories.value
    .map(cat => ({
      id: cat.category.id,
      name: cat.category.name,
      levels: cat.levels.filter(l => l.completed && l.bestAccuracy != null)
    }))
    .filter(cat => cat.levels.length > 0)
})

const loadData = async () => {
  loading.value = true
  loadingLevels.value = true
  loadingWeakWords.value = true
  stats.value = null
  profile.value = null
  weakWords.value = []
  allCategories.value = []

  try {
    const statsRes = isOwnProfile.value
      ? await getUserStats()
      : await getUserStatsByUsername(targetUsername.value!)
    stats.value = statsRes.data
  } catch (e) {
    console.error('加载统计失败:', e)
  } finally {
    loading.value = false
  }

  if (isOwnProfile.value) {
    try {
      const profileRes = await getMyProfile()
      profile.value = (profileRes.data as any).data || profileRes.data
    } catch (e) {
      console.error('加载个人信息失败:', e)
    }

    try {
      const res = await getLevels()
      const data = (res.data as any).data
      allCategories.value = data.map((item: any) => ({
        category: { id: item.id, name: item.name, description: item.description, difficulty: item.difficulty },
        levels: item.levels || []
      }))
    } catch (e) {
      console.error('加载关卡失败:', e)
    } finally {
      loadingLevels.value = false
    }

    try {
      const res = await getWeakWords()
      weakWords.value = res.data
    } catch (e) {
      console.error('加载易错词汇失败:', e)
    } finally {
      loadingWeakWords.value = false
    }
  } else {
    loadingLevels.value = false
    loadingWeakWords.value = false
  }
}

watch(() => route.params.userId, loadData)
onMounted(loadData)
</script>

<style scoped>
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
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
</style>
