import apiClient from './auth'

export interface ChallengeExercise {
  id: number
  contentEn: string
  contentZh: string
}

export interface SubmitChallengeRequest {
  contentType: 'WORD' | 'SENTENCE'
  timeMode: 'TIMED' | 'INFINITE'
  timeLimit?: number
  passedCount: number
  accuracy: number
  timeMs?: number
  wpm: number
}

export interface ChallengeLeaderboardEntry {
  rank: number
  userId: string
  username: string
  passedCount: number
  accuracy: number
  timeMs?: number
  wpm: number
  createdAt: string
  isCurrentUser: boolean
}

export interface ChallengeRecord {
  id: number
  contentType: string
  timeMode: string
  timeLimit?: number
  passedCount: number
  accuracy: number
  timeMs?: number
  wpm: number
  createdAt: string
}

export const getRandomExercises = (type: 'WORD' | 'SENTENCE', count = 20) =>
  apiClient.get<ChallengeExercise[]>('/challenge/exercises', { params: { type, count } })

export const submitChallenge = (data: SubmitChallengeRequest) =>
  apiClient.post('/challenge/submit', data)

export const getChallengeLeaderboard = (params: {
  type: 'WORD' | 'SENTENCE'
  mode: 'TIMED' | 'INFINITE'
  timeLimit?: number
}) => apiClient.get<ChallengeLeaderboardEntry[]>('/challenge/leaderboard', { params })

export const getChallengeHistory = (page = 0, size = 10) =>
  apiClient.get<ChallengeRecord[]>('/challenge/history', { params: { page, size } })
