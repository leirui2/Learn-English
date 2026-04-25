/** @type {import('tailwindcss').Config} */
export default {
  // 扫描所有 Vue 和 TypeScript 文件以生成样式
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}'
  ],
  theme: {
    extend: {
      // 使用 Tailwind 默认断点：
      // 默认（无前缀）：< 768px  → 手机竖屏
      // md: ≥ 768px             → 平板 / 手机横屏
      // lg: ≥ 1024px            → 桌面
      screens: {
        'md': '768px',
        'lg': '1024px'
      }
    }
  },
  plugins: []
}
