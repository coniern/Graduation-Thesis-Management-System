<template>
  <div class="similarity-check-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="查重管理"
    />

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：查重提交区域 -->
      <div class="left-panel">
        <!-- 查重说明卡片 -->
        <el-card class="info-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>查重说明</span>
            </div>
          </template>
          <el-alert
            title="注意事项"
            type="warning"
            :closable="false"
            show-icon
          >
            <ul>
              <li>支持PDF、Word格式文件，大小不超过50MB</li>
              <li>系统将自动上传至第三方查重平台（模拟知网）</li>
              <li>查重结果将在3-5分钟内返回，请耐心等待</li>
              <li>每位学生有3次免费查重机会</li>
              <li>终稿提交前请务必进行查重</li>
            </ul>
          </el-alert>
          <div class="check-info">
            <el-descriptions :column="1" :border="false" size="small">
              <el-descriptions-item label="剩余查重次数">{{ remainingChecks }}次</el-descriptions-item>
              <el-descriptions-item label="当前阶段">{{ currentPhase }}</el-descriptions-item>
              <el-descriptions-item label="系统状态">
                <el-tag type="success">正常</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>

        <!-- 查重文件选择 -->
        <el-card class="upload-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>选择查重文件</span>
            </div>
          </template>

          <!-- 文件上传组件 -->
          <el-upload
            ref="uploadRef"
            class="upload-dragger"
            action="/api/similarity/check"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :file-list="fileList"
            drag
            :multiple="false"
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

          <!-- 查重设置 -->
          <el-form :model="checkSettings" :inline="false" size="small" style="margin-top: 20px;">
            <el-form-item label="查重类型" required>
              <el-radio-group v-model="checkSettings.type">
                <el-radio label="1">初稿查重</el-radio>
                <el-radio label="2">终稿查重</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="查重范围">
              <el-checkbox-group v-model="checkSettings.scope">
                <el-checkbox label="1">中文期刊库</el-checkbox>
                <el-checkbox label="2">硕士论文库</el-checkbox>
                <el-checkbox label="3">博士论文库</el-checkbox>
                <el-checkbox label="4">会议论文库</el-checkbox>
                <el-checkbox label="5">互联网资源</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="checkSettings.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注信息（可选）"
              />
            </el-form-item>
          </el-form>

          <!-- 提交查重按钮 -->
          <div class="upload-actions">
            <el-button
              type="primary"
              size="large"
              :disabled="fileList.length === 0"
              @click="handleSubmitCheck"
            >
              <el-icon><check /></el-icon>提交查重
            </el-button>
            <el-button
              type="danger"
              size="large"
              @click="handleClearFiles"
            >
              <el-icon><delete /></el-icon>清空
            </el-button>
          </div>
        </el-card>

        <!-- 历史查重记录 -->
        <el-card class="history-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>历史查重记录</span>
              <el-tag type="info" size="small">{{ checkHistory.length }}条记录</el-tag>
            </div>
          </template>

          <el-empty v-if="checkHistory.length === 0" description="暂无查重记录" />
          <el-timeline v-else>
            <el-timeline-item
              v-for="item in checkHistory"
              :key="item.id"
              :timestamp="item.checkTime"
              :color="getCheckResultColor(item.similarity)
            >
              <el-card :bordered="false" shadow="hover" class="history-item">
                <div class="history-header">
                  <div class="history-info">
                    <h4>{{ item.fileName }}</h4>
                    <div class="history-meta">
                      <span>查重类型：{{ item.type === '1' ? '初稿查重' : '终稿查重' }}</span>
                      <span>状态：
                        <el-tag :type="item.status === 'completed' ? 'success' : 'warning'">
                          {{ item.status === 'completed' ? '已完成' : '处理中' }}
                        </el-tag>
                      </span>
                    </div>
                  </div>
                  <div class="similarity-result">
                    <div class="similarity-score" :class="getCheckResultClass(item.similarity)">
                      {{ item.similarity }}%
                    </div>
                    <div class="similarity-label">重复率</div>
                  </div>
                </div>
                <div class="history-footer">
                  <el-button
                    v-if="item.status === 'completed'"
                    type="primary"
                    size="small"
                    @click="handleViewCheckDetail(item.id)"
                  >
                    查看详情
                  </el-button>
                  <el-button
                    type="success"
                    size="small"
                    @click="handleDownloadReport(item.id)"
                  >
                    下载报告
                  </el-button>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>

      <!-- 右侧：查重结果展示和降重建议 -->
      <div class="right-panel">
        <!-- 查重结果概览 -->
        <el-card class="result-overview-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>查重结果概览</span>
              <el-tag v-if="currentCheckResult" :type="getCheckResultColor(currentCheckResult.similarity)" size="small">
                {{ currentCheckResult.type === '1' ? '初稿查重' : '终稿查重' }}
              </el-tag>
            </div>
          </template>

          <div v-if="!currentCheckResult" class="no-result">
            <el-empty description="暂无查重结果" />
            <el-button type="primary" style="margin-top: 20px" @click="handleBackToList">
              返回查重列表
            </el-button>
          </div>
          <div v-else class="result-content">
            <!-- 重复率环形图 -->
            <div class="similarity-chart">
              <div ref="similarityChartRef" class="chart-container"></div>
              <div class="chart-center">
                <div class="similarity-percentage" :class="getCheckResultClass(currentCheckResult.similarity)">
                  {{ currentCheckResult.similarity }}%
                </div>
                <div class="similarity-text">重复率</div>
              </div>
            </div>

            <!-- 查重结果详情 -->
            <div class="result-details">
              <el-descriptions :column="2" :border="false">
                <el-descriptions-item label="查重文件">{{ currentCheckResult.fileName }}</el-descriptions-item>
                <el-descriptions-item label="查重时间">{{ currentCheckResult.checkTime }}</el-descriptions-item>
                <el-descriptions-item label="总字数">{{ currentCheckResult.totalWords }}字</el-descriptions-item>
                <el-descriptions-item label="重复字数">{{ currentCheckResult.duplicateWords }}字</el-descriptions-item>
                <el-descriptions-item label="相似来源">
                  <el-tag
                    v-for="source in currentCheckResult.similarSources"
                    :key="source"
                    size="small"
                    type="info"
                    effect="light"
                    style="margin-right: 4px; margin-bottom: 4px;"
                  >
                    {{ source }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="结果状态">
                  <el-tag :type="getCheckResultColor(currentCheckResult.similarity)">
                    {{ getCheckResultText(currentCheckResult.similarity) }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
        </el-card>

        <!-- 相似片段和降重建议 -->
        <div v-if="currentCheckResult" class="result-tabs">
          <el-tabs v-model="activeTab" type="card">
            <el-tab-pane label="相似片段" name="similar-fragments">
              <el-card class="fragment-card" shadow="hover">
                <el-empty v-if="similarFragments.length === 0" description="暂无相似片段" />
                <div v-else class="fragment-list">
                  <el-collapse>
                    <el-collapse-item
                      v-for="(fragment, index) in similarFragments"
                      :key="fragment.id"
                      :title="`相似片段 ${index + 1} - 重复率 ${fragment.similarity}%`"
                    >
                      <div class="fragment-content">
                        <div class="original-text">
                          <h5>原文片段</h5>
                          <div class="text-content">{{ fragment.originalText }}</div>
                        </div>
                        <div class="similar-text">
                          <h5>相似来源</h5>
                          <div class="text-content" v-html="fragment.similarText"></div>
                          <div class="similar-source">来源：{{ fragment.source }}</div>
                        </div>
                      </div>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </el-card>
            </el-tab-pane>

            <el-tab-pane label="降重建议" name="recommendations">
              <el-card class="recommendation-card" shadow="hover">
                <h4>智能降重建议</h4>
                <el-divider />
                <el-timeline>
                  <el-timeline-item
                    v-for="(recommendation, index) in recommendations"
                    :key="index"
                    color="success"
                  >
                    <div class="recommendation-item">
                      <h5>{{ recommendation.title }}</h5>
                      <p>{{ recommendation.content }}</p>
                      <el-tag type="primary" size="small" style="margin-top: 8px;">
                        {{ recommendation.type }}
                      </el-tag>
                    </div>
                  </el-timeline-item>
                </el-timeline>

                <el-card class="recommendation-tips" shadow="hover" style="margin-top: 20px;">
                  <template #header>
                    <div class="card-header">
                      <span>降重技巧</span>
                    </div>
                  </template>
                  <ul class="tips-list">
                    <li>同义词替换：使用同义词替换重复词语</li>
                    <li>句式转换：将主动句改为被动句，或将长句拆分为短句</li>
                    <li>图表转换：将重复的文字内容转换为图表</li>
                    <li>引用规范：正确使用引用格式，避免直接复制粘贴</li>
                    <li>内容扩展：在重复内容基础上添加自己的理解和分析</li>
                  </ul>
                </el-card>
              </el-card>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { UploadFilled, Check, Delete, ArrowDown } from '@element-plus/icons-vue'

// 剩余查重次数
const remainingChecks = ref(2)

// 当前阶段
const currentPhase = ref('中期阶段')

// 文件上传引用
const uploadRef = ref()

// 文件列表
const fileList = ref([])

// 查重设置
const checkSettings = ref({
  type: '1',
  scope: ['1', '2', '3', '4', '5'],
  remark: ''
})

// 查重历史记录
const checkHistory = ref([
  {
    id: 1,
    fileName: '开题报告初稿.pdf',
    type: '1',
    status: 'completed',
    similarity: 15.2,
    checkTime: '2025-12-20 14:30'
  },
  {
    id: 2,
    fileName: '中期报告.docx',
    type: '1',
    status: 'completed',
    similarity: 8.5,
    checkTime: '2025-12-19 09:00'
  }
])

// 当前查重结果
const currentCheckResult = ref(null)

// 激活的标签页
const activeTab = ref('similar-fragments')

// 相似片段
const similarFragments = ref([
  {
    id: 1,
    originalText: '随着金融科技的快速发展，互联网金融已经成为金融行业的重要组成部分。互联网金融的出现，改变了传统金融的服务模式和运营方式，为金融行业带来了新的机遇和挑战。',
    similarText: '随着<span style="background-color: #ffcccc;">金融科技的快速发展</span>，<span style="background-color: #ffcccc;">互联网金融已经成为金融行业的重要组成部分</span>。<span style="background-color: #ffcccc;">互联网金融的出现</span>，<span style="background-color: #ffcccc;">改变了传统金融的服务模式和运营方式</span>，为金融行业带来了新的机遇和挑战。',
    source: '互联网金融发展研究，2024年第3期',
    similarity: 75
  },
  {
    id: 2,
    originalText: '金融风险管理是金融机构的核心业务之一，有效的风险管理可以帮助金融机构降低损失，提高盈利能力。金融风险管理包括市场风险、信用风险、操作风险等多种类型。',
    similarText: '<span style="background-color: #ffcccc;">金融风险管理是金融机构的核心业务之一</span>，<span style="background-color: #ffcccc;">有效的风险管理可以帮助金融机构降低损失</span>，提高盈利能力。<span style="background-color: #ffcccc;">金融风险管理包括市场风险、信用风险、操作风险等多种类型</span>。',
    source: '金融风险管理实务，2023年',
    similarity: 68
  }
])

// 降重建议
const recommendations = ref([
  {
    title: '同义词替换',
    content: '将"快速发展"替换为"迅猛发展"，"重要组成部分"替换为"关键构成要素"，"服务模式"替换为"服务范式"',
    type: '文本优化'
  },
  {
    title: '句式转换',
    content: '将主动句"互联网金融的出现，改变了传统金融的服务模式和运营方式"转换为被动句"传统金融的服务模式和运营方式因互联网金融的出现而发生了改变"',
    type: '句式优化'
  },
  {
    title: '内容扩展',
    content: '在"为金融行业带来了新的机遇和挑战"后添加具体的机遇和挑战，如"机遇包括降低服务成本、扩大服务范围等，挑战包括监管风险、技术风险等"',
    type: '内容扩展'
  },
  {
    title: '引用规范',
    content: '对于引用的内容，添加正确的引用格式，如"(张三, 2024)"',
    type: '引用优化'
  }
])

// 查重结果环形图
const similarityChartRef = ref<HTMLElement>()
let similarityChart: echarts.ECharts | null = null

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
  ElMessage.success('查重请求已提交，结果将在3-5分钟内返回')
  console.log('查重请求提交成功', response, file, fileList)
  // 模拟添加查重记录
  checkHistory.value.unshift({
    id: Date.now(),
    fileName: file.name,
    type: checkSettings.value.type,
    status: 'processing',
    similarity: 0,
    checkTime: new Date().toLocaleString()
  })
  // 减少查重次数
  remainingChecks.value--
}

// 处理文件上传失败
const handleUploadError = (error: any, file: any, fileList: any) => {
  ElMessage.error('查重请求提交失败，请重试')
  console.error('查重请求提交失败', error, file, fileList)
}

// 提交查重
const handleSubmitCheck = () => {
  if (!uploadRef.value || fileList.length === 0) return
  uploadRef.value.submit()
}

// 清空文件列表
const handleClearFiles = () => {
  if (!uploadRef.value) return
  uploadRef.value.clearFiles()
}

// 返回列表
const handleBackToList = () => {
  currentCheckResult.value = null
}

// 查看查重详情
const handleViewCheckDetail = (checkId: number) => {
  // 模拟获取查重详情
  currentCheckResult.value = {
    id: checkId,
    fileName: '开题报告初稿.pdf',
    type: '1',
    status: 'completed',
    similarity: 15.2,
    checkTime: '2025-12-20 14:30',
    totalWords: 5000,
    duplicateWords: 760,
    similarSources: ['中文期刊库', '互联网资源']
  }
  // 初始化查重结果图表
  initSimilarityChart()
}

// 下载查重报告
const handleDownloadReport = (checkId: number) => {
  console.log('下载查重报告', checkId)
  ElMessage.success('开始下载查重报告')
}

// 获取查重结果颜色
const getCheckResultColor = (similarity: number) => {
  if (similarity < 15) return 'success'
  if (similarity < 30) return 'warning'
  return 'danger'
}

// 获取查重结果样式类
const getCheckResultClass = (similarity: number) => {
  if (similarity < 15) return 'low'
  if (similarity < 30) return 'medium'
  return 'high'
}

// 获取查重结果文本
const getCheckResultText = (similarity: number) => {
  if (similarity < 15) return '合格'
  if (similarity < 30) return '需要修改'
  return '不合格'
}

// 初始化查重结果环形图
const initSimilarityChart = () => {
  if (!similarityChartRef.value || !currentCheckResult.value) return
  similarityChart = echarts.init(similarityChartRef.value)
  
  const similarity = currentCheckResult.value.similarity
  const normal = 100 - similarity
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}%'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: ['重复率', '原创率']
    },
    series: [
      {
        name: '查重结果',
        type: 'pie',
        radius: ['60%', '80%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: false
          }
        },
        labelLine: {
          show: false
        },
        data: [
          {
            value: similarity,
            name: '重复率',
            itemStyle: {
              color: similarity < 15 ? '#67c23a' : similarity < 30 ? '#e6a23c' : '#f56c6c'
            }
          },
          {
            value: normal,
            name: '原创率',
            itemStyle: {
              color: '#409eff'
            }
          }
        ]
      }
    ]
  }
  
  similarityChart.setOption(option)
}

