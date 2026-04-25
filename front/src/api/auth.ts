import axios from 'axios'

// API 基础 URL
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8085'

// 创建 axios 实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器：添加 token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器：处理 token 过期
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    
    // 如果是 401 错误且不是刷新 token 请求，尝试刷新 token
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      
      try {
        const refreshToken = localStorage.getItem('refresh_token')
        if (refreshToken) {
          const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
            refreshToken
          })
          
          const { accessToken } = response.data
          localStorage.setItem('access_token', accessToken)
          
          // 重试原请求
          originalRequest.headers.Authorization = `Bearer ${accessToken}`
          return apiClient(originalRequest)
        }
      } catch (refreshError) {
        // 刷新失败，清除登录状态
        localStorage.removeItem('access_token')
        localStorage.removeItem('refresh_token')
        localStorage.removeItem('user_info')
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }
    
    return Promise.reject(error)
  }
)

// 认证相关 API

/**
 * 用户登录
 */
export interface LoginRequest {
  identifier: string  // 用户名或邮箱
  password: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  user: {
    id: string
    username: string
    email: string
    totalScore: number
    streak: number
    role: string
    status: string
  }
}

// 后端统一响应格式
interface ApiResult<T> {
  code: number
  message: string
  data: T
}

export const login = (data: LoginRequest) => {
  return apiClient.post<ApiResult<LoginResponse>>('/auth/login', data)
}

/**
 * 用户注册
 */
export interface RegisterRequest {
  username: string
  email: string
  password: string
}

export interface RegisterResponse {
  accessToken: string
  refreshToken: string
  user: {
    id: string
    username: string
    email: string
    totalScore: number
    streak: number
    role: string
    status: string
  }
}

export const register = (data: RegisterRequest) => {
  return apiClient.post<ApiResult<RegisterResponse>>('/auth/register', data)
}

/**
 * 申请密码重置
 */
export interface ForgotPasswordRequest {
  email: string
}

export const forgotPassword = (data: ForgotPasswordRequest) => {
  return apiClient.post('/auth/forgot-password', data)
}

/**
 * 重置密码
 */
export interface ResetPasswordRequest {
  token: string
  newPassword: string
  confirmPassword: string
}

export const resetPassword = (data: ResetPasswordRequest) => {
  return apiClient.post('/auth/reset-password', data)
}

/**
 * 退出登录
 */
export const logout = () => {
  const refreshToken = localStorage.getItem('refresh_token')
  return apiClient.post('/auth/logout', { refreshToken })
}

/**
 * 获取当前用户信息
 */
export interface UserInfoResponse {
  id: string
  username: string
  email: string
  totalScore: number
  streak: number
  role: string
  status: string
}

export const getUserInfo = () => {
  return apiClient.get<ApiResult<UserInfoResponse>>('/users/me')
}

export default apiClient
