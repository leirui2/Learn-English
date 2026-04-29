import apiClient from './auth'

// 后端统一响应格式
interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// 积分类型枚举
export enum PointsType {
  LEVEL_COMPLETE = 'LEVEL_COMPLETE',
  CHALLENGE = 'CHALLENGE',
  CHECKIN_BONUS = 'CHECKIN_BONUS',
  GIFT_EXCHANGE = 'GIFT_EXCHANGE',
  ADMIN_GRANT = 'ADMIN_GRANT',
  ACTIVITY_BONUS = 'ACTIVITY_BONUS'
}

// 积分类型中文映射
export const PointsTypeLabels: Record<PointsType, string> = {
  [PointsType.LEVEL_COMPLETE]: '关卡完成',
  [PointsType.CHALLENGE]: '天梯挑战',
  [PointsType.CHECKIN_BONUS]: '打卡奖励',
  [PointsType.GIFT_EXCHANGE]: '道具兑换',
  [PointsType.ADMIN_GRANT]: '管理员发放',
  [PointsType.ACTIVITY_BONUS]: '活动奖励'
}

// 积分记录 DTO
export interface PointsRecordDto {
  id: number
  points: number
  type: PointsType
  description: string
  relatedId: number | null
  balanceAfter: number
  createdAt: string
}

// 积分记录分页响应
export interface PointsRecordPageResponse {
  records: PointsRecordDto[]
  total: number
  page: number
  size: number
  totalPages: number
}

/**
 * 获取当前用户的积分记录
 * GET /points/records?page=0&size=20&type=LEVEL_COMPLETE
 *
 * @param page 页码（从0开始）
 * @param size 每页大小
 * @param type 积分类型（可选）
 */
export const getMyPointsRecords = (
  page: number = 0,
  size: number = 20,
  type?: PointsType
) => {
  const params: Record<string, string | number> = { page, size }
  if (type) {
    params.type = type
  }
  return apiClient.get<ApiResult<PointsRecordPageResponse>>('/points/records', { params })
}

/**
 * 管理员查询所有用户积分记录
 * GET /admin/points/records
 *
 * @param params 查询参数
 */
export interface AdminPointsRecordParams {
  page?: number
  size?: number
  userId?: string
  username?: string
  type?: PointsType
  startDate?: string
  endDate?: string
}

export const getAllPointsRecords = (params: AdminPointsRecordParams = {}) => {
  return apiClient.get<ApiResult<PointsRecordPageResponse>>('/admin/points/records', { params })
}
