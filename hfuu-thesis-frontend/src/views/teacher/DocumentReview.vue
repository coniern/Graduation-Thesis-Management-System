<template>
  <div class="document-review-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="文档审阅"
    />

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：文档信息和预览 -->
      <div class="left-panel">
        <!-- 文档信息 -->
        <el-card class="document-info-card" shadow="hover">
          <div class="document-info">
            <div class="document-title">
              <h3>{{ documentInfo.title }}</h3>
              <el-tag :type="documentInfo.type === 'PDF' ? 'primary' : 'info'">
                {{ documentInfo.type }}
              </el-tag>
            </div>
            <div class="document-meta">
              <el-descriptions :column="3" size="small">
                <el-descriptions-item label="学生姓名">{{ documentInfo.studentName }}</el-descriptions-item>
                <el-descriptions-item label="学号">{{ documentInfo.studentNo }}</el-descriptions-item>
                <el-descriptions-item label="班级">{{ documentInfo.className }}</el-descriptions-item>
                <el-descriptions-item label="提交时间">{{ documentInfo.submitTime }}</el-descriptions-item>
                <el-descriptions-item label="文件大小">{{ formatFileSize(documentInfo.size) }}</el-descriptions-item>
                <el-descriptions-item label="重复率">{{ documentInfo.similarity }}%</el-descriptions-item>
                <el-descriptions-item label="当前阶段" span="3">
                  <el-tag type="warning">{{ documentInfo.phase }}</el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
        </el-card>

        <!-- 文档预览 -->
        <el-card class="document-preview-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>文档预览</span>
              <div class="preview-tools">
                <el-button-group>
                  <el-button size="small" @click="handleZoomOut">
                    <el-icon><minus /></el-icon>
                  </el-button>
                  <el-button size="small">{{ zoomLevel }}%</el-button>
                  <el-button size="small" @click="handleZoomIn">
                    <el-icon><plus /></el-icon>
                  </el-button>
                </el-button-group>
                <el-button size="small" @click="handlePageFit">
                  <el-icon><full-screen /></el-icon>
                </el-button>
                <el-button size="small" @click="handleDownload">
                  <el-icon><download /></el-icon> 下载
                </el-button>
              </div>
            </div>
          </template>

          <div class="document-preview-area">
            <div class="pdf-container">
              <!-- PDF预览组件将在实际开发中集成pdfjs-dist -->
              <div class="mock-pdf-preview">
                <h4>PDF预览区域（集成pdfjs-dist实现）</h4>
                <p>文档标题：{{ documentInfo.title }}</p>
                <p>共 {{ totalPages }} 页，当前第 {{ currentPage }} 页</p>
                <el-progress
                  :percentage="(currentPage / totalPages) * 100"
                  :stroke-width="6"
                  :show-text="false"
                  style="margin: 20px 0;"
                />
                <div class="pdf-content">
                  <!-- 模拟PDF内容 -->
                  <div class="pdf-page">
                    <h5>第 {{ currentPage }} 页</h5>
                    <p>这是文档的模拟内容，实际项目中会使用pdfjs-dist渲染真实PDF文件。</p>
                    <p v-for="i in 10" :key="i">段落 {{ i }}：这里是文档的正文内容，用于展示PDF预览效果。</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 页码控制 -->
            <div class="page-controls">
              <el-input-number
                v-model="currentPage"
                :min="1"
                :max="totalPages"
                size="small"
                @change="handlePageChange"
              />
              <span class="page-info">共 {{ totalPages }} 页</span>
              <el-button-group>
                <el-button size="small" :disabled="currentPage === 1" @click="handlePrevPage">
                  <el-icon><arrow-left /></el-icon> 上一页
                </el-button>
                <el-button size="small" :disabled="currentPage === totalPages" @click="handleNextPage">
                  下一页 <el-icon><arrow-right /></el-icon>
                </el-button>
              </el-button-group>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧：批注和评分 -->
      <div class="right-panel">
        <!-- 批注工具 -->
        <el-card class="annotation-tools-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>批注工具</span>
              <el-tag type="info" size="small">{{ annotations.length }} 条批注</el-tag>
            </div>
          </template>

          <div class="annotation-tools">
            <el-radio-group v-model="activeAnnotationTool" size="small">
              <el-radio-button label="text">
                <el-icon><edit /></el-icon> 文本批注
              </el-radio-button>
              <el-radio-button label="voice">
                <el-icon><microphone /></el-icon> 语音批注
              </el-radio-button>
              <el-radio-button label="highlight">
                <el-icon><brush /></el-icon> 高亮
              </el-radio-button>
              <el-radio-button label="comment">
                <el-icon><message /></el-icon> 评论
              </el-radio-button>
            </el-radio-group>

            <!-- 文本批注输入 -->
            <el-input
              v-if="activeAnnotationTool === 'text'"
              v-model="textAnnotation"
              type="textarea"
              placeholder="输入批注内容..."
              rows="3"
              maxlength="500"
              show-word-limit
              resize="none"
            >
              <template #append>
                <el-button type="primary" size="small" @click="handleAddTextAnnotation">
                  添加批注
                </el-button>
              </template>
            </el-input>

            <!-- 语音批注控件 -->
            <div v-if="activeAnnotationTool === 'voice'" class="voice-annotation">
              <el-button
                :type="isRecording ? 'danger' : 'primary'"
                size="large"
                :icon="isRecording ? 'Stop' : 'Microphone'"
                @click="handleVoiceRecord"
              >
                {{ isRecording ? '停止录音' : '开始录音' }}
              </el-button>
              <div v-if="recordDuration > 0" class="record-duration">
                录音时长：{{ recordDuration }}秒
              </div>
            </div>
          </div>
        </el-card>

        <!-- 批注列表 -->
        <el-card class="annotations-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>批注列表</span>
              <el-button size="small" type="danger" @click="handleClearAllAnnotations">
                <el-icon><Delete /></el-icon> 清空所有批注
              </el-button>
            </div>
          </template>

          <el-empty v-if="annotations.length === 0" description="暂无批注" />
          <el-timeline v-else>
            <el-timeline-item
              v-for="annotation in annotations"
              :key="annotation.id"
              :timestamp="annotation.time"
              placement="top"
            >
              <el-card :bordered="false" shadow="hover" class="annotation-item">
                <div class="annotation-header">
                  <div class="annotation-type">
                    <el-tag :type="annotation.type === 'text' ? 'primary' : annotation.type === 'voice' ? 'success' : annotation.type === 'highlight' ? 'warning' : 'info'">
                      {{ annotation.type === 'text' ? '文本批注' : annotation.type === 'voice' ? '语音批注' : annotation.type === 'highlight' ? '高亮' : '评论' }}
                    </el-tag>
                    <span class="annotation-page">第 {{ annotation.page }} 页</span>
                  </div>
                  <el-button
                    type="text"
                    size="small"
                    @click="handleDeleteAnnotation(annotation.id)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
                <div class="annotation-content">
                  {{ annotation.content }}
                  <!-- 语音批注播放控件 -->
                  <el-audio
                    v-if="annotation.type === 'voice'"
                    :src="annotation.voiceUrl"
                    style="margin-top: 8px;"
                  />
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>

        <!-- 评分和评语 -->
        <el-card class="scoring-panel-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>评分和评语</span>
            </div>
          </template>

          <div class="scoring-panel">
            <!-- 评分 -->
            <div class="score-section">
              <h4>评分</h4>
              <div class="score-input">
                <el-rate
                  v-model="score"
                  :max="100"
                  :show-score="true"
                  text-color="#1e3a8a"
                  score-template="{value}分"
                  size="large"
                />
              </div>
            </div>

            <!-- 评语模板 -->
            <div class="comment-template-section">
              <h4>评语模板</h4>
              <div class="comment-templates">
                <el-button
                  v-for="template in commentTemplates"
                  :key="template.id"
                  type="primary"
                  plain
                  size="small"
                  @click="handleApplyTemplate(template.content)"
                >
                  {{ template.name }}
                </el-button>
              </div>
            </div>

            <!-- 评语输入 -->
            <div class="comment-section">
              <h4>评语</h4>
              <el-input
                v-model="comment"
                type="textarea"
                placeholder="输入评语..."
                rows="6"
                maxlength="1000"
                show-word-limit
                resize="vertical"
              />
            </div>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button type="success" size="large" @click="handleSaveDraft">
                <el-icon><Document /></el-icon> 保存草稿
              </el-button>
              <el-button type="primary" size="large" @click="handleSubmitReview">
                <el-icon><Check /></el-icon> 提交审阅
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Microphone, Brush, Message, Delete, Plus, Minus, FullScreen, Download, Stop, ArrowLeft, ArrowRight, Document, Check } from '@element-plus/icons-vue'

