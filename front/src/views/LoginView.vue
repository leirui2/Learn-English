<template>
  <AuthLayout>
    <div class="bg-white rounded-lg shadow-md p-8 md:p-10">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900">登录</h1>
        <p class="mt-2 text-sm text-gray-600">欢迎回来！继续你的英语学习之旅</p>
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- 用户名或邮箱输入 -->
        <div>
          <label for="identifier" class="block text-sm font-medium text-gray-700 mb-2">
            用户名或邮箱
          </label>
          <input
            id="identifier"
            v-model="form.identifier"
            type="text"
            required
            autocomplete="username"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.identifier }"
            placeholder="请输入用户名或邮箱"
            @blur="validateIdentifier"
          />
          <p v-if="errors.identifier" class="mt-1 text-sm text-red-600">
            {{ errors.identifier }}
          </p>
        </div>

        <!-- 密码输入 -->
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
            密码
          </label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            required
            autocomplete="current-password"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.password }"
            placeholder="请输入密码"
          />
          <p v-if="errors.password" class="mt-1 text-sm text-red-600">
            {{ errors.password }}
          </p>
        </div>

        <!-- 忘记密码链接 -->
        <div class="flex items-center justify-end">
          <router-link
            to="/forgot-password"
            class="text-sm text-blue-600 hover:text-blue-500 transition"
          >
            忘记密码？
          </router-link>
        </div>

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="p-4 bg-red-50 border border-red-200 rounded-lg">
          <p class="text-sm text-red-600">{{ errorMessage }}</p>
        </div>

        <!-- 提交按钮 -->
        <button
          type="submit"
          :disabled="loading"
          class="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <span v-if="loading">登录中...</span>
          <span v-else>登录</span>
        </button>
      </form>

      <!-- 注册链接 -->
      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          还没有账号？
          <router-link to="/register" class="text-blue-600 hover:text-blue-500 font-medium transition">
            立即注册
          </router-link>
        </p>
      </div>
    </div>
  </AuthLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { login } from '@/api/auth'
import AuthLayout from '@/layouts/AuthLayout.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

// 表单数据
const form = reactive({
  identifier: '',  // 用户名或邮箱
  password: ''
})

// 表单错误
const errors = reactive({
  identifier: '',
  password: ''
})

// 加载状态
const loading = ref(false)
// 错误消息
const errorMessage = ref('')

/**
 * 验证用户名或邮箱
 */
const validateIdentifier = () => {
  if (!form.identifier) {
    errors.identifier = '请输入用户名或邮箱'
    return false
  }
  if (form.identifier.trim().length < 3) {
    errors.identifier = '用户名或邮箱长度至少为 3 个字符'
    return false
  }
  errors.identifier = ''
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
  errors.password = ''
  return true
}

/**
 * 表单提交
 */
const handleSubmit = async () => {
  // 清除之前的错误
  errorMessage.value = ''
  
  // 验证表单
  const isIdentifierValid = validateIdentifier()
  const isPasswordValid = validatePassword()
  
  if (!isIdentifierValid || !isPasswordValid) {
    return
  }

  loading.value = true

  try {
    // 调用登录 API
    const response = await login({
      identifier: form.identifier,
      password: form.password
    })

    // 从 ApiResult 中提取实际数据
    const loginData = response.data.data

    // 保存登录信息到 store
    authStore.login(
      {
        accessToken: loginData.accessToken,
        refreshToken: loginData.refreshToken
      },
      {
        id: loginData.user.id,
        username: loginData.user.username,
        email: loginData.user.email,
        totalScore: loginData.user.totalScore,
        streak: loginData.user.streak,
        role: loginData.user.role
      }
    )

    // 根据用户角色跳转到不同页面
    if (loginData.user.role === 'ADMIN') {
      // 管理员跳转到管理后台
      router.push('/admin')
    } else {
      // 普通用户跳转到目标页面（如果有 redirect 参数）或主页
      const redirect = (route.query.redirect as string) || '/'
      router.push(redirect)
    }
  } catch (error: any) {
    // 处理错误
    if (error.response?.status === 404) {
      // 用户不存在
      errorMessage.value = error.response.data.message || '该账号尚未注册，请先注册'
    } else if (error.response?.status === 401) {
      // 密码错误
      errorMessage.value = error.response.data.message || '密码错误，请重试'
    } else if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = '登录失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script>
