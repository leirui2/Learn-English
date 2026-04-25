<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <main>
      <slot />
    </main>
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
</script>