// 监听当前查重结果变化，初始化图表
watch(currentCheckResult, (newVal) => {
  if (newVal) {
    initSimilarityChart()
  }
})

// 组件挂载时初始化
onMounted(() => {
  // 如果有当前查重结果，初始化图表
  if (currentCheckResult.value) {
    initSimilarityChart()
  }
  
  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    similarityChart?.resize()
  })
})

// 页面返回处理
const handleBack = () => {
  console.log('返回')
}
</script>

<style scoped>
.similarity-check-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
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

.info-card, .upload-card, .history-card {
  flex-shrink: 0;
}

.check-info {
  margin-top: 16px;
  padding: 16px;
  background-color: #f0f9ff;
  border-radius: 4px;
}

.upload-dragger {
  margin-bottom: 16px;
}

.upload-actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
  justify-content: center;
}

.history-item {
  margin: 0;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.history-info h4 {
  margin: 0;
  font-size: 14px;
  color: #1e3a8a;
}

.history-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #6b7280;
  margin-top: 4px;
}

.similarity-result {
  text-align: center;
}

.similarity-score {
  font-size: 24px;
  font-weight: bold;
}

.similarity-score.low {
  color: #67c23a;
}

.similarity-score.medium {
  color: #e6a23c;
}

.similarity-score.high {
  color: #f56c6c;
}

