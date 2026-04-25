import apiClient from './auth'

// 后端统一响应格式
interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// 分类信息
export interface Category {
  id: number
  name: string
  description: string
  difficulty: number
}

// 关卡信息
export interface Level {
  id: number
  categoryId: number
  name: string
  description: string
  levelOrder: number
  standardTimeMs: number
  unlocked: boolean
  completed: boolean
  bestAccuracy?: number
}

// 分类及其关卡列表
export interface CategoryWithLevels {
  category: Category
  levels: Level[]
}

// 练习内容
export interface Exercise {
  id: number
  levelId: number
  contentEn: string
  contentZh: string
  exerciseOrder: number
}

// 关卡详情（包含练习内容）
export interface LevelDetail {
  id: number
  categoryId: number
  name: string
  description: string
  levelOrder: number
  standardTimeMs: number
  unlocked: boolean
  completed: boolean
  bestAccuracy?: number
  exercises: Exercise[]
}

/**
 * 获取所有分类及其关卡列表
 * GET /levels
 */
export const getLevels = () => {
  return apiClient.get<ApiResult<CategoryWithLevels[]>>('/levels')
}

/**
 * 获取关卡详情（包含练习内容）
 * GET /levels/{id}
 */
export const getLevelDetail = (levelId: number) => {
  return apiClient.get<ApiResult<LevelDetail>>(`/levels/${levelId}`)
}

/**
 * 提交关卡完成
 * POST /levels/{id}/complete
 */
export interface CompleteLevelRequest {
  accuracy: number
  timeMs: number
  charactersTyped?: number
  errorChars?: Record<string, number>  // 单词 -> 错误次数
}

export interface CompleteLevelResponse {
  accuracy: number
  timeMs: number
  score: number
  nextLevelUnlocked: boolean  // 后端字段名
  nextLevelId?: number
  checkedIn?: boolean
  checkinBonus?: number
}

export const completeLevel = (levelId: number, data: CompleteLevelRequest) => {
  return apiClient.post<ApiResult<CompleteLevelResponse>>(`/levels/${levelId}/complete`, data)
}
