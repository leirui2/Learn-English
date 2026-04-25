import apiClient from './auth'

export interface CheckinCalendarResponse {
  checkinDates: string[] // ISO date strings
  streak: number
}

export interface CheckinStreakResponse {
  streak: number
  lastCheckinDate: string // ISO datetime string
}

/**
 * 获取打卡日历（过去 30 天）
 */
export const getCheckinCalendar = () => {
  return apiClient.get<CheckinCalendarResponse>('/checkin/calendar')
}

/**
 * 获取当前连续打卡天数
 */
export const getCheckinStreak = () => {
  return apiClient.get<CheckinStreakResponse>('/checkin/streak')
}
