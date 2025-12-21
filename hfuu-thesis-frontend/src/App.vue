<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const isAuthenticated = ref(false)

// 模拟检查登录状态
onMounted(() => {
  const token = localStorage.getItem('token')
  isAuthenticated.value = !!token
})

// 登录处理
const handleLogin = () => {
  router.push('/login')
}

// 登出处理
const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  isAuthenticated.value = false
  ElMessage.success('登出成功')
  router.push('/login')
}
</script>

<template>
  <div class="app-container">
    <!-- 导航栏 -->
    <header class="app-header">
      <div class="header-left">
        <h1 class="app-title">哈尔滨金融学院毕业论文管理系统</h1>
      </div>
      <div class="header-right">
        <el-button v-if="!isAuthenticated" type="primary" @click="handleLogin">登录</el-button>
        <el-dropdown v-else>
          <el-button type="primary">
            {{ JSON.parse(localStorage.getItem('userInfo') || '{}').username || '用户' }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
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
