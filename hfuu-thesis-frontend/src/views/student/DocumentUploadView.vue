<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../../utils/axios'

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

// 文档状态映射
const documentStatusMap = {
  1: { text: '待审核', type: 'warning' },
  2: { text: '审核通过', type: 'success' },
  3: { text: '审核不通过', type: 'danger' },
  4: { text: '已修改', type: 'info' }
}

// 文档列表
const documents = ref([])

// 选中的文档类型
const selectedDocumentType = ref(1)

// 版本历史对话框
const versionDialogVisible = ref(false)
const currentDocumentId = ref(null)
const versionHistory = ref([])

// 上传组件
const uploadRef = ref(null)

// 获取学生ID（实际应该从登录信息中获取）
const studentId = ref(1)

// 加载文档列表
const loadDocuments = async () => {
  try {
    // 实际应该调用后端API获取文档列表
    // const response = await axios.get(`/api/file/student/${studentId.value}`)
    // documents.value = response.data
    
    // 模拟数据
    documents.value = [
      {
        documentId: 1,
        studentId: 1,
        documentType: 1,
        documentName: '开题报告.docx',
        uploadTime: '2024-03-15 14:30:00',
        status: 2,
        similarityRate: null,
        version: 1,
        fileFormat: 'docx'
      },
      {
        documentId: 2,
        studentId: 1,
        documentType: 2,
        documentName: '中期报告.docx',
        uploadTime: '2024-04-20 09:15:00',
        status: 1,
        similarityRate: 15.5,
        version: 2,
        fileFormat: 'docx'
      },
      {
        documentId: 3,
        studentId: 1,
        documentType: 3,
        documentName: '论文初稿.docx',
        uploadTime: '2024-05-10 16:45:00',
        status: 3,
        similarityRate: 25.8,
        version: 1,
        fileFormat: 'docx'
      }
    ]
  } catch (error) {
    ElMessage.error('获取文档列表失败')
    console.error('获取文档列表失败:', error)
  }
}

// 处理文件上传
const handleFileUpload = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file.raw)
    formData.append('documentType', selectedDocumentType.value)
    formData.append('studentId', studentId.value)
    
    // 调用后端上传接口
    // const response = await axios.post('/api/file/upload', formData, {
    //   headers: {
    //     'Content-Type': 'multipart/form-data'
    //   }
    // })
    
    // 模拟上传成功
    ElMessage.success('文件上传成功')
    loadDocuments()
    
    // 清空上传队列
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  } catch (error) {
    ElMessage.error('文件上传失败')
    console.error('文件上传失败:', error)
  }
}

// 查看版本历史
const viewVersionHistory = async (documentId) => {
  try {
    currentDocumentId.value = documentId
    // 调用后端API获取版本历史
    // const response = await axios.get(`/api/file/versions/${documentId}`)
    // versionHistory.value = response.data
    
    // 模拟版本历史数据
    versionHistory.value = [
      {
        version: 2,
        uploadTime: '2024-04-20 09:15:00',
        uploadUserId: 1,
        documentName: '中期报告_修改版.docx',
        fileFormat: 'docx',
        fileSize: 1024000
      },
      {
        version: 1,
        uploadTime: '2024-04-18 14:30:00',
        uploadUserId: 1,
        documentName: '中期报告_初稿.docx',
        fileFormat: 'docx',
        fileSize: 950000
      }
    ]
    
    versionDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取版本历史失败')
    console.error('获取版本历史失败:', error)
  }
}

// 下载文档
const downloadDocument = async (documentId, fileName) => {
  try {
    // 调用后端下载接口
    // const response = await axios.get(`/api/file/download/${documentId}`, {
    //   responseType: 'blob'
    // })
    
    // 模拟下载成功
    ElMessage.success('文件下载成功')
  } catch (error) {
    ElMessage.error('文件下载失败')
    console.error('文件下载失败:', error)
  }
}

// 删除文档
const deleteDocument = async (documentId) => {
  try {
    await ElMessageBox.confirm('确定要删除该文档吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 调用后端删除接口
    // await axios.delete(`/api/file/${documentId}`)
    
    // 模拟删除成功
    ElMessage.success('文档删除成功')
    loadDocuments()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('文档删除失败')
      console.error('文档删除失败:', error)
    }
  }
}

// 查重功能
const checkSimilarity = async (documentId) => {
  try {
    // 调用后端查重接口
    // const response = await axios.post(`/api/file/check-similarity/${documentId}`)
    // const similarityRate = response.data.similarityRate
    
    // 模拟查重结果
    const similarityRate = Math.random() * 30 + 5 // 5%-35%
    
    ElMessage.success(`查重完成，查重率：${similarityRate.toFixed(1)}%`)
    
    // 更新文档列表
    loadDocuments()
  } catch (error) {
    ElMessage.error('查重失败')
    console.error('查重失败:', error)
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadDocuments()
})
</script>

