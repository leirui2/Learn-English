<template>
  <div class="min-h-screen bg-gray-50">
    <div class="p-6">
      <!-- 搜索和筛选 -->
      <div class="flex items-center justify-between mb-5">
        <h1 class="text-2xl font-bold text-gray-900">用户管理</h1>
      </div>
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6 flex flex-wrap gap-3">
        <input
          v-model="filters.keyword"
          type="text"
          placeholder="搜索用户名或邮箱..."
          class="flex-1 min-w-48 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          @input="debouncedSearch"
        />
        <select v-model="filters.role" @change="loadUsers" class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="">全部角色</option>
          <option value="USER">普通用户</option>
          <option value="ADMIN">管理员</option>
        </select>
        <select v-model="filters.status" @change="loadUsers" class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="">全部状态</option>
          <option value="ACTIVE">正常</option>
          <option value="BANNED">已封禁</option>
        </select>
        <button @click="loadUsers" class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm rounded-lg transition">
          搜索
        </button>
        <button @click="resetFilters" class="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm rounded-lg transition">
          重置
        </button>
      </div>

      <!-- 用户表格 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div v-if="loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>

        <div v-else-if="users.length === 0" class="text-center py-16 text-gray-500">
          暂无用户数据
        </div>

        <table v-else class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户名</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">邮箱</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden md:table-cell">角色</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden lg:table-cell">积分</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase hidden lg:table-cell">注册时间</th>
              <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr v-for="user in users" :key="user.id" class="hover:bg-gray-50 transition">
              <td class="px-4 py-3">
                <div class="flex items-center gap-2">
                  <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-white text-xs font-bold flex-shrink-0">
                    {{ user.username.charAt(0).toUpperCase() }}
                  </div>
                  <span class="text-sm font-medium text-gray-900">{{ user.username }}</span>
                </div>
              </td>
              <td class="px-4 py-3 text-sm text-gray-600 max-w-[120px] truncate">{{ user.email }}</td>
              <td class="px-4 py-3 hidden md:table-cell">
                <span :class="user.role === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-gray-100 text-gray-600'" class="px-2 py-0.5 rounded text-xs font-medium">
                  {{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}
                </span>
              </td>
              <td class="px-4 py-3">
                <span :class="user.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'" class="px-2 py-0.5 rounded text-xs font-medium whitespace-nowrap">
                  {{ user.status === 'ACTIVE' ? '正常' : '已封禁' }}
                </span>
              </td>
              <td class="px-4 py-3 text-sm text-gray-600 hidden lg:table-cell">{{ user.totalScore }}</td>
              <td class="px-4 py-3 text-sm text-gray-500 hidden lg:table-cell">{{ formatDate(user.createdAt) }}</td>
              <td class="px-4 py-3 text-right whitespace-nowrap">
                <div class="relative inline-block">
                  <button @click.stop="toggleMenu(user.id)" class="p-1.5 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded transition">
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                      <circle cx="5" cy="12" r="2"/><circle cx="12" cy="12" r="2"/><circle cx="19" cy="12" r="2"/>
                    </svg>
                  </button>
                  <div
                    v-if="openMenuId === user.id"
                    class="absolute right-0 mt-1 w-36 bg-white rounded-lg shadow-lg border border-gray-100 z-20 py-1"
                    @click.stop
                  >
                    <button @click="openDetail(user); openMenuId = null" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 flex items-center gap-2">
                      👁 查看详情
                    </button>
                    <button v-if="user.status === 'ACTIVE'" @click="openBanModal(user); openMenuId = null" class="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 flex items-center gap-2">
                      🚫 封禁
                    </button>
                    <button v-else @click="handleUnban(user); openMenuId = null" class="w-full text-left px-4 py-2 text-sm text-green-600 hover:bg-green-50 flex items-center gap-2">
                      ✅ 解封
                    </button>
                    <button @click="openResetPwdModal(user); openMenuId = null" class="w-full text-left px-4 py-2 text-sm text-yellow-600 hover:bg-yellow-50 flex items-center gap-2">
                      🔑 重置密码
                    </button>
                    <button @click="openDeleteModal(user); openMenuId = null" class="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 flex items-center gap-2">
                      🗑 删除
                    </button>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 分页 -->
        <div v-if="totalPages > 1" class="px-4 py-3 border-t border-gray-100 flex items-center justify-between">
          <span class="text-sm text-gray-500">共 {{ totalElements }} 条</span>
          <div class="flex gap-1">
            <button
              v-for="p in pageNumbers"
              :key="p"
              @click="currentPage = p; loadUsers()"
              :class="['px-3 py-1 text-sm rounded transition', currentPage === p ? 'bg-blue-600 text-white' : 'bg-gray-100 hover:bg-gray-200 text-gray-700']"
            >
              {{ p + 1 }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 用户详情弹窗 -->
    <div v-if="detailUser" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="detailUser = null">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
        <div class="flex items-center justify-between px-6 py-4 border-b">
          <h3 class="text-lg font-bold text-gray-900">用户详情</h3>
          <button @click="detailUser = null" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5 space-y-3">
          <div class="flex justify-between"><span class="text-sm text-gray-500">用户名</span><span class="text-sm font-medium">{{ detailUser.username }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">邮箱</span><span class="text-sm font-medium">{{ detailUser.email }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">角色</span><span class="text-sm font-medium">{{ detailUser.role === 'ADMIN' ? '管理员' : '普通用户' }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">状态</span><span class="text-sm font-medium">{{ detailUser.status === 'ACTIVE' ? '正常' : '已封禁' }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">总积分</span><span class="text-sm font-medium">{{ detailUser.totalScore }}</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">连续打卡</span><span class="text-sm font-medium">{{ detailUser.streak }} 天</span></div>
          <div class="flex justify-between"><span class="text-sm text-gray-500">注册时间</span><span class="text-sm font-medium">{{ formatDate(detailUser.createdAt) }}</span></div>
          <div v-if="detailUser.bannedReason" class="flex justify-between"><span class="text-sm text-gray-500">封禁原因</span><span class="text-sm font-medium text-red-600">{{ detailUser.bannedReason }}</span></div>
        </div>
        <div class="px-6 py-4 border-t flex justify-between gap-2">
          <select v-model="roleChangeValue" class="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm">
            <option value="USER">普通用户</option>
            <option value="ADMIN">管理员</option>
          </select>
          <button @click="handleRoleChange" class="px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white text-sm rounded-lg transition">修改角色</button>
        </div>
      </div>
    </div>

    <!-- 封禁弹窗 -->
    <div v-if="banTarget" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="banTarget = null">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-sm">
        <div class="px-6 py-4 border-b flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-900">封禁用户</h3>
          <button @click="banTarget = null" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5">
          <p class="text-sm text-gray-600 mb-3">确认封禁用户 <strong>{{ banTarget.username }}</strong>？</p>
          <textarea v-model="banReason" rows="3" placeholder="请输入封禁原因..." class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-red-500 resize-none" />
        </div>
        <div class="px-6 py-4 border-t flex gap-3 justify-end">
          <button @click="banTarget = null" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleBan" :disabled="!banReason.trim()" class="px-4 py-2 text-sm bg-red-600 hover:bg-red-700 disabled:opacity-50 text-white rounded-lg transition">确认封禁</button>
        </div>
      </div>
    </div>

    <!-- 重置密码弹窗 -->
    <div v-if="resetPwdTarget" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="resetPwdTarget = null">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-sm">
        <div class="px-6 py-4 border-b flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-900">重置密码</h3>
          <button @click="resetPwdTarget = null" class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        <div class="px-6 py-5">
          <p class="text-sm text-gray-600 mb-3">为用户 <strong>{{ resetPwdTarget.username }}</strong> 设置新密码</p>
          <input v-model="newPassword" type="password" placeholder="请输入新密码（至少8位）" class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div class="px-6 py-4 border-t flex gap-3 justify-end">
          <button @click="resetPwdTarget = null" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleResetPwd" :disabled="newPassword.length < 8" class="px-4 py-2 text-sm bg-yellow-600 hover:bg-yellow-700 disabled:opacity-50 text-white rounded-lg transition">确认重置</button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="deleteTarget" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4" @click.self="deleteTarget = null">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-sm">
        <div class="px-6 py-5 text-center">
          <div class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" /></svg>
          </div>
          <h3 class="text-lg font-bold text-gray-900 mb-2">确认删除</h3>
          <p class="text-sm text-gray-600">确认删除用户 <strong>{{ deleteTarget.username }}</strong>？此操作不可撤销，将级联删除所有相关数据。</p>
        </div>
        <div class="px-6 py-4 border-t flex gap-3 justify-center">
          <button @click="deleteTarget = null" class="px-5 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition">取消</button>
          <button @click="handleDelete" class="px-5 py-2 text-sm bg-red-600 hover:bg-red-700 text-white rounded-lg transition">确认删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getAdminUsers, banUser, unbanUser, deleteUser, resetUserPassword, updateUserRole } from '@/api/admin'
import type { UserListDto } from '@/api/admin'

const loading = ref(false)
const users = ref<UserListDto[]>([])
const totalElements = ref(0)
const totalPages = ref(0)
const currentPage = ref(0)

const filters = reactive({ keyword: '', role: '', status: '' })

// 弹窗状态
const detailUser = ref<UserListDto | null>(null)
const banTarget = ref<UserListDto | null>(null)
const banReason = ref('')
const resetPwdTarget = ref<UserListDto | null>(null)
const newPassword = ref('')
const deleteTarget = ref<UserListDto | null>(null)
const roleChangeValue = ref('USER')
const openMenuId = ref<string | null>(null)

let searchTimer: ReturnType<typeof setTimeout> | null = null
const debouncedSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    currentPage.value = 0
    loadUsers()
  }, 400)
}

