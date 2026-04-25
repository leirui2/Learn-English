<template>
  <AuthLayout>
    <div class="bg-white rounded-lg shadow-md p-8 md:p-10">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900">重置密码</h1>
        <p class="mt-2 text-sm text-gray-600">请输入新密码</p>
      </div>

      <!-- 成功提示 -->
      <div v-if="success" class="space-y-6">
        <div class="p-4 bg-green-50 border border-green-200 rounded-lg">
          <div class="flex items-start">
            <svg class="w-5 h-5 text-green-600 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
            <div class="ml-3">
              <h3 class="text-sm font-medium text-green-800">密码重置成功</h3>
              <p class="mt-1 text-sm text-green-700">
                你的密码已成功重置，请使用新密码登录。
              </p>
            </div>
          </div>
        </div>

        <router-link
          to="/login"
          class="block w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg text-center transition"
        >
          前往登录
        </router-link>
      </div>

      <!-- 重置表单 -->
      <form v-else @submit.prevent="handleSubmit" class="space-y-6">
        <!-- 新密码输入 -->
        <div>
          <label for="newPassword" class="block text-sm font-medium text-gray-700 mb-2">
            新密码
          </label>
          <input
            id="newPassword"
            v-model="form.newPassword"
            type="password"
            required
            autocomplete="new-password"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.newPassword }"
            placeholder="请输入新密码（至少8位）"
            @blur="validateNewPassword"
          />
          <p v-if="errors.newPassword" class="mt-1 text-sm text-red-600">
            {{ errors.newPassword }}
          </p>
          <p v-else class="mt-1 text-xs text-gray-500">
            密码至少需要 8 个字符
          </p>
        </div>

        <!-- 确认密码输入 -->
        <div>
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
            确认密码
          </label>
          <input
            id="confirmPassword"
            v-model="form.confirmPassword"
            type="password"
            required
            autocomplete="new-password"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.confirmPassword }"
            placeholder="请再次输入新密码"
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
          class="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <span v-if="loading">重置中...</span>
          <span v-else>重置密码</span>
        </button>
      </form>
    </div>
  </AuthLayout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { resetPassword } from '@/api/auth'
import AuthLayout from '@/layouts/AuthLayout.vue'

const route = useRoute()
const router = useRouter()

// 表单数据
const form = reactive({
  newPassword: '',
  confirmPassword: ''
})

// 表单错误
const errors = reactive({
  newPassword: '',
  confirmPassword: ''
})

// 加载状态
const loading = ref(false)
// 错误消息
const errorMessage = ref('')
// 成功状态
const success = ref(false)
// token
const token = ref('')

/**
 * 从 URL 获取 token
 */
onMounted(() => {
  token.value = route.query.token as string
  
  if (!token.value) {
    errorMessage.value = '链接已失效，请重新申请'
  }
})

/**
 * 验证新密码
 */
const validateNewPassword = () => {
  if (!form.newPassword) {
    errors.newPassword = '请输入新密码'
    return false
  }
  if (form.newPassword.length < 8) {
    errors.newPassword = '密码至少需要 8 个字符'
    return false
  }
  errors.newPassword = ''
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
  if (form.newPassword !== form.confirmPassword) {
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
  
  // 检查 token
  if (!token.value) {
    errorMessage.value = '链接已失效，请重新申请'
    return
  }
  
  // 验证表单
  const isNewPasswordValid = validateNewPassword()
  const isConfirmPasswordValid = validateConfirmPassword()
  
  if (!isNewPasswordValid || !isConfirmPasswordValid) {
    return
  }

  loading.value = true

  try {
    // 调用重置密码 API
    await resetPassword({
      token: token.value,
      newPassword: form.newPassword,
      confirmPassword: form.confirmPassword
    })

    // 显示成功提示
    success.value = true
  } catch (error: any) {
    // 处理错误
    if (error.response?.status === 400) {
      errorMessage.value = '链接已失效，请重新申请'
    } else if (error.response?.status === 422) {
      errorMessage.value = error.response.data.message || '密码格式不正确'
    } else if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = '重置失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script>