// 文档信息类型定义
interface DocumentInfo {
  id: number
  title: string
  studentName: string
  studentNo: string
  className: string
  submitTime: string
  size: number
  similarity: number
  type: string
  phase: string
}

// 批注类型定义
interface Annotation {
  id: number
  type: 'text' | 'voice' | 'highlight' | 'comment'
  content: string
  page: number
  time: string
  voiceUrl?: string
}

// 评语模板类型定义
interface CommentTemplate {
  id: number
  name: string
  content: string
}

// 文档信息
const documentInfo = ref<DocumentInfo>({
  id: 1,
  title: '开题报告初稿.pdf',
  studentName: '张三',
  studentNo: '202201001',
  className: '金融2201',
  submitTime: '2025-12-20 14:30',
  size: 2048000,
  similarity: 15.2,
  type: 'PDF',
  phase: '开题阶段'
})

// PDF预览相关
const totalPages = ref(10)
const currentPage = ref(1)
const zoomLevel = ref(100)

// 批注相关
const activeAnnotationTool = ref<'text' | 'voice' | 'highlight' | 'comment'>('text')
const textAnnotation = ref('')
const annotations = ref<Annotation[]>([
  {
    id: 1,
    type: 'text',
    content: '这个部分需要进一步详细说明',
    page: 1,
    time: '2025-12-21 09:30'
  },
  {
    id: 2,
    type: 'highlight',
    content: '这里的理论依据不充分',
    page: 2,
    time: '2025-12-21 09:35'
  },
  {
    id: 3,
    type: 'comment',
    content: '建议增加相关文献引用',
    page: 3,
    time: '2025-12-21 09:40'
  }
])

