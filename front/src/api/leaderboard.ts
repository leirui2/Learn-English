import apiClient from './auth'

export interface LeaderboardEntryDto {
  rank: number
  userId: string
  username: string
  score: number
  streak: number
  isCurrentUser: boolean
}

export interface LeaderboardResponse {
  type: string
  period: string
  categoryId: number | null
  entries: LeaderboardEntryDto[]
  currentUserRank: LeaderboardEntryDto | null
}

export const getLeaderboard = (params: {
  type?: 'score' | 'streak'
  period?: 'week' | 'all'
  category?: number
}) => {
  return apiClient.get<LeaderboardResponse>('/leaderboard', { params })
}
