<template>
  <!-- 根组件：根据路由 meta.layout 决定使用哪个布局 -->
  <component :is="currentLayout" v-if="currentLayout">
    <router-view />
  </component>
  <!-- 无布局时直接渲染路由视图 -->
  <router-view v-else />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AuthLayout from '@/layouts/AuthLayout.vue'
import MainLayout from '@/layouts/MainLayout.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'

const route = useRoute()

const currentLayout = computed(() => {
  const layout = route.meta.layout
  if (layout === 'auth') return AuthLayout
  if (layout === 'main') return MainLayout
  if (layout === 'admin') return AdminLayout
  return null
})
</script>