// 语音批注相关
const isRecording = ref(false)
const recordDuration = ref(0)
let recordTimer: number | null = null

// 评分和评语
const score = ref(85)
const comment = ref('')
const commentTemplates = ref<CommentTemplate[]>([
  {
    id: 1,
    name: '优秀',
    content: '该文档内容全面，结构清晰，逻辑严谨，理论依据充分，达到优秀水平。'
  },
  {
    id: 2,
    name: '良好',
    content: '该文档内容完整，结构合理，逻辑清晰，需要进一步完善细节。'
  },
  {
    id: 3,
    name: '中等',
    content: '该文档内容基本完整，结构基本合理，需要进一步调整和完善。'
  },
  {
    id: 4,
    name: '及格',
    content: '该文档内容基本符合要求，结构存在问题，需要大量修改。'
  },
  {
    id: 5,
    name: '不及格',
    content: '该文档内容不符合要求，结构混乱，需要重新撰写。'
  }
])

// 格式化文件大小
const formatFileSize = (size: number) => {
  if (size < 1024) {
    return size + ' B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB'
  } else {
    return (size / (1024 * 1024)).toFixed(2) + ' MB'
  }
}

// 处理缩放
const handleZoomIn = () => {
  if (zoomLevel.value < 200) {
    zoomLevel.value += 10
  }
}

const handleZoomOut = () => {
  if (zoomLevel.value > 50) {
    zoomLevel.value -= 10
  }
}

const handlePageFit = () => {
  zoomLevel.value = 100
  ElMessage.info('已调整为适合页面大小')
}

// 处理页码
const handlePrevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const handleNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const handlePageChange = () => {
  console.log('跳转到第', currentPage.value, '页')
}

// 处理下载
const handleDownload = () => {
  console.log('下载文档', documentInfo.value.id)
  ElMessage.info('文档下载功能开发中')
}

// 处理添加文本批注
const handleAddTextAnnotation = () => {
  if (!textAnnotation.value.trim()) {
    ElMessage.warning('请输入批注内容')
    return
  }

  const newAnnotation: Annotation = {
    id: annotations.value.length + 1,
    type: 'text',
    content: textAnnotation.value.trim(),
    page: currentPage.value,
    time: new Date().toLocaleString()
  }

  annotations.value.push(newAnnotation)
  textAnnotation.value = ''
  ElMessage.success('批注添加成功')
}

// 处理语音录音
const handleVoiceRecord = () => {
  if (isRecording.value) {
    // 停止录音
    isRecording.value = false
    if (recordTimer) {
      clearInterval(recordTimer)
      recordTimer = null
    }
    ElMessage.success('录音已停止')
    // 模拟语音批注添加
    const newAnnotation: Annotation = {
      id: annotations.value.length + 1,
      type: 'voice',
      content: `语音批注（${recordDuration.value}秒）`,
      page: currentPage.value,
      time: new Date().toLocaleString(),
      voiceUrl: 'https://example.com/voice.mp3' // 模拟语音URL
    }
    annotations.value.push(newAnnotation)
    recordDuration.value = 0
  } else {
    // 开始录音
    isRecording.value = true
    recordDuration.value = 0
    recordTimer = window.setInterval(() => {
      recordDuration.value++
    }, 1000)
    ElMessage.success('录音已开始')
  }
}

