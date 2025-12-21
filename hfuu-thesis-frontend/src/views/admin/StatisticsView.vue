<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 统计数据
const statistics = ref({
  students: 1200,
  teachers: 80,
  topics: 950,
  archives: 850,
  pendingReviews: 23,
  completedTheses: 780
})

// 历年论文数量
const thesisTrend = ref([
  { year: '2020', count: 650 },
  { year: '2021', count: 720 },
  { year: '2022', count: 800 },
  { year: '2023', count: 850 },
  { year: '2024', count: 950 }
])

// 学院分布
const departmentDistribution = ref([
  { department: '金融学院', count: 420 },
  { department: '会计学院', count: 280 },
  { department: '经济学院', count: 180 },
  { department: '信息管理学院', count: 70 }
])

// 教师指导学生数
const teacherStudentCount = ref([
  { name: '张三', students: 15 },
  { name: '李四', students: 12 },
  { name: '王五', students: 18 },
  { name: '赵六', students: 10 },
  { name: '钱七', students: 14 }
])
</script>

<template>
  <div class="statistics-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>统计分析</h1>
      <p>查看系统统计数据</p>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-cards">
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
        <el-col :xs="12" :sm="12" :md="6" :lg="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-info">
                <span class="stat-label">待评审</span>
                <h3 class="stat-value">{{ statistics.pendingReviews }}</h3>
              </div>
              <div class="stat-icon pending-icon"><el-icon><Clock /></el-icon></div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="12" :md="6" :lg="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-info">
                <span class="stat-label">完成论文</span>
                <h3 class="stat-value">{{ statistics.completedTheses }}</h3>
              </div>
              <div class="stat-icon completed-icon"><el-icon><CheckCircle /></el-icon></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 历年论文趋势 -->
        <el-col :xs="24" :md="12">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">历年论文数量趋势</h3>
              </div>
            </template>
            <div class="chart-container">
              <el-empty description="图表占位" />
              <div class="chart-data">
                <el-table :data="thesisTrend" style="width: 100%" border>
                  <el-table-column prop="year" label="年份" width="100" align="center" />
                  <el-table-column prop="count" label="论文数量" width="120" align="center" />
                </el-table>
              </div>
            </div>
          </el-card>
        </el-col>
        <!-- 学院分布 -->
        <el-col :xs="24" :md="12">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">学院论文分布</h3>
              </div>
            </template>
            <div class="chart-container">
              <el-empty description="图表占位" />
              <div class="chart-data">
                <el-table :data="departmentDistribution" style="width: 100%" border>
                  <el-table-column prop="department" label="学院" width="150" align="center" />
                  <el-table-column prop="count" label="论文数量" width="120" align="center" />
                </el-table>
              </div>
            </div>
          </el-card>
        </el-col>
        <!-- 教师指导学生数 -->
        <el-col :xs="24">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">教师指导学生数</h3>
              </div>
            </template>
            <div class="chart-container">
              <el-empty description="图表占位" />
              <div class="chart-data">
                <el-table :data="teacherStudentCount" style="width: 100%" border>
                  <el-table-column prop="name" label="教师姓名" width="120" align="center" />
                  <el-table-column prop="students" label="指导学生数" width="120" align="center" />
                </el-table>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.statistics-container {
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

.stats-cards {
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

.pending-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.completed-icon {
  background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
}

.charts-section {
  margin-bottom: 20px;
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

.chart-container {
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.chart-data {
  width: 100%;
  margin-top: 20px;
}
</style>