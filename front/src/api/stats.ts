import apiClient from './auth'

export interface UserStatsResponse {
  totalPracticeTimeMs: number
  totalCompletedLevels: number
  totalCharactersTyped: number
  totalScore: number
  streak: number
  username?: string
}

export interface WeakWordDto {
  word: string
  errorCount: number
}

/**
 * 获取用户个人统计数据
 */
export const getUserStats = () => {
  return apiClient.get<UserStatsResponse>('/stats/me')
}

/**
 * 获取指定用户名的公开统计数据
 */
export const getUserStatsByUsername = (username: string) => {
  return apiClient.get<UserStatsResponse>(`/stats/user/${username}`)
}

/**
 * 获取用户易错词汇 Top 10
 */
export const getWeakWords = () => {
  return apiClient.get<WeakWordDto[]>('/stats/weak-words')
}
