<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from '../../utils/axios'

const router = useRouter()

// 学生列表
const students = ref([])

// 加载学生列表
const loadStudents = async () => {
  try {
    const response = await axios.get('/api/teacher/students')
    students.value = response.data
  } catch (error) {
    ElMessage.error('获取学生列表失败')
    console.error('获取学生列表失败:', error)
  }
}

// 查看学生详情
const viewStudent = (student) => {
  router.push({
    path: '/student/detail',
    query: { studentId: student.userId }
  })
}

// 查看学生进度
const viewProgress = (student) => {
  router.push({
    path: '/student/progress',
    query: { studentId: student.userId }
  })
}

// 审核学生文档
const reviewDocument = (student) => {
  router.push({
    path: '/document/review',
    query: { studentId: student.userId }
  })
}

// 提交答辩成绩
const submitScore = (student) => {
  router.push({
    path: '/evaluation/give',
    query: { studentId: student.userId }
  })
}

// 状态映射
const statusMap = {
  '已完成': 'success',
  '进行中': 'warning',
  '未开始': 'info'
}

// 页面挂载时加载数据
onMounted(() => {
  loadStudents()
})
</script>

<template>
  <div class="student-manage-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>学生管理</h1>
      <p>管理您指导的学生</p>
    </div>
    
    <!-- 学生列表 -->
    <el-card shadow="hover" class="student-list-card">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">我的学生列表</h3>
        </div>
      </template>
      
      <el-table :data="students" style="width: 100%" border stripe>
        <el-table-column prop="studentId" label="学号" width="120" align="center">
          <template #default="scope">
            <div class="student-id">{{ scope.row.studentNo }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="realName" label="姓名" width="100" align="center">
          <template #default="scope">
            <div class="student-name">{{ scope.row.name }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="major" label="专业" width="120" align="center">
          <template #default="scope">
            <div class="student-major">{{ scope.row.major }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="年级" width="120" align="center">
          <template #default="scope">
            <div class="student-grade">{{ scope.row.grade }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="topic" label="选题" min-width="300">
          <template #default="scope">
            <div class="student-topic">{{ scope.row.topicName || '未选题' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="进度状态" width="120" align="center">
          <template #default="scope">
            <el-tag size="small" :type="statusMap[scope.row.status || '未开始']">
              {{ scope.row.status || '未开始' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewStudent(scope.row)" style="margin-right: 5px;">
              <el-icon><User /></el-icon>
              学生详情
            </el-button>
            <el-button type="success" size="small" @click="viewProgress(scope.row)" style="margin-right: 5px;">
              <el-icon><Progress /></el-icon>
              进度查看
            </el-button>
            <el-button type="info" size="small" @click="reviewDocument(scope.row)" style="margin-right: 5px;">
              <el-icon><Document /></el-icon>
              文档审核
            </el-button>
            <el-button type="warning" size="small" @click="submitScore(scope.row)">
              <el-icon><Star /></el-icon>
              成绩评定
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.student-manage-container {
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

.student-list-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.student-list-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.student-id,
.student-name,
.student-major,
.student-grade,
.student-topic {
  font-size: 14px;
}

.student-name {
  font-weight: 500;
}

.student-topic {
  color: #606266;
  line-height: 1.5;
}
</style>