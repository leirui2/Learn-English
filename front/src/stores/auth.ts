import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 用户信息类型
interface UserInfo {
  id: string
  username: string
  email: string
  totalScore: number
  streak: number
  role?: string  // 用户角色：USER 或 ADMIN
}

/**
 * 认证 Store
 * 管理 JWT token、用户信息，并持久化到 localStorage
 */
export const useAuthStore = defineStore('auth', () => {
  // access token（短期，2小时）
  const accessToken = ref<string | null>(localStorage.getItem('access_token'))
  // refresh token（长期，7天）
  const refreshToken = ref<string | null>(localStorage.getItem('refresh_token'))
  // 当前登录用户信息
  const userInfo = ref<UserInfo | null>(
    JSON.parse(localStorage.getItem('user_info') ?? 'null')
  )

  // 是否已登录（有 access token 即视为登录状态）
  const isLoggedIn = computed(() => !!accessToken.value)
  
  // 是否是管理员
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')

  /**
   * 登录：保存 token 和用户信息到内存及 localStorage
   */
  function login(tokens: { accessToken: string; refreshToken: string }, user: UserInfo) {
    accessToken.value = tokens.accessToken
    refreshToken.value = tokens.refreshToken
    userInfo.value = user

    localStorage.setItem('access_token', tokens.accessToken)
    localStorage.setItem('refresh_token', tokens.refreshToken)
    localStorage.setItem('user_info', JSON.stringify(user))
  }

  /**
   * 更新 access token（token 刷新时使用）
   */
  function updateAccessToken(newToken: string) {
    accessToken.value = newToken
    localStorage.setItem('access_token', newToken)
  }

  /**
   * 退出登录：清除所有认证信息
   */
  function logout() {
    accessToken.value = null
    refreshToken.value = null
    userInfo.value = null

    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    localStorage.removeItem('user_info')
  }

  return {
    accessToken,
    refreshToken,
    userInfo,
    isLoggedIn,
    isAdmin,
    login,
    updateAccessToken,
    logout
  }
})
