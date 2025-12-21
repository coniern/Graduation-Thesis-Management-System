<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 待评审文档列表
const pendingDocuments = ref([
  { id: 1, studentName: '张三', studentId: '20210001', topic: '金融科技对传统银行业务的影响研究', documentType: '中期报告', uploadTime: '2024-04-20', status: '待评审' },
  { id: 2, studentName: '李四', studentId: '20210002', topic: '区块链技术在金融领域的应用前景', documentType: '开题报告', uploadTime: '2024-03-15', status: '待评审' }
])

// 已评审文档列表
const reviewedDocuments = ref([
  { id: 3, studentName: '王五', studentId: '20210003', topic: '普惠金融发展现状及对策研究', documentType: '论文终稿', uploadTime: '2024-05-10', status: '已评审', score: '90' },
  { id: 4, studentName: '赵六', studentId: '20210004', topic: '互联网金融风险监管研究', documentType: '中期报告', uploadTime: '2024-04-18', status: '已评审', score: '85' }
])

// 评审文档
const reviewDocument = (document) => {
  // 模拟评审文档
  console.log('评审文档:', document)
}

// 查看文档
const viewDocument = (document) => {
  // 模拟查看文档
  console.log('查看文档:', document)
}
</script>

<template>
  <div class="document-review-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>文档评审</h1>
      <p>评审学生提交的文档</p>
    </div>
    
    <!-- 待评审文档 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">
            待评审文档
            <el-badge :value="pendingDocuments.length" type="danger" />
          </h3>
        </div>
      </template>
      
      <el-table :data="pendingDocuments" style="width: 100%" border>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center" />
        <el-table-column prop="studentId" label="学号" width="120" align="center" />
        <el-table-column prop="documentType" label="文档类型" width="120" align="center" />
        <el-table-column prop="topic" label="选题" min-width="200" />
        <el-table-column prop="uploadTime" label="上传时间" width="120" align="center" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDocument(scope.row)" style="margin-right: 5px;">
              <el-icon><Document /></el-icon>
              查看
            </el-button>
            <el-button type="success" size="small" @click="reviewDocument(scope.row)">
              <el-icon><EditPen /></el-icon>
              评审
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 已评审文档 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">已评审文档</h3>
        </div>
      </template>
      
      <el-table :data="reviewedDocuments" style="width: 100%" border>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center" />
        <el-table-column prop="studentId" label="学号" width="120" align="center" />
        <el-table-column prop="documentType" label="文档类型" width="120" align="center" />
        <el-table-column prop="topic" label="选题" min-width="200" />
        <el-table-column prop="uploadTime" label="上传时间" width="120" align="center" />
        <el-table-column prop="score" label="评分" width="80" align="center" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDocument(scope.row)">
              <el-icon><Document /></el-icon>
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.document-review-container {
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
  display: flex;
  align-items: center;
}
</style>