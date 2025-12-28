<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 学生统计数据
const studentStats = ref({
  topicCount: 0,
  documentCount: 0,
  unreadMessages: 0,
  submissionProgress: 0
})

// 待办事项
const todoList = ref([
  { id: 1, title: '提交论文选题', status: 0, deadline: '2025-01-15' },
  { id: 2, title: '上传开题报告', status: 0, deadline: '2025-02-20' },
  { id: 3, title: '提交中期检查', status: 0, deadline: '2025-03-30' },
  { id: 4, title: '上传最终论文', status: 0, deadline: '2025-05-10' }
])

// 最近动态
const recentActivities = ref([
  { id: 1, title: '导师已审核您的选题', time: '2025-12-20 14:30' },
  { id: 2, title: '系统发布了新的论文格式要求', time: '2025-12-18 09:15' },
  { id: 3, title: '您上传了最新的开题报告', time: '2025-12-15 16:45' }
])

// 加载学生数据
const loadStudentData = async () => {
  // 模拟加载数据
  await new Promise(resolve => setTimeout(resolve, 500))
  studentStats.value = {
    topicCount: 5,
    documentCount: 3,
    unreadMessages: 2,
    submissionProgress: 65
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadStudentData()
})
</script>

<template>
  <div class="student-home-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>学生主页</h1>
      <p>欢迎回来，{{ JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').realName || '同学' }}</p>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">可选题目数</div>
            <div class="stat-value">{{ studentStats.topicCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">已上传文档</div>
            <div class="stat-value">{{ studentStats.documentCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">未读消息</div>
            <div class="stat-value">{{ studentStats.unreadMessages }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">完成进度</div>
            <div class="stat-value">{{ studentStats.submissionProgress }}%</div>
            <el-progress :percentage="studentStats.submissionProgress" :height="8" :show-text="false" class="progress-bar" />
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 待办事项 -->
      <el-col :xs="24" :md="12">
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">待办事项</h3>
              <el-button type="primary" size="small" @click="router.push('/topic/apply')">选题申请</el-button>
            </div>
          </template>
          <el-table :data="todoList" stripe style="width: 100%">
            <el-table-column prop="title" label="任务" width="400" />
            <el-table-column prop="deadline" label="截止日期" width="150" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 0 ? 'warning' : 'success'">
                  {{ scope.row.status === 0 ? '未完成' : '已完成' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <!-- 最近动态 -->
      <el-col :xs="24" :md="12">
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">最近动态</h3>
              <el-button type="primary" size="small" @click="router.push('/document/upload')">上传文档</el-button>
            </div>
          </template>
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-title">{{ activity.title }}</div>
              <div class="activity-time">{{ activity.time }}</div>
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
        <div class="function-item" @click="router.push('/topic/apply')">
          <div class="function-icon"><el-icon><document /></el-icon></div>
          <div class="function-name">选题申请</div>
        </div>
        <div class="function-item" @click="router.push('/document/upload')">
          <div class="function-icon"><el-icon><upload /></el-icon></div>
          <div class="function-name">文档上传</div>
        </div>
        <div class="function-item" @click="router.push('/progress')">
          <div class="function-icon"><el-icon><timer /></el-icon></div>
          <div class="function-name">进度查询</div>
        </div>
        <div class="function-item" @click="router.push('/evaluation')">
          <div class="function-icon"><el-icon><star /></el-icon></div>
          <div class="function-name">成绩查询</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.student-home-container {
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
  color: #409eff;
}

.progress-bar {
  margin-top: 10px;
  width: 100%;
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

.activity-list {
  max-height: 300px;
  overflow-y: auto;
}

.activity-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f2f5;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.activity-time {
  font-size: 12px;
  color: #909399;
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
  background-color: #e3f2fd;
  border-color: #409eff;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(64, 158, 255, 0.15);
}

.function-icon {
  font-size: 24px;
  color: #409eff;
  margin-bottom: 10px;
}

.function-name {
  font-size: 14px;
  color: #303133;
}
</style>