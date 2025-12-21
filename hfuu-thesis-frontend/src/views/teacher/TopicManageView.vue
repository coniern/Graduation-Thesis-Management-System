<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 选题列表
const topics = ref([
  { id: 1, title: '金融科技对传统银行业务的影响研究', teacher: '张三', department: '金融学院', status: '可选', applyCount: 5 },
  { id: 2, title: '区块链技术在金融领域的应用前景', teacher: '张三', department: '金融学院', status: '可选', applyCount: 3 },
  { id: 3, title: '普惠金融发展现状及对策研究', teacher: '张三', department: '金融学院', status: '可选', applyCount: 2 },
  { id: 4, title: '互联网金融风险监管研究', teacher: '张三', department: '金融学院', status: '可选', applyCount: 1 }
])

// 查看申请列表
const viewApplications = (topic) => {
  // 模拟查看申请列表
  console.log('查看选题申请列表:', topic)
}

// 新增选题
const addTopic = () => {
  // 模拟新增选题
  const newTopic = {
    id: topics.value.length + 1,
    title: '新选题标题',
    teacher: '张三',
    department: '金融学院',
    status: '可选',
    applyCount: 0
  }
  topics.value.push(newTopic)
}

// 删除选题
const deleteTopic = (id) => {
  // 模拟删除选题
  topics.value = topics.value.filter(topic => topic.id !== id)
}
</script>

<template>
  <div class="topic-manage-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>选题管理</h1>
      <p>管理您的毕业论文选题</p>
    </div>
    
    <!-- 操作按钮 -->
    <div class="action-section">
      <el-button type="primary" @click="addTopic">
        <el-icon><Plus /></el-icon>
        新增选题
      </el-button>
    </div>
    
    <!-- 选题列表 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">我的选题列表</h3>
        </div>
      </template>
      
      <el-table :data="topics" style="width: 100%" border>
        <el-table-column prop="id" label="选题编号" width="100" align="center" />
        <el-table-column prop="title" label="选题名称" min-width="300" />
        <el-table-column prop="department" label="所属部门" width="150" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyCount" label="申请人数" width="120" align="center">
          <template #default="scope">
            <el-badge :value="scope.row.applyCount" type="danger" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewApplications(scope.row)" style="margin-right: 5px;">
              <el-icon><View /></el-icon>
              查看申请
            </el-button>
            <el-button type="danger" size="small" @click="deleteTopic(scope.row.id)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.topic-manage-container {
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

.action-section {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
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