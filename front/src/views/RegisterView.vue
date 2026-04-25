<template>
  <AuthLayout>
    <div class="bg-white rounded-2xl shadow-xl p-8 md:p-10 border border-gray-100">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <div class="inline-block mb-4">
          <div class="text-6xl animate-bounce">✨</div>
        </div>
        <h1 class="text-3xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">创建账号</h1>
        <p class="mt-2 text-sm text-gray-600">开始你的英语学习之旅 🎯</p>
      </div>

      <!-- 注册表单 -->
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- 用户名输入 -->
        <div>
          <label for="username" class="block text-sm font-medium text-gray-700 mb-2">
            <span class="flex items-center gap-2">
              <span>👤</span>
              <span>用户名</span>
            </span>
          </label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            required
            autocomplete="username"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.username }"
            placeholder="请输入用户名"
            @blur="validateUsername"
          />
          <p v-if="errors.username" class="mt-1 text-sm text-red-600">
            {{ errors.username }}
          </p>
        </div>

        <!-- 邮箱输入 -->
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
            <span class="flex items-center gap-2">
              <span>📧</span>
              <span>邮箱</span>
            </span>
          </label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            required
            autocomplete="email"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.email }"
            placeholder="请输入邮箱"
            @blur="validateEmail"
          />
          <p v-if="errors.email" class="mt-1 text-sm text-red-600">
            {{ errors.email }}
          </p>
        </div>

        <!-- 密码输入 -->
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
            <span class="flex items-center gap-2">
              <span>🔒</span>
              <span>密码</span>
            </span>
          </label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            required
            autocomplete="new-password"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.password }"
            placeholder="请输入密码（至少8位）"
            @blur="validatePassword"
          />
          <p v-if="errors.password" class="mt-1 text-sm text-red-600">
            {{ errors.password }}
          </p>
          <p v-else class="mt-1 text-xs text-gray-500">
            密码至少需要 8 个字符
          </p>
        </div>

        <!-- 确认密码输入 -->
        <div>
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
            <span class="flex items-center gap-2">
              <span>🔐</span>
              <span>确认密码</span>
            </span>
          </label>
          <input
            id="confirmPassword"
            v-model="form.confirmPassword"
            type="password"
            required
            autocomplete="new-password"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.confirmPassword }"
            placeholder="请再次输入密码"
            @blur="validateConfirmPassword"
          />
          <p v-if="errors.confirmPassword" class="mt-1 text-sm text-red-600">
            {{ errors.confirmPassword }}
          </p>
        </div>

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="p-4 bg-red-50 border border-red-200 rounded-lg">
          <p class="text-sm text-red-600">{{ errorMessage }}</p>
        </div>

        <!-- 提交按钮 -->
        <button
          type="submit"
          :disabled="loading"
          class="w-full py-3 px-4 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold rounded-lg transition disabled:opacity-50 disabled:cursor-not-allowed shadow-lg hover:shadow-xl transform hover:scale-105"
        >
          <span v-if="loading">注册中...</span>
          <span v-else>立即注册 →</span>
        </button>
      </form>

      <!-- 登录链接 -->
      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          已有账号？
          <router-link to="/login" class="text-blue-600 hover:text-blue-500 font-medium transition">
            立即登录
          </router-link>
        </p>
      </div>
    </div>
  </AuthLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { register } from '@/api/auth'
import AuthLayout from '@/layouts/AuthLayout.vue'

const router = useRouter()
const authStore = useAuthStore()

// 表单数据
const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 表单错误
const errors = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 加载状态
const loading = ref(false)
// 错误消息
const errorMessage = ref('')

/**
 * 验证用户名
 */
const validateUsername = () => {
  if (!form.username) {
    errors.username = '请输入用户名'
    return false
  }
  if (form.username.length < 2) {
    errors.username = '用户名至少需要 2 个字符'
    return false
  }
  errors.username = ''
  return true
}

/**
 * 验证邮箱格式
 */
const validateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!form.email) {
    errors.email = '请输入邮箱'
    return false
  }
  if (!emailRegex.test(form.email)) {
    errors.email = '邮箱格式不正确'
    return false
  }
  errors.email = ''
  return true
}

/**
 * 验证密码
 */
const validatePassword = () => {
  if (!form.password) {
    errors.password = '请输入密码'
    return false
  }
  if (form.password.length < 8) {
    errors.password = '密码至少需要 8 个字符'
    return false
  }
  errors.password = ''
  return true
}

/**
 * 验证确认密码
 */
const validateConfirmPassword = () => {
  if (!form.confirmPassword) {
    errors.confirmPassword = '请再次输入密码'
    return false
  }
  if (form.password !== form.confirmPassword) {
    errors.confirmPassword = '两次输入的密码不一致'
    return false
  }
  errors.confirmPassword = ''
  return true
}

/**
 * 表单提交
 */
const handleSubmit = async () => {
  // 清除之前的错误
  errorMessage.value = ''
  
  // 验证表单
  const isUsernameValid = validateUsername()
  const isEmailValid = validateEmail()
  const isPasswordValid = validatePassword()
  const isConfirmPasswordValid = validateConfirmPassword()
  
  if (!isUsernameValid || !isEmailValid || !isPasswordValid || !isConfirmPasswordValid) {
    return
  }

  loading.value = true

  try {
    // 调用注册 API
    const response = await register({
      username: form.username,
      email: form.email,
      password: form.password
    })

    // 从 ApiResult 中提取实际数据
    const registerData = response.data.data

    // 注册成功后自动登录
    authStore.login(
      {
        accessToken: registerData.accessToken,
        refreshToken: registerData.refreshToken
      },
      {
        id: registerData.user.id,
        username: registerData.user.username,
        email: registerData.user.email,
        totalScore: registerData.user.totalScore,
        streak: registerData.user.streak,
        role: registerData.user.role
      }
    )

    // 根据用户角色跳转到不同页面
    if (registerData.user.role === 'ADMIN') {
      router.push('/admin')
    } else {
      router.push('/')
    }
  } catch (error: any) {
    // 处理错误
    if (error.response?.status === 409) {
      errorMessage.value = '该邮箱已被注册'
    } else if (error.response?.status === 422) {
      errorMessage.value = error.response.data.message || '输入信息格式不正确'
    } else if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = '注册失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script>