const toggleMenu = (id: string) => {
  openMenuId.value = openMenuId.value === id ? null : id
}

const pageNumbers = computed(() => Array.from({ length: totalPages.value }, (_, i) => i))

const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getAdminUsers({
      keyword: filters.keyword || undefined,
      role: filters.role || undefined,
      status: filters.status || undefined,
      page: currentPage.value,
      size: 15
    })
    const data = (res.data as any).data
    users.value = data.content
    totalElements.value = data.totalElements
    totalPages.value = data.totalPages
  } catch (e) {
    console.error('加载用户失败:', e)
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  filters.keyword = ''
  filters.role = ''
  filters.status = ''
  currentPage.value = 0
  loadUsers()
}

const openDetail = (user: UserListDto) => {
  detailUser.value = user
  roleChangeValue.value = user.role
}

const openBanModal = (user: UserListDto) => {
  banTarget.value = user
  banReason.value = ''
}

const openResetPwdModal = (user: UserListDto) => {
  resetPwdTarget.value = user
  newPassword.value = ''
}

const openDeleteModal = (user: UserListDto) => {
  deleteTarget.value = user
}

const handleBan = async () => {
  if (!banTarget.value) return
  try {
    await banUser(banTarget.value.id, banReason.value)
    banTarget.value = null
    loadUsers()
  } catch (e: any) {
    alert(e.response?.data?.message || '封禁失败')
  }
}