.similarity-label {
  font-size: 12px;
  color: #6b7280;
  margin-top: 4px;
}

.history-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 16px;
}

.right-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.result-overview-card {
  flex-shrink: 0;
}

.no-result {
  padding: 40px 0;
  text-align: center;
}

.result-content {
  padding: 20px 0;
}

.similarity-chart {
  position: relative;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 100%;
}

.chart-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.similarity-percentage {
  font-size: 36px;
  font-weight: bold;
}

.similarity-percentage.low {
  color: #67c23a;
}

.similarity-percentage.medium {
  color: #e6a23c;
}

.similarity-percentage.high {
  color: #f56c6c;
}

.similarity-text {
  font-size: 14px;
  color: #6b7280;
  margin-top: 4px;
}

.result-details {
  margin-top: 20px;
}

.result-tabs {
  margin-top: 20px;
}

.fragment-card, .recommendation-card {
  margin-bottom: 20px;
}

.fragment-list {
  max-height: 500px;
  overflow-y: auto;
}

.fragment-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.original-text, .similar-text {
  padding: 16px;
  background-color: #f9fafb;
  border-radius: 4px;
}

.original-text h5, .similar-text h5 {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #1e3a8a;
}

.text-content {
  font-size: 13px;
  line-height: 1.6;
}

.similar-source {
  margin-top: 8px;
  font-size: 12px;
  color: #6b7280;
}

.recommendation-item {
  padding: 16px;
  background-color: #f0f9ff;
  border-radius: 4px;
}

.recommendation-item h5 {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #1e3a8a;
}

.recommendation-item p {
  margin: 0;
  font-size: 13px;
  line-height: 1.6;
  color: #374151;
}

.tips-list {
  padding-left: 20px;
}

.tips-list li {
  margin-bottom: 8px;
  font-size: 13px;
  color: #374151;
}
</style>