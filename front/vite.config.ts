import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    // Vue 插件，支持 .vue 单文件组件
    vue()
  ],
  resolve: {
    alias: {
      // 路径别名：@ 指向 src 目录，简化导入路径
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    // 开发服务器端口
    port: 5173,
    proxy: {
      // 将 /api 开头的请求代理到后端服务
      '/api': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        // 去掉 /api 前缀转发给后端
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
