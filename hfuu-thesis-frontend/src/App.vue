<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const isAuthenticated = ref(false)

// 模拟检查登录状态
onMounted(() => {
  let token = ''
  if (typeof localStorage !== 'undefined') {
    token = localStorage.getItem('token') || ''
  }
  isAuthenticated.value = !!token
})

// 登录处理
const handleLogin = () => {
  router.push('/login')
}

// 登出处理
const handleLogout = async () => {
  try {
    // 调用后端退出接口
    const token = typeof localStorage !== 'undefined' ? localStorage.getItem('token') : ''
    await fetch('/api/auth/logout', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
  } catch (error) {
    console.error('退出失败:', error)
  } finally {
    // 清除本地存储，确保localStorage存在
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('isLoggedIn')
      localStorage.removeItem('loginFailCount')
    }
    isAuthenticated.value = false
    ElMessage.success('登出成功')
    router.push('/login')
  }
}
</script>

<template>
  <div class="app-container">
    <!-- 导航栏 - 仅在非登录页面显示 -->
    <header v-if="$route.name !== 'login'" class="app-header">
      <div class="header-left">
        <h1 class="app-title">哈尔滨金融学院毕业论文管理系统</h1>
      </div>
      <div class="header-right">
        <el-dropdown v-if="isAuthenticated">
          <div class="user-info-container">
            <el-avatar class="user-avatar" :size="40">
              {{ JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').realName?.charAt(0) || '用' }}
            </el-avatar>
            <span class="user-name">{{ JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').realName || '用户' }}</span>
            <el-icon class="arrow-down"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleLogout" class="logout-item">
                <el-icon><switch-button /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="app-main">
      <router-view />
    </main>

    <!-- 页脚 -->
    <footer class="app-footer">
      <p>&copy; 2024 哈尔滨金融学院 毕业论文管理系统</p>
    </footer>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
}

.app-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #1989fa;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-info-container {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 20px;
  cursor: pointer;
  min-height: 44px;
  min-width: 44px;
  transition: all 0.3s ease;
}

.user-info-container:hover {
  background-color: rgba(25, 137, 250, 0.1);
}

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 18px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  line-height: 1.5;
}

.arrow-down {
  font-size: 16px;
  color: #606266;
}

.logout-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  padding: 12px 20px;
  min-height: 44px;
}

.logout-item:hover {
  background-color: rgba(25, 137, 250, 0.1);
}

.app-main {
  flex: 1;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.app-footer {
  text-align: center;
  padding: 10px 0;
  background-color: #ffffff;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  color: #909399;
}
</style>
