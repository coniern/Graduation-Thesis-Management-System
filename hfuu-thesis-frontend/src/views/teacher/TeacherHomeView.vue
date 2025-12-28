<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 教师统计数据
const teacherStats = ref({
  studentCount: 0,
  topicCount: 0,
  pendingReviews: 0,
  completedReviews: 0
})

// 待审核文档
const pendingDocuments = ref([
  { id: 1, studentName: '张三', title: '基于大数据的金融风险预测模型', submitTime: '2025-12-20 14:30' },
  { id: 2, studentName: '李四', title: '区块链技术在数字货币中的应用', submitTime: '2025-12-19 09:15' },
  { id: 3, studentName: '王五', title: '人工智能在金融行业的应用研究', submitTime: '2025-12-18 16:45' }
])

// 我的学生列表
const myStudents = ref([
  { id: 1, studentName: '张三', studentId: '20210001', topicTitle: '基于大数据的金融风险预测模型', progress: 65 },
  { id: 2, studentName: '李四', studentId: '20210002', topicTitle: '区块链技术在数字货币中的应用', progress: 80 },
  { id: 3, studentName: '王五', studentId: '20210003', topicTitle: '人工智能在金融行业的应用研究', progress: 50 }
])

// 加载教师数据
const loadTeacherData = async () => {
  // 模拟加载数据
  await new Promise(resolve => setTimeout(resolve, 500))
  teacherStats.value = {
    studentCount: 15,
    topicCount: 8,
    pendingReviews: 3,
    completedReviews: 12
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadTeacherData()
})
</script>

<template>
  <div class="teacher-home-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>教师主页</h1>
      <p>欢迎回来，{{ JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').realName || '老师' }}</p>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">指导学生数</div>
            <div class="stat-value">{{ teacherStats.studentCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">发布题目数</div>
            <div class="stat-value">{{ teacherStats.topicCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">待审核文档</div>
            <div class="stat-value">{{ teacherStats.pendingReviews }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title">已审核文档</div>
            <div class="stat-value">{{ teacherStats.completedReviews }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 待审核文档 -->
      <el-col :xs="24" :md="12">
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">待审核文档</h3>
              <el-button type="primary" size="small" @click="router.push('/document/review')">文档审核</el-button>
            </div>
          </template>
          <el-table :data="pendingDocuments" stripe style="width: 100%">
            <el-table-column prop="studentName" label="学生姓名" width="120" />
            <el-table-column prop="title" label="文档标题" width="400" />
            <el-table-column prop="submitTime" label="提交时间" width="180" />
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="primary" size="small" @click="router.push('/document/review')">审核</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <!-- 我的学生 -->
      <el-col :xs="24" :md="12">
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">我的学生</h3>
              <el-button type="primary" size="small" @click="router.push('/student/manage')">学生管理</el-button>
            </div>
          </template>
          <el-table :data="myStudents" stripe style="width: 100%">
            <el-table-column prop="studentName" label="学生姓名" width="120" />
            <el-table-column prop="studentId" label="学号" width="120" />
            <el-table-column prop="topicTitle" label="选题" width="300" />
            <el-table-column prop="progress" label="进度">
              <template #default="scope">
                <el-progress :percentage="scope.row.progress" :height="8" :show-text="false" style="margin-top: 8px;" />
              </template>
            </el-table-column>
          </el-table>
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
        <div class="function-item" @click="router.push('/topic/manage')">
          <div class="function-icon"><el-icon><edit /></el-icon></div>
          <div class="function-name">题目管理</div>
        </div>
        <div class="function-item" @click="router.push('/document/review')">
          <div class="function-icon"><el-icon><document /></el-icon></div>
          <div class="function-name">文档审核</div>
        </div>
        <div class="function-item" @click="router.push('/student/manage')">
          <div class="function-icon"><el-icon><user /></el-icon></div>
          <div class="function-name">学生管理</div>
        </div>
        <div class="function-item" @click="router.push('/evaluation/give')">
          <div class="function-icon"><el-icon><star /></el-icon></div>
          <div class="function-name">成绩评定</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.teacher-home-container {
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
  color: #67c23a;
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
  background-color: #f0f9eb;
  border-color: #67c23a;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(103, 194, 58, 0.15);
}

.function-icon {
  font-size: 24px;
  color: #67c23a;
  margin-bottom: 10px;
}

.function-name {
  font-size: 14px;
  color: #303133;
}
</style>