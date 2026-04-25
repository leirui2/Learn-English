import apiClient from './auth'

export interface UserProfileDto {
  id: string
  username: string
  nickname?: string
  email: string
  avatarUrl?: string
  bio?: string
  gender?: string
  phone?: string
  totalScore: number
  streak: number
  role: string
  status: string
}

export interface UpdateProfileRequest {
  nickname?: string
  bio?: string
  gender?: string
  phone?: string
}

export const getMyProfile = () => {
  return apiClient.get<UserProfileDto>('/users/profile')
}

export const updateProfile = (data: UpdateProfileRequest) => {
  return apiClient.put<UserProfileDto>('/users/profile', data)
}

export const uploadAvatar = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return apiClient.post<string>('/users/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
