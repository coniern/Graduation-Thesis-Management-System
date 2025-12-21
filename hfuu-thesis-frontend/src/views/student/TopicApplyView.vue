<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 选题列表
const topics = ref([
  { id: 1, title: '金融科技对传统银行业务的影响研究', teacher: '张三', department: '金融学院', status: '可选' },
  { id: 2, title: '区块链技术在金融领域的应用前景', teacher: '李四', department: '金融学院', status: '可选' },
  { id: 3, title: '普惠金融发展现状及对策研究', teacher: '王五', department: '金融学院', status: '可选' },
  { id: 4, title: '互联网金融风险监管研究', teacher: '赵六', department: '金融学院', status: '可选' },
  { id: 5, title: '绿色金融发展路径研究', teacher: '钱七', department: '金融学院', status: '可选' }
])

// 申请状态
const applyStatus = ref('')

// 申请选题
const applyTopic = (topic) => {
  // 模拟申请逻辑
  topic.status = '已申请'
  applyStatus.value = `已成功申请选题：${topic.title}`
}
</script>

<template>
  <div class="topic-apply-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>选题申请</h1>
      <p>选择您感兴趣的毕业论文选题</p>
    </div>
    
    <!-- 提示信息 -->
    <el-alert v-if="applyStatus" :message="applyStatus" type="success" show-icon :closable="true" @close="applyStatus = ''" />
    
    <!-- 选题列表 -->
    <el-card class="topic-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">可选选题列表</h3>
        </div>
      </template>
      
      <el-table :data="topics" style="width: 100%" border>
        <el-table-column prop="id" label="选题编号" width="100" align="center" />
        <el-table-column prop="title" label="选题名称" min-width="300" />
        <el-table-column prop="teacher" label="指导教师" width="120" align="center" />
        <el-table-column prop="department" label="所属部门" width="150" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === '可选' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="applyTopic(scope.row)" 
              :disabled="scope.row.status !== '可选'"
            >
              申请
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 我的选题 -->
    <el-card class="topic-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">我的选题</h3>
        </div>
      </template>
      
      <div v-if="topics.filter(t => t.status === '已申请').length > 0">
        <el-table :data="topics.filter(t => t.status === '已申请')" style="width: 100%" border>
          <el-table-column prop="id" label="选题编号" width="100" align="center" />
          <el-table-column prop="title" label="选题名称" min-width="300" />
          <el-table-column prop="teacher" label="指导教师" width="120" align="center" />
          <el-table-column prop="department" label="所属部门" width="150" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag type="warning">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else class="no-data">
        <el-empty description="您还没有申请任何选题" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.topic-apply-container {
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

.topic-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.topic-card:hover {
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

.no-data {
  padding: 40px 0;
}
</style>