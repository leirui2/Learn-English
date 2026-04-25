import apiClient from './auth'

export interface UserListDto {
  id: string
  username: string
  email: string
  role: string
  status: string
  totalScore: number
  streak: number
  createdAt: string
  bannedAt?: string
  bannedReason?: string
}

export interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

// 获取用户列表
export const getAdminUsers = (params: {
  keyword?: string
  role?: string
  status?: string
  page?: number
  size?: number
}) => {
  return apiClient.get<PageResult<UserListDto>>('/admin/users', { params })
}

// 封禁用户
export const banUser = (id: string, reason: string) => {
  return apiClient.put(`/admin/users/${id}/ban`, { reason })
}

// 解封用户
export const unbanUser = (id: string) => {
  return apiClient.put(`/admin/users/${id}/unban`)
}

// 删除用户
export const deleteUser = (id: string) => {
  return apiClient.delete(`/admin/users/${id}`)
}

// 重置密码
export const resetUserPassword = (id: string, newPassword: string) => {
  return apiClient.post(`/admin/users/${id}/reset-password`, { newPassword })
}

// 修改角色
export const updateUserRole = (id: string, role: string) => {
  return apiClient.put(`/admin/users/${id}/role`, { role })
}

// ===== 分类管理 =====
export interface CategoryDto {
  id: number
  name: string
  description: string
  difficulty: number
  difficultyLevel?: number
}

export const getAdminCategories = () =>
  apiClient.get<CategoryDto[]>('/admin/categories')

export const createCategory = (data: { name: string; description: string; difficulty: number }) =>
  apiClient.post<CategoryDto>('/admin/categories', { name: data.name, description: data.description, difficultyLevel: data.difficulty })

export const updateCategory = (id: number, data: { name: string; description: string; difficulty: number }) =>
  apiClient.put<CategoryDto>(`/admin/categories/${id}`, { name: data.name, description: data.description, difficultyLevel: data.difficulty })

export const deleteCategory = (id: number) =>
  apiClient.delete(`/admin/categories/${id}`)

// ===== 关卡管理 =====
export interface LevelDto {
  id: number
  categoryId: number
  categoryName: string
  name: string
  description: string
  orderIndex: number
  standardTime: number  // 秒
  exerciseCount?: number
}

export const getAdminLevels = (categoryId?: number) =>
  apiClient.get<LevelDto[]>('/admin/levels', { params: categoryId ? { categoryId } : {} })

export const createLevel = (data: { categoryId: number; name: string; description: string; levelOrder: number; standardTimeMs: number }) =>
  apiClient.post<LevelDto>('/admin/levels', {
    categoryId: data.categoryId,
    name: data.name,
    description: data.description,
    orderIndex: data.levelOrder,
    standardTime: data.standardTimeMs  // 前端传的已经是秒
  })

export const updateLevel = (id: number, data: { categoryId: number; name: string; description: string; levelOrder: number; standardTimeMs: number }) =>
  apiClient.put<LevelDto>(`/admin/levels/${id}`, {
    categoryId: data.categoryId,
    name: data.name,
    description: data.description,
    orderIndex: data.levelOrder,
    standardTime: data.standardTimeMs
  })

export const deleteLevel = (id: number) =>
  apiClient.delete(`/admin/levels/${id}`)

// ===== 操作日志 =====
export interface AdminOperationLogDto {
  id: number
  operatorId: string
  operatorName: string
  operationType: string
  targetType: string
  targetId: string
  operationDetail: string
  ipAddress: string
  createdAt: string
}

export const getAdminLogs = (params: {
  operatorId?: string
  operationType?: string
  targetType?: string
  startDate?: string
  endDate?: string
  page?: number
  size?: number
}) => apiClient.get<PageResult<AdminOperationLogDto>>('/admin/logs', { params })

// ===== 练习内容管理 =====
export interface ExerciseDto {
  id: number
  levelId: number
  levelName: string
  contentEn: string
  contentZh: string
  orderIndex: number
}

export const getAdminExercises = (levelId?: number) =>
  apiClient.get<ExerciseDto[]>('/admin/exercises', { params: levelId ? { levelId } : {} })

export const createExercise = (data: { levelId: number; contentEn: string; contentZh: string; orderIndex: number }) =>
  apiClient.post<ExerciseDto>('/admin/exercises', data)

export const batchCreateExercises = (levelId: number, exercises: { contentEn: string; contentZh: string; orderIndex: number }[]) =>
  apiClient.post('/admin/exercises/batch', { levelId, exercises })

export const updateExercise = (id: number, data: { levelId: number; contentEn: string; contentZh: string; orderIndex: number }) =>
  apiClient.put<ExerciseDto>(`/admin/exercises/${id}`, data)

export const deleteExercise = (id: number) =>
  apiClient.delete(`/admin/exercises/${id}`)