<template>
  <div class="document-upload-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>过程文档提交</h1>
      <p>上传您的毕业论文相关文档，系统将记录版本历史</p>
    </div>
    
    <!-- 上传区域 -->
    <div class="upload-section">
      <el-card shadow="hover">
        <div class="upload-content">
          <div class="upload-info">
            <h3>上传文档</h3>
            <p>支持 .docx, .pdf, .zip 格式，单个文件不超过 20MB</p>
          </div>
          
          <div class="upload-form">
            <el-row :gutter="20" align="middle">
              <el-col :xs="24" :md="12" :lg="8">
                <el-form-item label="文档类型" required>
                  <el-select v-model="selectedDocumentType" placeholder="请选择文档类型" style="width: 100%">
                    <el-option label="开题报告" value="1" />
                    <el-option label="中期报告" value="2" />
                    <el-option label="论文初稿" value="3" />
                    <el-option label="论文终稿" value="4" />
                    <el-option label="查重报告" value="5" />
                    <el-option label="答辩材料" value="6" />
                    <el-option label="支撑材料" value="7" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :md="12" :lg="16">
                <el-upload
                  ref="uploadRef"
                  class="upload-btn"
                  action="#"
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleFileUpload"
                  accept=".docx,.pdf,.zip"
                  :file-list="[]"
                >
                  <el-button type="primary" size="large">
                    <el-icon><Upload /></el-icon>
                    选择文件
                  </el-button>
                </el-upload>
              </el-col>
            </el-row>
          </div>
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
        
        <el-table :data="documents" style="width: 100%" border stripe>
          <el-table-column prop="documentId" label="文档ID" width="100" align="center" />
          <el-table-column prop="documentName" label="文档名称" min-width="250">
            <template #default="scope">
              <div class="document-name-section">
                <el-link :underline="false">{{ scope.row.documentName }}</el-link>
                <el-tag size="small" class="file-format-tag">{{ scope.row.fileFormat }}</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="文档类型" width="120" align="center">
            <template #default="scope">
              {{ documentTypeMap[scope.row.documentType] }}
            </template>
          </el-table-column>
          <el-table-column label="状态" width="120" align="center">
            <template #default="scope">
              <el-tag :type="documentStatusMap[scope.row.status].type">
                {{ documentStatusMap[scope.row.status].text }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="similarityRate" label="查重率" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.similarityRate !== null" :class="scope.row.similarityRate > 20 ? 'high-similarity' : ''">
                {{ scope.row.similarityRate }}%
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="version" label="版本" width="80" align="center">
            <template #default="scope">
              <el-tag size="small" type="info">V{{ scope.row.version }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="uploadTime" label="上传时间" width="180" align="center" />
          <el-table-column label="操作" width="300" align="center">
            <template #default="scope">
              <el-button 
                type="primary" 
                size="small" 
                style="margin-right: 5px;"
                @click="downloadDocument(scope.row.documentId, scope.row.documentName)"
              >
                <el-icon><Download /></el-icon>
                下载
              </el-button>
              <el-button 
                type="info" 
                size="small" 
                style="margin-right: 5px;"
                @click="viewVersionHistory(scope.row.documentId)"
              >
                <el-icon><Time /></el-icon>
                版本历史
              </el-button>
              <el-button 
                v-if="scope.row.documentType === 3 || scope.row.documentType === 4" 
                type="warning" 
                size="small" 
                style="margin-right: 5px;"
                @click="checkSimilarity(scope.row.documentId)"
              >
                <el-icon><Search /></el-icon>
                查重
              </el-button>
              <el-button 
                type="danger" 
                size="small"
                @click="deleteDocument(scope.row.documentId)"
              >
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 版本历史对话框 -->
    <el-dialog
      v-model="versionDialogVisible"
      title="版本历史"
      width="800px"
      destroy-on-close
    >
      <div class="version-dialog-content">
        <el-table :data="versionHistory" style="width: 100%" border>
          <el-table-column prop="version" label="版本" width="100" align="center" />
          <el-table-column prop="documentName" label="文档名称" min-width="300" />
          <el-table-column prop="uploadTime" label="上传时间" width="180" align="center" />
          <el-table-column prop="fileFormat" label="格式" width="80" align="center" />
          <el-table-column prop="fileSize" label="大小" width="100" align="center">
            <template #default="scope">
              {{ (scope.row.fileSize / 1024 / 1024).toFixed(2) }} MB
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template #default="scope">
              <el-button type="primary" size="small">
                <el-icon><Download /></el-icon>
                下载
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
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
  padding: 20px;
}

.upload-info h3 {
  margin: 0 0 10px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.upload-info p {
  margin: 0 0 20px;
  font-size: 14px;
  color: #909399;
}

.upload-form {
  margin-top: 20px;
}

.upload-btn {
  display: inline-block;
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

.document-name-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-format-tag {
  font-size: 10px;
  padding: 2px 6px;
}

.high-similarity {
  color: #f56c6c;
  font-weight: 600;
}

.version-dialog-content {
  padding: 10px 0;
}
</style>