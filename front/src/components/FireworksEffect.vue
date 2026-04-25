<template>
  <canvas
    ref="canvasRef"
    class="fixed inset-0 pointer-events-none z-40"
    :width="width"
    :height="height"
  />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const canvasRef = ref<HTMLCanvasElement | null>(null)
const width = ref(window.innerWidth)
const height = ref(window.innerHeight)

let animationId: number
let ctx: CanvasRenderingContext2D | null = null

interface Particle {
  x: number
  y: number
  vx: number
  vy: number
  alpha: number
  color: string
  radius: number
}

interface Firework {
  x: number
  y: number
  targetY: number
  vy: number
  color: string
  exploded: boolean
  particles: Particle[]
}

const fireworks: Firework[] = []
const colors = ['#ff4444', '#ff8800', '#ffdd00', '#44ff44', '#44aaff', '#aa44ff', '#ff44aa', '#ffffff']

const createFirework = () => {
  const x = Math.random() * width.value
  const targetY = Math.random() * height.value * 0.5
  const color = colors[Math.floor(Math.random() * colors.length)]
  fireworks.push({
    x,
    y: height.value,
    targetY,
    vy: -(Math.random() * 8 + 10),
    color,
    exploded: false,
    particles: []
  })
}

const explode = (fw: Firework) => {
  fw.exploded = true
  const count = Math.floor(Math.random() * 40 + 60)
  for (let i = 0; i < count; i++) {
    const angle = (Math.PI * 2 * i) / count
    const speed = Math.random() * 5 + 2
    fw.particles.push({
      x: fw.x,
      y: fw.y,
      vx: Math.cos(angle) * speed,
      vy: Math.sin(angle) * speed,
      alpha: 1,
      color: fw.color,
      radius: Math.random() * 3 + 1
    })
  }
}

const animate = () => {
  if (!ctx) return
  ctx.fillStyle = 'rgba(0, 0, 0, 0.15)'
  ctx.fillRect(0, 0, width.value, height.value)

  fireworks.forEach((fw, fwIndex) => {
    if (!fw.exploded) {
      fw.y += fw.vy
      fw.vy += 0.3 // 重力

      // 画火箭轨迹
      ctx!.beginPath()
      ctx!.arc(fw.x, fw.y, 3, 0, Math.PI * 2)
      ctx!.fillStyle = fw.color
      ctx!.fill()

      if (fw.y <= fw.targetY) {
        explode(fw)
      }
    } else {
      // 更新粒子
      fw.particles.forEach((p, pIndex) => {
        p.x += p.vx
        p.y += p.vy
        p.vy += 0.1 // 重力
        p.vx *= 0.98 // 空气阻力
        p.alpha -= 0.02

        if (p.alpha > 0) {
          ctx!.beginPath()
          ctx!.arc(p.x, p.y, p.radius, 0, Math.PI * 2)
          ctx!.fillStyle = p.color
          ctx!.globalAlpha = p.alpha
          ctx!.fill()
          ctx!.globalAlpha = 1
        }
      })

      // 移除消失的粒子
      fw.particles = fw.particles.filter(p => p.alpha > 0)
    }
  })

  // 移除完成的烟花
  for (let i = fireworks.length - 1; i >= 0; i--) {
    if (fireworks[i].exploded && fireworks[i].particles.length === 0) {
      fireworks.splice(i, 1)
    }
  }

  animationId = requestAnimationFrame(animate)
}

const handleResize = () => {
  width.value = window.innerWidth
  height.value = window.innerHeight
}

onMounted(() => {
  const canvas = canvasRef.value
  if (!canvas) return
  ctx = canvas.getContext('2d')
  if (!ctx) return

  window.addEventListener('resize', handleResize)

  // 持续发射烟花，间隔随机
  let launchCount = 0
  const maxLaunches = 12

  const launch = () => {
    if (launchCount < maxLaunches) {
      createFirework()
      // 同时发射 1-3 个
      if (Math.random() > 0.5) createFirework()
      if (Math.random() > 0.7) createFirework()
      launchCount++
      setTimeout(launch, Math.random() * 300 + 150)
    }
  }

  launch()
  animate()
})

onUnmounted(() => {
  cancelAnimationFrame(animationId)
  window.removeEventListener('resize', handleResize)
  if (ctx) {
    ctx.clearRect(0, 0, width.value, height.value)
  }
})
</script>
