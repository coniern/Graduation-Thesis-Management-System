<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 统计数据
const statistics = ref({
  students: 1200,
  teachers: 80,
  topics: 950,
  archives: 850
})

// 快捷操作
const quickActions = [
  { id: 1, name: '选题申请', icon: 'Document', color: '#667eea', path: '/topic/apply' },
  { id: 2, name: '文档上传', icon: 'Upload', color: '#764ba2', path: '/document/upload' },
  { id: 3, name: '进度查看', icon: 'Timer', color: '#f093fb', path: '/progress' },
  { id: 4, name: '导师评价', icon: 'Star', color: '#4facfe', path: '/evaluation' }
]

// 最新动态
const latestNews = [
  { id: 1, title: '毕业论文选题截止时间延长通知', date: '2024-05-15', author: '教务处' },
  { id: 2, title: '关于2024届毕业论文答辩安排的通知', date: '2024-05-10', author: '教务处' },
  { id: 3, title: '毕业论文查重系统使用指南', date: '2024-05-05', author: '信息中心' },
  { id: 4, title: '毕业论文格式规范更新通知', date: '2024-04-28', author: '教务处' }
]
</script>

<template>
  <div class="home-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>首页</h1>
      <p>欢迎使用哈尔滨金融学院毕业论文管理系统</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :xs="12" :sm="12" :md="6" :lg="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-info">
                <span class="stat-label">学生总数</span>
                <h3 class="stat-value">{{ statistics.students }}</h3>
              </div>
              <div class="stat-icon student-icon"><el-icon><User /></el-icon></div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="12" :md="6" :lg="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-info">
                <span class="stat-label">导师总数</span>
                <h3 class="stat-value">{{ statistics.teachers }}</h3>
              </div>
              <div class="stat-icon teacher-icon"><el-icon><EditPen /></el-icon></div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="12" :md="6" :lg="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-info">
                <span class="stat-label">选题总数</span>
                <h3 class="stat-value">{{ statistics.topics }}</h3>
              </div>
              <div class="stat-icon topic-icon"><el-icon><Document /></el-icon></div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="12" :md="6" :lg="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-info">
                <span class="stat-label">归档总数</span>
                <h3 class="stat-value">{{ statistics.archives }}</h3>
              </div>
              <div class="stat-icon archive-icon"><el-icon><DocumentChecked /></el-icon></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions-section">
      <h2 class="section-title">快捷操作</h2>
      <el-row :gutter="20">
        <el-col v-for="action in quickActions" :key="action.id" :xs="12" :sm="12" :md="6" :lg="6">
          <div class="action-card" :style="{ backgroundColor: action.color }" @click="router.push(action.path)">
            <div class="action-icon"><el-icon :size="32"><component :is="action.icon" /></el-icon></div>
            <div class="action-name">{{ action.name }}</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 内容区域 -->
    <div class="content-section">
      <el-row :gutter="20">
        <!-- 最新动态 -->
        <el-col :xs="24" :md="12" :lg="12">
          <el-card class="content-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">最新动态</h3>
                <el-button type="text" size="small">更多</el-button>
              </div>
            </template>
            <div class="news-list">
              <div v-for="news in latestNews" :key="news.id" class="news-item">
                <div class="news-content">
                  <h4 class="news-title">{{ news.title }}</h4>
                  <p class="news-meta">{{ news.date }} | {{ news.author }}</p>
                </div>
                <el-icon class="news-arrow"><ArrowRight /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 系统公告 -->
        <el-col :xs="24" :md="12" :lg="12">
          <el-card class="content-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">系统公告</h3>
                <el-button type="text" size="small">更多</el-button>
              </div>
            </template>
            <div class="notice-list">
              <el-timeline>
                <el-timeline-item
                  v-for="(item, index) in latestNews"
                  :key="index"
                  :timestamp="item.date"
                >
                  <div class="notice-content">
                    <h4 class="notice-title">{{ item.title }}</h4>
                    <p class="notice-author">{{ item.author }}</p>
                  </div>
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.home-container {
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

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  display: block;
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.stat-value {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: #ffffff;
}

.student-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.teacher-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.topic-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.archive-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.quick-actions-section {
  margin-bottom: 20px;
}

.section-title {
  margin: 0 0 15px;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.action-card {
  height: 120px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #ffffff;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.2);
}

.action-icon {
  margin-bottom: 10px;
}

.action-name {
  font-size: 16px;
  font-weight: 500;
}

.content-section {
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
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.news-list {
  padding: 10px 0;
}

.news-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.news-item:last-child {
  border-bottom: none;
}

.news-item:hover {
  transform: translateX(5px);
}

.news-content {
  flex: 1;
}

.news-title {
  margin: 0 0 5px;
  font-size: 15px;
  font-weight: 500;
  color: #303133;
  line-height: 1.4;
}

.news-meta {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.news-arrow {
  color: #c0c4cc;
  font-size: 16px;
}

.notice-list {
  padding: 10px 0;
}

.notice-content {
  padding: 10px 0;
}

.notice-title {
  margin: 0 0 5px;
  font-size: 15px;
  font-weight: 500;
  color: #303133;
}

.notice-author {
  margin: 0;
  font-size: 12px;
  color: #909399;
}
</style>