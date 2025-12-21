<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 文档列表
const documents = ref([
  { id: 1, name: '开题报告.docx', type: '开题报告', uploadTime: '2024-03-15', status: '待评审', score: '' },
  { id: 2, name: '中期报告.docx', type: '中期报告', uploadTime: '2024-04-20', status: '已评审', score: '85' },
  { id: 3, name: '毕业论文终稿.docx', type: '毕业论文', uploadTime: '2024-05-10', status: '待评审', score: '' }
])

// 上传文件
const uploadFile = () => {
  // 模拟文件上传
  const newDoc = {
    id: documents.value.length + 1,
    name: '新上传文档.docx',
    type: '其他文档',
    uploadTime: new Date().toISOString().split('T')[0],
    status: '待评审',
    score: ''
  }
  documents.value.push(newDoc)
}
</script>

<template>
  <div class="document-upload-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>文档上传</h1>
      <p>上传您的毕业论文相关文档</p>
    </div>
    
    <!-- 上传按钮 -->
    <div class="upload-section">
      <el-card shadow="hover">
        <div class="upload-content">
          <div class="upload-info">
            <h3>上传文档</h3>
            <p>支持 .docx, .pdf, .zip 格式，单个文件不超过 20MB</p>
          </div>
          <el-upload
            class="upload-btn"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="uploadFile"
          >
            <el-button type="primary" size="large">
              <el-icon><Upload /></el-icon>
              选择文件
            </el-button>
          </el-upload>
        </div>
      </el-card>
    </div>
    
    <!-- 文档列表 -->
    <div class="document-section">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <h3 class="card-title">已上传文档</h3>
          </div>
        </template>
        
        <el-table :data="documents" style="width: 100%" border>
          <el-table-column prop="id" label="序号" width="80" align="center" />
          <el-table-column prop="name" label="文档名称" min-width="200">
            <template #default="scope">
              <el-link :underline="false">{{ scope.row.name }}</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="文档类型" width="120" align="center" />
          <el-table-column prop="uploadTime" label="上传时间" width="120" align="center" />
          <el-table-column prop="status" label="状态" width="120" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已评审' ? 'success' : 'warning'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="评分" width="80" align="center">
            <template #default="scope">
              <span v-if="scope.row.score">{{ scope.row.score }}</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" style="margin-right: 5px;">
                <el-icon><Download /></el-icon>
                下载
              </el-button>
              <el-button type="danger" size="small">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.document-upload-container {
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

.upload-section {
  margin-bottom: 20px;
}

.upload-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
}

.upload-info h3 {
  margin: 0 0 10px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.upload-info p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.upload-btn {
  margin-left: 20px;
}

.document-section {
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
</style>