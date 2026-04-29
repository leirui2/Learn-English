import apiClient from './auth'

// 后端统一响应格式
interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// 道具状态枚举
export enum GiftStatus {
  ON_SHELF = 'ON_SHELF',
  OFF_SHELF = 'OFF_SHELF'
}

// 道具状态中文映射
export const GiftStatusLabels: Record<GiftStatus, string> = {
  [GiftStatus.ON_SHELF]: '上架中',
  [GiftStatus.OFF_SHELF]: '已下架'
}

// 用户道具状态枚举
export enum UserItemStatus {
  UNUSED = 'UNUSED',
  USED = 'USED'
}

// 用户道具状态中文映射
export const UserItemStatusLabels: Record<UserItemStatus, string> = {
  [UserItemStatus.UNUSED]: '未使用',
  [UserItemStatus.USED]: '已使用'
}

// 道具 DTO
export interface GiftDto {
  id: number
  name: string
  description: string
  icon: string | null
  pointsCost: number
  stock: number
  status: GiftStatus
  createdAt: string
  updatedAt: string
}

// 道具分页响应
export interface GiftPageResponse {
  content: GiftDto[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

// 用户道具 DTO
export interface UserItemDto {
  id: number
  giftId: number
  giftName: string
  giftIcon: string | null
  status: UserItemStatus
  redeemedAt: string
  usedAt: string | null
}

// 用户道具分页响应
export interface UserItemPageResponse {
  content: UserItemDto[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

// 兑换响应
export interface ExchangeGiftResponse {
  success: boolean
  giftName: string
  pointsSpent: number
  remainingPoints: number
  message: string
}

// 创建道具请求（管理员）
export interface CreateGiftRequest {
  name: string
  description: string
  icon?: string
  pointsCost: number
  stock: number
}

// 更新道具请求（管理员）
export interface UpdateGiftRequest {
  name?: string
  description?: string
  icon?: string
  pointsCost?: number
  stock?: number
}

// ===== 用户端接口 =====

/**
 * 获取道具商城列表（仅上架的道具）
 * GET /gifts
 */
export const getGifts = (page: number = 0, size: number = 20) => {
  return apiClient.get<ApiResult<GiftPageResponse>>('/gifts', {
    params: { page, size }
  })
}

/**
 * 获取道具详情
 * GET /gifts/{id}
 */
export const getGiftDetail = (id: number) => {
  return apiClient.get<ApiResult<GiftDto>>(`/gifts/${id}`)
}

/**
 * 兑换道具
 * POST /gifts/{id}/exchange
 */
export const exchangeGift = (giftId: number) => {
  return apiClient.post<ApiResult<ExchangeGiftResponse>>(`/gifts/${giftId}/exchange`)
}

/**
 * 获取我的道具背包
 * GET /gifts/user/items
 */
export const getMyItems = () => {
  return apiClient.get<ApiResult<UserItemDto[]>>('/gifts/user/items')
}

/**
 * 使用道具
 * POST /gifts/user/items/{id}/use
 */
export const useItem = (userItemId: number) => {
  return apiClient.post<ApiResult<void>>(`/gifts/user/items/${userItemId}/use`)
}

/**
 * 获取我的兑换记录（分页）
 * GET /gifts/user/exchange-records
 */
export const getExchangeRecords = (page: number = 0, size: number = 20) => {
  return apiClient.get<ApiResult<UserItemPageResponse>>('/gifts/user/exchange-records', {
    params: { page, size }
  })
}

// ===== 管理员接口 =====

/**
 * 管理员获取所有道具（分页）
 * GET /admin/gifts
 */
export const getAdminGifts = (page: number = 0, size: number = 20) => {
  return apiClient.get<ApiResult<GiftPageResponse>>('/admin/gifts', {
    params: { page, size }
  })
}

/**
 * 管理员获取道具详情
 * GET /admin/gifts/{id}
 */
export const getAdminGiftDetail = (id: number) => {
  return apiClient.get<ApiResult<GiftDto>>(`/admin/gifts/${id}`)
}

/**
 * 管理员创建道具
 * POST /admin/gifts
 */
export const createGift = (data: CreateGiftRequest) => {
  return apiClient.post<ApiResult<GiftDto>>('/admin/gifts', data)
}

/**
 * 管理员更新道具
 * PUT /admin/gifts/{id}
 */
export const updateGift = (id: number, data: UpdateGiftRequest) => {
  return apiClient.put<ApiResult<GiftDto>>(`/admin/gifts/${id}`, data)
}

/**
 * 管理员删除道具
 * DELETE /admin/gifts/{id}
 */
export const deleteGift = (id: number) => {
  return apiClient.delete<ApiResult<void>>(`/admin/gifts/${id}`)
}

/**
 * 管理员上架道具
 * PUT /admin/gifts/{id}/on-shelf
 */
export const onShelfGift = (id: number) => {
  return apiClient.put<ApiResult<void>>(`/admin/gifts/${id}/on-shelf`)
}

/**
 * 管理员下架道具
 * PUT /admin/gifts/{id}/off-shelf
 */
export const offShelfGift = (id: number) => {
  return apiClient.put<ApiResult<void>>(`/admin/gifts/${id}/off-shelf`)
}

/**
 * 管理员上传道具图片
 * POST /admin/gifts/{id}/image
 */
export const uploadGiftImage = (giftId: number, file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return apiClient.post<ApiResult<string>>(`/admin/gifts/${giftId}/image`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}