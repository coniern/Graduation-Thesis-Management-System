<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 管理员统计数据
const adminStats = ref({
  totalUsers: 0,
  totalTeachers: 0,
  totalStudents: 0,
  totalTopics: 0,
  totalDocuments: 0,
  pendingReviews: 0
})

// 系统公告
const announcements = ref([
  { id: 1, title: '2025年毕业论文工作安排通知',发布Time: '2025-12-20 09:00' },
  { id: 2, title: '关于毕业论文格式规范的修订通知',发布Time: '2025-12-15 14:30' },
  { id: 3, title: '毕业论文系统升级公告',发布Time: '2025-12-10 16:45' }
])

// 加载管理员数据
const loadAdminData = async () => {
  // 模拟加载数据
  await new Promise(resolve => setTimeout(resolve, 500))
  adminStats.value = {
    totalUsers: 1250,
    totalTeachers: 120,
    totalStudents: 1130,
    totalTopics: 850,
    totalDocuments: 2500,
    pendingReviews: 120
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadAdminData()
})
</script>

<template>
  <div class="admin-home-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>管理员主页</h1>
      <p>欢迎回来，{{ JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').realName || '管理员' }}</p>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">总用户数</div>
            <div class="stat-value">{{ adminStats.totalUsers }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">教师数</div>
            <div class="stat-value">{{ adminStats.totalTeachers }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">学生数</div>
            <div class="stat-value">{{ adminStats.totalStudents }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">题目总数</div>
            <div class="stat-value">{{ adminStats.totalTopics }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">文档总数</div>
            <div class="stat-value">{{ adminStats.totalDocuments }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">待审核文档</div>
            <div class="stat-value">{{ adminStats.pendingReviews }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 系统公告 -->
      <el-col :xs="24" :md="12">
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">系统公告</h3>
              <el-button type="primary" size="small">发布公告</el-button>
            </div>
          </template>
          <div class="announcement-list">
            <div v-for="announcement in announcements" :key="announcement.id" class="announcement-item">
              <div class="announcement-title">{{ announcement.title }}</div>
              <div class="announcement-time">{{ announcement.发布Time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 快速操作 -->
      <el-col :xs="24" :md="12">
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">快速操作</h3>
            </div>
          </template>
          <div class="quick-actions">
            <div class="quick-action-item" @click="router.push('/user/manage')">
              <div class="quick-action-icon"><el-icon><user /></el-icon></div>
              <div class="quick-action-text">用户管理</div>
            </div>
            <div class="quick-action-item" @click="router.push('/department/manage')">
              <div class="quick-action-icon"><el-icon><office-building /></el-icon></div>
              <div class="quick-action-text">部门管理</div>
            </div>
            <div class="quick-action-item" @click="router.push('/statistics')">
              <div class="quick-action-icon"><el-icon><pie-chart /></el-icon></div>
              <div class="quick-action-text">数据统计</div>
            </div>
            <div class="quick-action-item" @click="router.push('/system/config')">
              <div class="quick-action-icon"><el-icon><setting /></el-icon></div>
              <div class="quick-action-text">系统配置</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 功能导航 -->
    <el-card class="function-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">功能导航</h3>
        </div>
      </template>
      <div class="function-grid">
        <div class="function-item" @click="router.push('/user/manage')">
          <div class="function-icon"><el-icon><user /></el-icon></div>
          <div class="function-name">用户管理</div>
        </div>
        <div class="function-item" @click="router.push('/department/manage')">
          <div class="function-icon"><el-icon><office-building /></el-icon></div>
          <div class="function-name">部门管理</div>
        </div>
        <div class="function-item" @click="router.push('/statistics')">
          <div class="function-icon"><el-icon><pie-chart /></el-icon></div>
          <div class="function-name">数据统计</div>
        </div>
        <div class="function-item" @click="router.push('/system/config')">
          <div class="function-icon"><el-icon><setting /></el-icon></div>
          <div class="function-name">系统配置</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.admin-home-container {
  min-height: 100%;
  background-color: #f5f7fa;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 5px;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.page-header p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #e6a23c;
}

.content-row {
  margin-bottom: 20px;
}

.content-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.content-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.announcement-list {
  max-height: 300px;
  overflow-y: auto;
}

.announcement-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f2f5;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.announcement-time {
  font-size: 12px;
  color: #909399;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.quick-action-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e9ecef;
}

.quick-action-item:hover {
  background-color: #fff3cd;
  border-color: #ffeeba;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(230, 162, 60, 0.15);
}

.quick-action-icon {
  font-size: 24px;
  color: #e6a23c;
  margin-right: 15px;
}

.quick-action-text {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.function-card {
  border-radius: 12px;
}

.function-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e9ecef;
}

.function-item:hover {
  background-color: #fff3cd;
  border-color: #ffeeba;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(230, 162, 60, 0.15);
}

.function-icon {
  font-size: 24px;
  color: #e6a23c;
  margin-bottom: 10px;
}

.function-name {
  font-size: 14px;
  color: #303133;
}
</style>