// 处理删除批注
const handleDeleteAnnotation = (annotationId: number) => {
  ElMessageBox.confirm('确定要删除这条批注吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    annotations.value = annotations.value.filter(annotation => annotation.id !== annotationId)
    ElMessage.success('批注已删除')
  }).catch(() => {
    // 取消删除
  })
}

// 处理清空所有批注
const handleClearAllAnnotations = () => {
  ElMessageBox.confirm('确定要清空所有批注吗？此操作不可恢复。', '清空确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'error'
  }).then(() => {
    annotations.value = []
    ElMessage.success('所有批注已清空')
  }).catch(() => {
    // 取消清空
  })
}

// 处理应用评语模板
const handleApplyTemplate = (templateContent: string) => {
  comment.value = comment.value ? `${comment.value}\n\n${templateContent}` : templateContent
  ElMessage.success('评语模板已应用')
}

// 处理保存草稿
const handleSaveDraft = () => {
  console.log('保存草稿', { score: score.value, comment: comment.value, annotations: annotations.value })
  ElMessage.success('草稿已保存')
}

// 处理提交审阅
const handleSubmitReview = () => {
  if (!score.value) {
    ElMessage.warning('请输入评分')
    return
  }

  if (!comment.value.trim()) {
    ElMessage.warning('请输入评语')
    return
  }

  ElMessageBox.confirm('确定要提交审阅吗？提交后不可修改。', '提交确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    console.log('提交审阅', { score: score.value, comment: comment.value, annotations: annotations.value })
    ElMessage.success('审阅已提交')
    // 这里可以添加跳转到其他页面的逻辑
  }).catch(() => {
    // 取消提交
  })
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
  // 这里可以添加返回上一页的逻辑
}
</script>

<style scoped>
.document-review-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 20px;
  margin-top: 20px;
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

.right-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 文档信息卡片 */
.document-info-card {
  margin-bottom: 20px;
}

.document-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.document-title h3 {
  margin: 0;
  font-size: 18px;
  color: #1e3a8a;
}

.document-meta {
  background-color: #f8fafc;
  padding: 16px;
  border-radius: 4px;
}

/* 文档预览卡片 */
.document-preview-card {
  flex: 1;
  min-height: 600px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-tools {
  display: flex;
  align-items: center;
  gap: 8px;
}

.document-preview-area {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.pdf-container {
  flex: 1;
  background-color: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  min-height: 500px;
  overflow: auto;
  padding: 20px;
}

.mock-pdf-preview {
  text-align: center;
  padding: 40px 20px;
  color: #64748b;
}

.mock-pdf-preview h4 {
  margin-bottom: 20px;
  color: #1e3a8a;
}

.pdf-content {
  background-color: #fff;
  padding: 40px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  max-width: 800px;
  margin: 20px auto;
  text-align: left;
}

.pdf-page {
  min-height: 600px;
}

.page-controls {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 16px 0;
  border-top: 1px solid #e2e8f0;
}

.page-info {
  color: #64748b;
}

/* 批注工具 */
.annotation-tools-card {
  margin-bottom: 20px;
}

.annotation-tools {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.voice-annotation {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  background-color: #f8fafc;
  border-radius: 4px;
}

.record-duration {
  color: #64748b;
  font-size: 14px;
}

/* 批注列表 */
.annotations-list-card {
  margin-bottom: 20px;
  max-height: 400px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.annotation-item {
  margin: 0;
  border-left: 4px solid #3b82f6;
}

.annotation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.annotation-type {
  display: flex;
  align-items: center;
  gap: 8px;
}

.annotation-page {
  font-size: 12px;
  color: #64748b;
}

.annotation-content {
  font-size: 14px;
  line-height: 1.5;
  color: #334155;
}

/* 评分面板 */
.scoring-panel-card {
  margin-bottom: 20px;
}

.scoring-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.scoring-panel h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #1e3a8a;
}

.score-section {
  background-color: #f8fafc;
  padding: 20px;
  border-radius: 4px;
}

.score-input {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}

.comment-template-section {
  background-color: #f8fafc;
  padding: 20px;
  border-radius: 4px;
}

.comment-templates {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.comment-section {
  background-color: #f8fafc;
  padding: 20px;
  border-radius: 4px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
  padding: 20px 0;
  background-color: #f8fafc;
  border-radius: 4px;
}
</style>