<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from '../../utils/axios'

const router = useRouter()

// 待评审文档列表
const pendingDocuments = ref([])

// 已评审文档列表
const reviewedDocuments = ref([])

// 文档类型映射
const documentTypeMap = {
  1: '开题报告',
  2: '中期报告',
  3: '论文初稿',
  4: '论文终稿',
  5: '查重报告',
  6: '答辩材料',
  7: '支撑材料'
}

// 状态映射
const statusMap = {
  1: { text: '待审核', type: 'warning' },
  2: { text: '审核通过', type: 'success' },
  3: { text: '审核不通过', type: 'danger' },
  4: { text: '已修改', type: 'primary' }
}

// 加载待评审文档
const loadPendingDocuments = async () => {
  try {
    // TODO: 实现从后端获取待评审文档列表的逻辑
    console.log('加载待评审文档')
  } catch (error) {
    ElMessage.error('获取待评审文档列表失败')
    console.error('获取待评审文档列表失败:', error)
  }
}

// 加载已评审文档
const loadReviewedDocuments = async () => {
  try {
    // TODO: 实现从后端获取已评审文档列表的逻辑
    console.log('加载已评审文档')
  } catch (error) {
    ElMessage.error('获取已评审文档列表失败')
    console.error('获取已评审文档列表失败:', error)
  }
}

// 评审文档
const reviewDocument = (document) => {
  router.push({
    path: '/document/review',
    query: { documentId: document.documentId }
  })
}

// 查看文档
const viewDocument = (document) => {
  // TODO: 实现查看文档的逻辑
  console.log('查看文档:', document)
}

// 页面挂载时加载数据
onMounted(() => {
  loadPendingDocuments()
  loadReviewedDocuments()
})
</script>

<template>
  <div class="document-review-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>文档评审</h1>
      <p>评审学生提交的文档</p>
    </div>
    
    <!-- 待评审文档 -->
    <el-card shadow="hover" class="document-card" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">
            待评审文档
            <el-badge :value="pendingDocuments.length" type="danger" />
          </h3>
        </div>
      </template>
      
      <el-table :data="pendingDocuments" style="width: 100%" border stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center">
          <template #default="scope">
            <div class="student-name">{{ scope.row.studentName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="studentId" label="学号" width="120" align="center">
          <template #default="scope">
            <div class="student-id">{{ scope.row.studentId }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="documentType" label="文档类型" width="120" align="center">
          <template #default="scope">
            <el-tag size="small" type="info">{{ documentTypeMap[scope.row.documentType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="topic" label="选题" min-width="200">
          <template #default="scope">
            <div class="document-topic">{{ scope.row.topic }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="120" align="center">
          <template #default="scope">
            <div class="upload-time">{{ scope.row.submitTime }}</div>
          </template>
        </el-table-column>
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
    <el-card shadow="hover" class="document-card">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">已评审文档</h3>
        </div>
      </template>
      
      <el-table :data="reviewedDocuments" style="width: 100%" border stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center">
          <template #default="scope">
            <div class="student-name">{{ scope.row.studentName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="studentId" label="学号" width="120" align="center">
          <template #default="scope">
            <div class="student-id">{{ scope.row.studentId }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="documentType" label="文档类型" width="120" align="center">
          <template #default="scope">
            <el-tag size="small" type="info">{{ documentTypeMap[scope.row.documentType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="topic" label="选题" min-width="200">
          <template #default="scope">
            <div class="document-topic">{{ scope.row.topic }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="120" align="center">
          <template #default="scope">
            <div class="upload-time">{{ scope.row.submitTime }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="评分" width="80" align="center">
          <template #default="scope">
            <div class="document-score">{{ scope.row.score }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="scope">
            <el-tag size="small" :type="statusMap[scope.row.status]?.type">{{ statusMap[scope.row.status]?.text }}</el-tag>
          </template>
        </el-table-column>
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
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
}

.document-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.document-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.student-name,
.student-id,
.upload-time,
.document-score {
  font-size: 14px;
}

.student-name {
  font-weight: 500;
}

.document-topic {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}
</style>