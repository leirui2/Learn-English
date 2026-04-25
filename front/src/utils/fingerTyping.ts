// =====================================================
// 指法训练工具函数
// Feature: english-typing-learning
// =====================================================

export interface KeyStat {
  correct: number
  incorrect: number
  confidence: number
}

export interface FingerTypingProgress {
  unlockedKeys: string[]
  keyStats: Record<string, KeyStat>
  totalSessions: number
  lastPracticeAt: string
}

// 按键解锁顺序（每组解锁的按键）
export const UNLOCK_GROUPS: string[][] = [
  ['f', 'j'],
  ['d', 'k'],
  ['s', 'l'],
  ['a', ';'],
  ['g', 'h'],
  ['e', 'i'],
  ['r', 'u'],
  ['w', 'o'],
  ['q', 'p'],
  ['t', 'y'],
  ['v', 'm'],
  ['c', ','],
  ['x', '.'],
  ['z', '/'],
  ['b', 'n'],
  [' '],
]

// 所有按键（按顺序展开）
export const ALL_KEYS = UNLOCK_GROUPS.flat()

// 手指分区颜色映射
export const FINGER_COLORS: Record<string, string> = {
  // 左手小指
  q: 'bg-pink-200', a: 'bg-pink-200', z: 'bg-pink-200',
  // 左手无名指
  w: 'bg-purple-200', s: 'bg-purple-200', x: 'bg-purple-200',
  // 左手中指
  e: 'bg-blue-200', d: 'bg-blue-200', c: 'bg-blue-200',
  // 左手食指
  r: 'bg-green-200', f: 'bg-green-200', v: 'bg-green-200',
  t: 'bg-green-200', g: 'bg-green-200', b: 'bg-green-200',
  // 右手食指
  y: 'bg-yellow-200', h: 'bg-yellow-200', n: 'bg-yellow-200',
  u: 'bg-yellow-200', j: 'bg-yellow-200', m: 'bg-yellow-200',
  // 右手中指
  i: 'bg-orange-200', k: 'bg-orange-200', ',': 'bg-orange-200',
  // 右手无名指
  o: 'bg-red-200', l: 'bg-red-200', '.': 'bg-red-200',
  // 右手小指
  p: 'bg-rose-200', ';': 'bg-rose-200', '/': 'bg-rose-200',
  // 拇指
  ' ': 'bg-gray-200',
}

// 手指名称
export const FINGER_NAMES: Record<string, string> = {
  q: '左小指', a: '左小指', z: '左小指',
  w: '左无名指', s: '左无名指', x: '左无名指',
  e: '左中指', d: '左中指', c: '左中指',
  r: '左食指', f: '左食指', v: '左食指',
  t: '左食指', g: '左食指', b: '左食指',
  y: '右食指', h: '右食指', n: '右食指',
  u: '右食指', j: '右食指', m: '右食指',
  i: '右中指', k: '右中指', ',': '右中指',
  o: '右无名指', l: '右无名指', '.': '右无名指',
  p: '右小指', ';': '右小指', '/': '右小指',
  ' ': '拇指',
}

const STORAGE_KEY = 'finger_typing_progress'

// 获取带用户 ID 的存储 key
function getStorageKey(): string {
  try {
    const userInfo = localStorage.getItem('user_info')
    if (userInfo) {
      const user = JSON.parse(userInfo)
      if (user?.id) return `finger_typing_progress_${user.id}`
    }
  } catch {}
  return STORAGE_KEY
}

// 计算置信度
export function calcConfidence(correct: number, incorrect: number): number {
  if (correct === 0 && incorrect === 0) return 0
  return Math.min(100, Math.round(correct / (correct + incorrect * 2) * 100))
}

// 初始化进度
export function initProgress(): FingerTypingProgress {
  return {
    unlockedKeys: [...UNLOCK_GROUPS[0]], // 初始解锁 f, j
    keyStats: {},
    totalSessions: 0,
    lastPracticeAt: new Date().toISOString()
  }
}

