<template>
  <AuthLayout>
    <div class="bg-white rounded-lg shadow-md p-8 md:p-10">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900">忘记密码</h1>
        <p class="mt-2 text-sm text-gray-600">输入你的邮箱，我们将发送重置链接</p>
      </div>

      <!-- 成功提示 -->
      <div v-if="success" class="space-y-6">
        <div class="p-4 bg-green-50 border border-green-200 rounded-lg">
          <div class="flex items-start">
            <svg class="w-5 h-5 text-green-600 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
            <div class="ml-3">
              <h3 class="text-sm font-medium text-green-800">邮件已发送</h3>
              <p class="mt-1 text-sm text-green-700">
                我们已向 <strong>{{ form.email }}</strong> 发送了密码重置链接，请查收邮件并点击链接重置密码。
              </p>
              <p class="mt-2 text-xs text-green-600">
                如果没有收到邮件，请检查垃圾邮件文件夹，或稍后重试。
              </p>
            </div>
          </div>
        </div>

        <router-link
          to="/login"
          class="block w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg text-center transition"
        >
          返回登录
        </router-link>
      </div>

      <!-- 申请表单 -->
      <form v-else @submit.prevent="handleSubmit" class="space-y-6">
        <!-- 邮箱输入 -->
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
            邮箱
          </label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            required
            autocomplete="email"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
            :class="{ 'border-red-500': errors.email }"
            placeholder="请输入注册时使用的邮箱"
            @blur="validateEmail"
          />
          <p v-if="errors.email" class="mt-1 text-sm text-red-600">
            {{ errors.email }}
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
          <span v-if="loading">发送中...</span>
          <span v-else>发送重置链接</span>
        </button>

        <!-- 返回登录 -->
        <div class="text-center">
          <router-link
            to="/login"
            class="text-sm text-blue-600 hover:text-blue-500 transition"
          >
            返回登录
          </router-link>
        </div>
      </form>
    </div>
  </AuthLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { forgotPassword } from '@/api/auth'
import AuthLayout from '@/layouts/AuthLayout.vue'

// 表单数据
const form = reactive({
  email: ''
})

// 表单错误
const errors = reactive({
  email: ''
})

// 加载状态
const loading = ref(false)
// 错误消息
const errorMessage = ref('')
// 成功状态
const success = ref(false)

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
 * 表单提交
 */
const handleSubmit = async () => {
  // 清除之前的错误
  errorMessage.value = ''
  
  // 验证表单
  if (!validateEmail()) {
    return
  }

  loading.value = true

  try {
    // 调用申请密码重置 API
    await forgotPassword({
      email: form.email
    })

    // 显示成功提示
    success.value = true
  } catch (error: any) {
    // 处理错误
    if (error.response?.status === 404) {
      errorMessage.value = '该邮箱未注册'
    } else if (error.response?.status === 429) {
      errorMessage.value = '发送过于频繁，请稍后再试'
    } else if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = '发送失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script>