const handleUnban = async (user: UserListDto) => {
  if (!confirm(`确认解封用户 ${user.username}？`)) return
  try {
    await unbanUser(user.id)
    loadUsers()
  } catch (e: any) {
    alert(e.response?.data?.message || '解封失败')
  }
}

const handleResetPwd = async () => {
  if (!resetPwdTarget.value) return
  try {
    await resetUserPassword(resetPwdTarget.value.id, newPassword.value)
    resetPwdTarget.value = null
    alert('密码重置成功')
  } catch (e: any) {
    alert(e.response?.data?.message || '重置失败')
  }
}

const handleDelete = async () => {
  if (!deleteTarget.value) return
  try {
    await deleteUser(deleteTarget.value.id)
    deleteTarget.value = null
    loadUsers()
  } catch (e: any) {
    alert(e.response?.data?.message || '删除失败')
  }
}

const handleRoleChange = async () => {
  if (!detailUser.value) return
  if (!confirm(`确认将 ${detailUser.value.username} 的角色修改为 ${roleChangeValue.value === 'ADMIN' ? '管理员' : '普通用户'}？`)) return
  try {
    await updateUserRole(detailUser.value.id, roleChangeValue.value)
    detailUser.value = null
    loadUsers()
  } catch (e: any) {
    alert(e.response?.data?.message || '修改失败')
  }
}

onMounted(() => {
  loadUsers()
  document.addEventListener('click', (e) => {
    openMenuId.value = null
  })
})
</script>