// 从 localStorage 加载进度
export function loadProgress(): FingerTypingProgress {
  try {
    const raw = localStorage.getItem(getStorageKey())
    if (raw) return JSON.parse(raw)
  } catch {}
  return initProgress()
}

// 保存进度到 localStorage
export function saveProgress(progress: FingerTypingProgress): void {
  progress.lastPracticeAt = new Date().toISOString()
  localStorage.setItem(getStorageKey(), JSON.stringify(progress))
}

// 重置全部进度
export function resetProgress(): FingerTypingProgress {
  const p = initProgress()
  saveProgress(p)
  return p
}

// 获取某个按键的统计，不存在则初始化
export function getKeyStat(progress: FingerTypingProgress, key: string): KeyStat {
  if (!progress.keyStats[key]) {
    progress.keyStats[key] = { correct: 0, incorrect: 0, confidence: 0 }
  }
  return progress.keyStats[key]
}

// 记录按键输入
export function recordKeyInput(
  progress: FingerTypingProgress,
  key: string,
  isCorrect: boolean
): FingerTypingProgress {
  const stat = getKeyStat(progress, key)
  if (isCorrect) {
    stat.correct++
  } else {
    stat.incorrect++
  }
  stat.confidence = calcConfidence(stat.correct, stat.incorrect)
  return { ...progress, keyStats: { ...progress.keyStats, [key]: stat } }
}

// 检查是否可以解锁下一组按键
export function checkUnlock(progress: FingerTypingProgress): FingerTypingProgress {
  const unlocked = progress.unlockedKeys
  // 检查所有已解锁按键置信度是否都 >= 100
  const allMastered = unlocked.every(key => {
    const stat = progress.keyStats[key]
    return stat && stat.confidence >= 100
  })

  if (!allMastered) return progress

  // 找到下一组未解锁的按键
  const nextGroup = UNLOCK_GROUPS.find(group =>
    group.some(key => !unlocked.includes(key))
  )

  if (!nextGroup) return progress // 已全部解锁

  const newUnlocked = [...unlocked, ...nextGroup.filter(k => !unlocked.includes(k))]
  return { ...progress, unlockedKeys: newUnlocked }
}

// 生成练习序列（20-30个字符）
export function generateSequence(
  progress: FingerTypingProgress,
  length: number = 25
): string {
  const unlocked = progress.unlockedKeys
  if (unlocked.length === 0) return 'fj'.repeat(length / 2)

  // 计算每个按键的权重（置信度越低，权重越高）
  const weights: { key: string; weight: number }[] = unlocked.map(key => {
    const stat = progress.keyStats[key]
    const confidence = stat ? stat.confidence : 0
    // 权重 = 100 - confidence + 10（保证最小权重）
    return { key, weight: Math.max(10, 100 - confidence + 10) }
  })

  const totalWeight = weights.reduce((sum, w) => sum + w.weight, 0)
  const sequence: string[] = []

  for (let i = 0; i < length; i++) {
    // 按权重随机选择按键
    let rand = Math.random() * totalWeight
    let selected = weights[0].key
    for (const w of weights) {
      rand -= w.weight
      if (rand <= 0) {
        selected = w.key
        break
      }
    }
    sequence.push(selected)
  }

  return sequence.join('')
}

// 获取按键状态颜色（用于热力图）
export function getKeyStatusClass(
  progress: FingerTypingProgress,
  key: string,
  currentKey: string
): string {
  if (key === currentKey) return 'ring-2 ring-blue-500 bg-blue-400 text-white scale-110'

  if (!progress.unlockedKeys.includes(key)) {
    return 'bg-gray-200 text-gray-400 cursor-not-allowed'
  }

  const stat = progress.keyStats[key]
  if (!stat || stat.confidence === 0) return 'bg-white border-gray-300'

  if (stat.confidence >= 90) return 'bg-green-400 text-white'
  if (stat.confidence >= 60) return 'bg-yellow-400 text-white'
  return 'bg-red-400 text-white'
}
