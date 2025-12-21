<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 学生列表
const students = ref([
  { id: 1, studentId: '20210001', realName: '张三', major: '金融学', grade: '2021级', topic: '金融科技对传统银行业务的影响研究', status: '进行中' },
  { id: 2, studentId: '20210002', realName: '李四', major: '金融学', grade: '2021级', topic: '区块链技术在金融领域的应用前景', status: '进行中' },
  { id: 3, studentId: '20210003', realName: '王五', major: '金融学', grade: '2021级', topic: '普惠金融发展现状及对策研究', status: '已完成' },
  { id: 4, studentId: '20210004', realName: '赵六', major: '金融学', grade: '2021级', topic: '互联网金融风险监管研究', status: '未开始' }
])

// 查看学生详情
const viewStudent = (student) => {
  // 模拟查看学生详情
  console.log('查看学生详情:', student)
}

// 状态映射
const statusMap = {
  '已完成': 'success',
  '进行中': 'warning',
  '未开始': 'info'
}
</script>

<template>
  <div class="student-manage-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>学生管理</h1>
      <p>管理您指导的学生</p>
    </div>
    
    <!-- 学生列表 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">我的学生列表</h3>
        </div>
      </template>
      
      <el-table :data="students" style="width: 100%" border>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="studentId" label="学号" width="120" align="center" />
        <el-table-column prop="realName" label="姓名" width="100" align="center" />
        <el-table-column prop="major" label="专业" width="120" align="center" />
        <el-table-column prop="grade" label="年级" width="120" align="center" />
        <el-table-column prop="topic" label="选题" min-width="300" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status]">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewStudent(scope.row)">
              <el-icon><View /></el-icon>
              查看
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
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
</style>