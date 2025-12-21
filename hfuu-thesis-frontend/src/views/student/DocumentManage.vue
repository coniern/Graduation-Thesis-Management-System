<template>
  <div class="document-manage-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="文档管理"
    />

    <!-- 论文信息卡片 -->
    <el-card class="thesis-info-card">
      <el-descriptions :column="2" :border="false">
        <el-descriptions-item label="论文标题">
          <el-input
            v-model="thesisInfo.title"
            placeholder="请输入论文标题"
            clearable
            @change="handleUpdateThesisTitle"
          />
        </el-descriptions-item>
        <el-descriptions-item label="导师">
          {{ thesisInfo.teacherName || '未分配' }}
        </el-descriptions-item>
        <el-descriptions-item label="当前阶段">{{ thesisInfo.currentPhase }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="thesisInfo.status === '已完成' ? 'success' : 'warning'">
            {{ thesisInfo.status }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：文档列表和上传区域 -->
      <div class="left-panel">
        <!-- 文件上传区域 -->
        <el-card class="upload-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>文件上传</span>
              <el-tag type="info" size="small">支持PDF、Word格式</el-tag>
            </div>
          </template>

          <!-- 拖拽上传区域 -->
          <el-upload
            ref="uploadRef"
            class="upload-dragger"
            action="/api/document/upload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-preview="handleFilePreview"
            :on-remove="handleFileRemove"
            :before-upload="beforeUpload"
            :file-list="fileList"
            drag
            multiple
            :limit="3"
            :auto-upload="false"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              点击或拖拽文件到此处上传
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只能上传PDF、Word文件，且不超过50MB
              </div>
            </template>
          </el-upload>

          <!-- 上传按钮 -->
          <div class="upload-actions">
            <el-button type="primary" @click="handleSubmitUpload">上传到服务器</el-button>
            <el-button @click="handleClearFiles">清空队列</el-button>
          </div>
        </el-card>

        <!-- 文档列表 -->
        <el-card class="document-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>我的文档</span>
              <el-tag type="info" size="small">{{ documentList.length }}个文档</el-tag>
            </div>
          </template>

          <el-empty v-if="documentList.length === 0" description="暂无文档" />
          <el-table
            v-else
            :data="documentList"
            border
            stripe
            size="small"
            style="width: 100%"
          >
            <el-table-column prop="name" label="文档名称" min-width="200">
              <template #default="scope">
                <div class="document-name">
                  <el-icon :size="18"><document /></el-icon>
                  <span>{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="100">
              <template #default="scope">
                <el-tag size="small" :type="scope.row.type === 'PDF' ? 'primary' : 'success'">
                  {{ scope.row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="size" label="大小" width="100">
              <template #default="scope">
                {{ formatFileSize(scope.row.size) }}
              </template>
            </el-table-column>
            <el-table-column prop="uploadTime" label="上传时间" width="180" />
            <el-table-column prop="uploader" label="上传者" width="120" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleViewVersions(scope.row)"
                >
                  版本管理
                </el-button>
                <el-button
                  type="info"
                  size="small"
                  @click="handlePreviewDocument(scope.row.id)"
                >
                  预览
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="handleDeleteDocument(scope.row.id)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>

      <!-- 右侧：文档预览和版本历史 -->
      <div class="right-panel">
        <!-- 文档预览区域 -->
        <el-card class="preview-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>文档预览</span>
              <el-dropdown @command="handlePreviewCommand">
                <el-button size="small" type="primary">
                  操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="download">下载文档</el-dropdown-item>
                    <el-dropdown-item command="fullscreen">全屏预览</el-dropdown-item>
                    <el-dropdown-item command="print">打印文档</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>

          <div class="preview-content">
            <div v-if="!currentPreviewDocument" class="no-preview">
              <el-empty description="请选择一个文档进行预览" />
            </div>
            <div v-else class="document-preview">
              <!-- PDF预览区域 -->
              <div v-if="currentPreviewDocument.type === 'PDF'" class="pdf-preview">
                <iframe
                  :src="`https://mozilla.github.io/pdf.js/web/viewer.html?file=${encodeURIComponent(currentPreviewDocument.url)}`"
                  frameborder="0"
                  width="100%"
                  height="500px"
                ></iframe>
              </div>
              <!-- Word文档预览区域 -->
              <div v-else class="word-preview">
                <el-empty description="Word文档预览功能开发中，请下载查看" />
                <el-button type="primary" style="margin-top: 20px">下载文档</el-button>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 版本历史区域 -->
        <el-card class="version-history-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>版本历史</span>
              <el-tag v-if="selectedDocument" type="info" size="small">
                {{ selectedDocument.name }}
              </el-tag>
            </div>
          </template>

          <div v-if="!selectedDocument" class="no-version-history">
            <el-empty description="请选择一个文档查看版本历史" />
          </div>
          <div v-else>
            <el-timeline>
              <el-timeline-item
                v-for="version in versionHistory"
                :key="version.id"
                :timestamp="version.createTime"
                :color="version.isCurrent ? 'primary' : 'info'"
              >
                <el-card :bordered="false" shadow="hover" class="version-item">
                  <div class="version-header">
                    <div class="version-info">
                      <h4>版本 {{ version.version }}</h4>
                      <div class="version-meta">
                        <span>上传者：{{ version.uploader }}</span>
                        <span>大小：{{ formatFileSize(version.size) }}</span>
                      </div>
                    </div>
                    <div class="version-actions">
                      <el-button
                        v-if="!version.isCurrent"
                        type="primary"
                        size="small"
                        @click="handleRollbackVersion(version)"
                      >
                        回滚到该版本
                      </el-button>
                      <el-button
                        type="info"
                        size="small"
                        @click="handleCompareVersion(version)"
                      >
                        对比
                      </el-button>
                      <el-button
                        type="success"
                        size="small"
                        @click="handleDownloadVersion(version)"
                      >
                        下载
                      </el-button>
                    </div>
                  </div>
                  <div class="version-description">
                    <el-descriptions-item label="更新说明">
                      {{ version.description || '无' }}
                    </el-descriptions-item>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled, Document, ArrowDown } from '@element-plus/icons-vue'

// 论文信息
const thesisInfo = ref({
  title: '哈尔滨金融学院毕业论文管理系统设计与实现',
  teacherName: '张三',
  currentPhase: '中期阶段',
  status: '进行中'
})

// 文件上传引用
const uploadRef = ref()

// 文件列表
const fileList = ref([])

// 文档列表
const documentList = ref([
  {
    id: 1,
    name: '开题报告初稿.pdf',
    type: 'PDF',
    size: 2048000,
    url: '',
    uploadTime: '2025-12-20 14:30',
    uploader: '李四',
    currentVersion: 'v1.0'
  },
  {
    id: 2,
    name: '中期报告.docx',
    type: 'Word',
    size: 1024000,
    url: '',
    uploadTime: '2025-12-19 09:00',
    uploader: '李四',
    currentVersion: 'v1.0'
  }
])

// 当前预览的文档
const currentPreviewDocument = ref(null)

// 选中的文档
const selectedDocument = ref(null)

// 版本历史
const versionHistory = ref([
  {
    id: 1,
    version: 'v1.2',
    description: '修改了部分内容',
    uploader: '李四',
    size: 2048000,
    createTime: '2025-12-20 14:30',
    isCurrent: true
  },
  {
    id: 2,
    version: 'v1.1',
    description: '更新了开题报告',
    uploader: '李四',
    size: 1848000,
    createTime: '2025-12-19 16:45',
    isCurrent: false
  },
  {
    id: 3,
    version: 'v1.0',
    description: '初始版本',
    uploader: '李四',
    size: 1548000,
    createTime: '2025-12-18 10:20',
    isCurrent: false
  }
])

// 处理文件上传之前的验证
const beforeUpload = (file: any) => {
  const isPDF = file.type === 'application/pdf'
  const isWord = file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' || file.type === 'application/msword'
  const isLt50M = file.size / 1024 / 1024 < 50

  if (!isPDF && !isWord) {
    ElMessage.error('只能上传PDF和Word文件')
    return false
  }
  if (!isLt50M) {
    ElMessage.error('文件大小不能超过50MB')
    return false
  }
  return true
}

// 处理文件上传成功
const handleUploadSuccess = (response: any, file: any, fileList: any) => {
  ElMessage.success('文件上传成功')
  console.log('上传成功', response, file, fileList)
}

// 处理文件上传失败
const handleUploadError = (error: any, file: any, fileList: any) => {
  ElMessage.error('文件上传失败')
  console.error('上传失败', error, file, fileList)
}

// 处理文件预览
const handleFilePreview = (file: any) => {
  console.log('文件预览', file)
}

// 处理文件删除
const handleFileRemove = (file: any, fileList: any) => {
  console.log('文件删除', file, fileList)
}

// 提交上传
const handleSubmitUpload = () => {
  if (!uploadRef.value) return
  uploadRef.value.submit()
}

// 清空文件列表
const handleClearFiles = () => {
  if (!uploadRef.value) return
  uploadRef.value.clearFiles()
}

// 更新论文标题
const handleUpdateThesisTitle = () => {
  ElMessage.success('论文标题已更新')
}

// 查看版本历史
const handleViewVersions = (document: any) => {
  selectedDocument.value = document
  currentPreviewDocument.value = document
  console.log('查看版本历史', document)
}

// 预览文档
const handlePreviewDocument = (documentId: number) => {
  const document = documentList.value.find(item => item.id === documentId)
  if (document) {
    currentPreviewDocument.value = document
  }
}

// 删除文档
const handleDeleteDocument = (documentId: number) => {
  ElMessageBox.confirm('确定要删除该文档吗？删除后不可恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    documentList.value = documentList.value.filter(item => item.id !== documentId)
    if (currentPreviewDocument.value?.id === documentId) {
      currentPreviewDocument.value = null
    }
    if (selectedDocument.value?.id === documentId) {
      selectedDocument.value = null
    }
    ElMessage.success('文档删除成功')
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 预览命令处理
const handlePreviewCommand = (command: string) => {
  console.log('预览命令', command)
  ElMessage.info(`${command}功能开发中`)
}

// 回滚版本
const handleRollbackVersion = (version: any) => {
  ElMessageBox.confirm(`确定要回滚到版本 ${version.version} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 更新当前版本
    versionHistory.value.forEach(item => {
      item.isCurrent = item.id === version.id
    })
    ElMessage.success('版本回滚成功')
  }).catch(() => {
    ElMessage.info('已取消回滚')
  })
}

// 对比版本
const handleCompareVersion = (version: any) => {
  console.log('对比版本', version)
  ElMessage.info('版本对比功能开发中')
}

// 下载版本
const handleDownloadVersion = (version: any) => {
  console.log('下载版本', version)
  ElMessage.success('开始下载')
}

// 格式化文件大小
const formatFileSize = (size: number) => {
  if (size < 1024) {
    return size + ' B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB'
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(2) + ' MB'
  } else {
    return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
  }
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
}

// 组件挂载时的初始化
onMounted(() => {
  console.log('文档管理页面已加载')
})
</script>

<style scoped>
.document-manage-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.thesis-info-card {
  margin-bottom: 20px;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 1200px) {
  .main-content {
    grid-template-columns: 1fr;
  }
}

.left-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.upload-card {
  flex-shrink: 0;
}

.upload-dragger {
  margin-bottom: 16px;
}

.upload-actions {
  display: flex;
  gap: 8px;
}

.document-list-card {
  flex: 1;
  min-height: 300px;
}

.document-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.right-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.preview-card {
  flex: 1;
  min-height: 400px;
}

.preview-content {
  height: 500px;
  overflow: auto;
  background-color: #fafafa;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
}

.no-preview {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.document-preview {
  height: 100%;
  padding: 20px;
}

.pdf-preview {
  height: 100%;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  overflow: hidden;
}

.word-preview {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.version-history-card {
  flex-shrink: 0;
}

.no-version-history {
  padding: 40px 0;
  text-align: center;
}

.version-item {
  margin: 0;
}

.version-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.version-info h4 {
  margin: 0;
  font-size: 14px;
  color: #1e3a8a;
}

.version-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #6b7280;
  margin-top: 4px;
}

.version-actions {
  display: flex;
  gap: 8px;
}

.version-description {
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
}
</style>