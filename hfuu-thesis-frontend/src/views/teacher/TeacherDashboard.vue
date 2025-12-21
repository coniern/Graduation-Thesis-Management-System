<template>
  <div class="teacher-dashboard-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="导师工作台"
    />

    <!-- 统计卡片区域 -->
    <div class="stats-grid">
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-info">
            <div class="stats-value">{{ totalStudents }}</div>
            <div class="stats-label">指导学生总数</div>
          </div>
          <div class="stats-icon">
            <el-icon><user-filled /></el-icon>
          </div>
        </div>
      </el-card>
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-info">
            <div class="stats-value">{{ pendingReviews }}</div>
            <div class="stats-label">待审阅文档</div>
          </div>
          <div class="stats-icon">
            <el-icon><document-checked /></el-icon>
          </div>
        </div>
      </el-card>
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-info">
            <div class="stats-value">{{ completedTheses }}</div>
            <div class="stats-label">已完成论文</div>
          </div>
          <div class="stats-icon">
            <el-icon><success-filled /></el-icon>
          </div>
        </div>
      </el-card>
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-info">
            <div class="stats-value">{{ avgSimilarity }}%</div>
            <div class="stats-label">平均重复率</div>
          </div>
          <div class="stats-icon">
            <el-icon><percent /></el-icon>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：指导学生列表 -->
      <div class="left-panel">
        <!-- 指导学生列表 -->
        <el-card class="student-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>指导学生列表</span>
              <el-tag type="info" size="small">{{ students.length }}名学生</el-tag>
            </div>
          </template>

          <!-- 搜索和筛选 -->
          <div class="student-search">
            <el-input
              v-model="studentSearchQuery"
              placeholder="搜索学生姓名或学号"
              clearable
              @keyup.enter="handleStudentSearch"
              prefix-icon="Search"
            />
            <el-select
              v-model="studentStatusFilter"
              placeholder="筛选状态"
              clearable
              @change="handleStudentFilter"
              style="margin-left: 8px;"
            >
              <el-option label="全部" value="" />
              <el-option label="选题阶段" value="选题阶段" />
              <el-option label="开题阶段" value="开题阶段" />
              <el-option label="中期阶段" value="中期阶段" />
              <el-option label="答辩阶段" value="答辩阶段" />
              <el-option label="归档阶段" value="归档阶段" />
            </el-select>
          </div>

          <!-- 学生列表 -->
          <el-table
            :data="filteredStudents"
            border
            stripe
            size="small"
            style="width: 100%"
            @row-click="handleStudentRowClick"
            :row-class-name="getStudentRowClass"
          >
            <el-table-column prop="studentNo" label="学号" width="120" />
            <el-table-column prop="name" label="姓名" width="100">
              <template #default="scope">
                <div class="student-name">
                  <el-avatar :size="24" :src="scope.row.avatar || defaultAvatar" />
                  <span>{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="className" label="班级" width="120" />
            <el-table-column prop="currentPhase" label="当前阶段" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="similarity" label="重复率" width="100">
              <template #default="scope">
                <span :class="getSimilarityClass(scope.row.similarity)">
                  {{ scope.row.similarity }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="成绩" width="80">
              <template #default="scope">
                <span>{{ scope.row.score || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleViewStudentDetail(scope.row.id)"
                >
                  详情
                </el-button>
                <el-button
                  type="info"
                  size="small"
                  @click="handleViewStudentDocuments(scope.row.id)"
                >
                  文档
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>

      <!-- 右侧：甘特图和待审阅文档 -->
      <div class="right-panel">
        <!-- 学生进度甘特图 -->
        <el-card class="gantt-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>学生进度甘特图</span>
              <el-dropdown @command="handleGanttCommand">
                <el-button size="small" type="primary">
                  操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="export">导出甘特图</el-dropdown-item>
                    <el-dropdown-item command="refresh">刷新数据</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>

          <div ref="ganttChartRef" class="gantt-chart"></div>
        </el-card>

        <!-- 待审阅文档列表 -->
        <el-card class="review-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>待审阅文档</span>
              <el-tag type="danger" size="small">{{ pendingDocuments.length }}个文档</el-tag>
            </div>
          </template>

          <el-empty v-if="pendingDocuments.length === 0" description="暂无待审阅文档" />
          <el-timeline v-else>
            <el-timeline-item
              v-for="doc in pendingDocuments"
              :key="doc.id"
              :timestamp="doc.submitTime"
              placement="top"
            >
              <el-card :bordered="false" shadow="hover" class="document-item">
                <div class="document-header">
                  <div class="document-info">
                    <h4 class="document-title">{{ doc.title }}</h4>
                    <div class="document-meta">
                      <span>学生：{{ doc.studentName }}</span>
                      <span>阶段：{{ doc.phase }}</span>
                      <span>格式：{{ doc.format }}</span>
                    </div>
                  </div>
                  <div class="document-actions">
                    <el-button
                      type="primary"
                      size="small"
                      @click="handleReviewDocument(doc.id)"
                    >
                      审阅
                    </el-button>
                    <el-button
                      type="info"
                      size="small"
                      @click="handlePreviewDocument(doc.id)"
                    >
                      预览
                    </el-button>
                  </div>
                </div>
                <div class="document-progress">
                  <div class="progress-label">文档大小：{{ formatFileSize(doc.size) }}</div>
                  <div class="progress-bar">
                    <el-progress
                      :percentage="doc.similarity || 0"
                      :color="getSimilarityColor(doc.similarity || 0)"
                      :stroke-width="8"
                      :show-text="false"
                    />
                    <span class="progress-text">{{ doc.similarity || 0 }}% 重复率</span>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { UserFilled, DocumentChecked, SuccessFilled, Percent, Search, ArrowDown } from '@element-plus/icons-vue'

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 统计数据
const totalStudents = ref(8)
const pendingReviews = ref(3)
const completedTheses = ref(5)
const avgSimilarity = ref(12.5)

// 学生列表数据
const students = ref([
  {
    id: 1,
    studentNo: '202201001',
    name: '张三',
    avatar: '',
    className: '金融2201',
    currentPhase: '中期阶段',
    status: '进行中',
    similarity: 15.2,
    score: null
  },
  {
    id: 2,
    studentNo: '202201002',
    name: '李四',
    avatar: '',
    className: '金融2201',
    currentPhase: '答辩阶段',
    status: '进行中',
    similarity: 8.5,
    score: null
  },
  {
    id: 3,
    studentNo: '202201003',
    name: '王五',
    avatar: '',
    className: '金融2202',
    currentPhase: '归档阶段',
    status: '已完成',
    similarity: 10.8,
    score: 85
  },
  {
    id: 4,
    studentNo: '202201004',
    name: '赵六',
    avatar: '',
    className: '金融2202',
    currentPhase: '开题阶段',
    status: '进行中',
    similarity: 22.3,
    score: null
  },
  {
    id: 5,
    studentNo: '202201005',
    name: '孙七',
    avatar: '',
    className: '金融2203',
    currentPhase: '中期阶段',
    status: '进行中',
    similarity: 18.7,
    score: null
  },
  {
    id: 6,
    studentNo: '202201006',
    name: '周八',
    avatar: '',
    className: '金融2203',
    currentPhase: '归档阶段',
    status: '已完成',
    similarity: 7.9,
    score: 92
  },
  {
    id: 7,
    studentNo: '202201007',
    name: '吴九',
    avatar: '',
    className: '金融2204',
    currentPhase: '答辩阶段',
    status: '进行中',
    similarity: 14.2,
    score: null
  },
  {
    id: 8,
    studentNo: '202201008',
    name: '郑十',
    avatar: '',
    className: '金融2204',
    currentPhase: '开题阶段',
    status: '进行中',
    similarity: 25.6,
    score: null
  }
])

// 学生搜索和筛选
const studentSearchQuery = ref('')
const studentStatusFilter = ref('')

// 筛选后的学生列表
const filteredStudents = computed(() => {
  return students.value.filter(student => {
    // 搜索筛选
    if (studentSearchQuery.value && !student.name.includes(studentSearchQuery.value) && !student.studentNo.includes(studentSearchQuery.value)) {
      return false
    }
    // 状态筛选
    if (studentStatusFilter.value && student.currentPhase !== studentStatusFilter.value) {
      return false
    }
    return true
  })
})

// 待审阅文档列表
const pendingDocuments = ref([
  {
    id: 1,
    title: '开题报告初稿.pdf',
    studentName: '张三',
    phase: '开题阶段',
    format: 'PDF',
    size: 2048000,
    similarity: 15.2,
    submitTime: '2025-12-20 14:30'
  },
  {
    id: 2,
    title: '中期报告.docx',
    studentName: '李四',
    phase: '中期阶段',
    format: 'Word',
    size: 1024000,
    similarity: 8.5,
    submitTime: '2025-12-19 09:00'
  },
  {
    id: 3,
    title: '论文终稿.pdf',
    studentName: '王五',
    phase: '答辩阶段',
    format: 'PDF',
    size: 3072000,
    similarity: 10.8,
    submitTime: '2025-12-18 16:45'
  }
])

// 甘特图引用
const ganttChartRef = ref<HTMLElement>()
let ganttChart: echarts.ECharts | null = null

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

// 获取学生行样式类
const getStudentRowClass = (row: any, index: number) => {
  return row.status === '已完成' ? 'completed-row' : ''
}

// 获取重复率样式类
const getSimilarityClass = (similarity: number) => {
  if (similarity < 15) return 'similarity-low'
  if (similarity < 30) return 'similarity-medium'
  return 'similarity-high'
}

// 获取重复率颜色
const getSimilarityColor = (similarity: number) => {
  if (similarity < 15) return '#67c23a'
  if (similarity < 30) return '#e6a23c'
  return '#f56c6c'
}

// 处理学生搜索
const handleStudentSearch = () => {
  console.log('搜索学生', studentSearchQuery.value)
}

// 处理学生筛选
const handleStudentFilter = () => {
  console.log('筛选学生', studentStatusFilter.value)
}

// 处理学生行点击
const handleStudentRowClick = (row: any) => {
  console.log('点击学生行', row)
}

// 查看学生详情
const handleViewStudentDetail = (studentId: number) => {
  console.log('查看学生详情', studentId)
  ElMessage.info('查看学生详情功能开发中')
}

// 查看学生文档
const handleViewStudentDocuments = (studentId: number) => {
  console.log('查看学生文档', studentId)
  ElMessage.info('查看学生文档功能开发中')
}

// 审阅文档
const handleReviewDocument = (documentId: number) => {
  console.log('审阅文档', documentId)
  ElMessage.info('文档审阅功能开发中')
}

// 预览文档
const handlePreviewDocument = (documentId: number) => {
  console.log('预览文档', documentId)
  ElMessage.info('文档预览功能开发中')
}

// 甘特图命令处理
const handleGanttCommand = (command: string) => {
  console.log('甘特图命令', command)
  ElMessage.info(`${command}功能开发中`)
}

// 初始化甘特图
const initGanttChart = () => {
  if (!ganttChartRef.value) return
  ganttChart = echarts.init(ganttChartRef.value)
  
  // 模拟甘特图数据
  const ganttData = [
    {
      name: '张三',
      type: 'bar',
      stack: '张三',
      emphasis: {
        focus: 'series'
      },
      data: [
        { value: [new Date('2025-10-01').getTime(), new Date('2025-10-31').getTime()], itemStyle: { color: '#1e3a8a' } },
        { value: [new Date('2025-11-01').getTime(), new Date('2025-11-30').getTime()], itemStyle: { color: '#3b82f6' } },
        { value: [new Date('2025-12-01').getTime(), new Date('2025-12-31').getTime()], itemStyle: { color: '#60a5fa' } },
        { value: [new Date('2026-01-01').getTime(), new Date('2026-01-31').getTime()], itemStyle: { color: '#93c5fd' } },
        { value: [new Date('2026-02-01').getTime(), new Date('2026-02-28').getTime()], itemStyle: { color: '#bfdbfe' } }
      ]
    },
    {
      name: '李四',
      type: 'bar',
      stack: '李四',
      emphasis: {
        focus: 'series'
      },
      data: [
        { value: [new Date('2025-10-01').getTime(), new Date('2025-10-31').getTime()], itemStyle: { color: '#1e3a8a' } },
        { value: [new Date('2025-11-01').getTime(), new Date('2025-11-30').getTime()], itemStyle: { color: '#3b82f6' } },
        { value: [new Date('2025-12-01').getTime(), new Date('2025-12-31').getTime()], itemStyle: { color: '#60a5fa' } },
        { value: [new Date('2026-01-01').getTime(), new Date('2026-01-31').getTime()], itemStyle: { color: '#93c5fd' } },
        { value: [new Date('2026-02-01').getTime(), new Date('2026-02-28').getTime()], itemStyle: { color: '#bfdbfe' } }
      ]
    },
    {
      name: '王五',
      type: 'bar',
      stack: '王五',
      emphasis: {
        focus: 'series'
      },
      data: [
        { value: [new Date('2025-10-01').getTime(), new Date('2025-10-31').getTime()], itemStyle: { color: '#1e3a8a' } },
        { value: [new Date('2025-11-01').getTime(), new Date('2025-11-30').getTime()], itemStyle: { color: '#3b82f6' } },
        { value: [new Date('2025-12-01').getTime(), new Date('2025-12-31').getTime()], itemStyle: { color: '#60a5fa' } },
        { value: [new Date('2026-01-01').getTime(), new Date('2026-01-31').getTime()], itemStyle: { color: '#93c5fd' } },
        { value: [new Date('2026-02-01').getTime(), new Date('2026-02-28').getTime()], itemStyle: { color: '#bfdbfe' } }
      ]
    }
  ]
  
  const option = {
    tooltip: {
      formatter: function(params: any) {
        const data = params.data
        return `${params.name}<br/>${params.seriesName}: ${new Date(data.value[0]).toLocaleDateString()} - ${new Date(data.value[1]).toLocaleDateString()}`
      }
    },
    legend: {
      data: ['选题阶段', '开题阶段', '中期阶段', '答辩阶段', '归档阶段'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'time',
      axisLabel: {
        formatter: '{yyyy}-{MM}-{dd}'
      }
    },
    yAxis: {
      type: 'category',
      data: ['张三', '李四', '王五']
    },
    series: ganttData
  }
  
  ganttChart.setOption(option)
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
}

// 组件挂载时初始化甘特图
onMounted(() => {
  initGanttChart()
  
  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    ganttChart?.resize()
  })
})
</script>

<style scoped>
.teacher-dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stats-card {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stats-content {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0 20px;
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #1e3a8a;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #6b7280;
}

.stats-icon {
  font-size: 32px;
  color: #3b82f6;
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

.right-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.student-list-card {
  flex: 1;
  min-height: 500px;
}

.student-search {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.student-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.completed-row {
  background-color: #f0f9ff;
}

.similarity-low {
  color: #67c23a;
}

.similarity-medium {
  color: #e6a23c;
}

.similarity-high {
  color: #f56c6c;
}

.gantt-card {
  flex: 1;
  min-height: 400px;
}

.gantt-chart {
  height: 350px;
  background-color: #fafafa;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
}

.review-list-card {
  flex-shrink: 0;
}

.document-item {
  margin: 0;
}

.document-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.document-title {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #1e3a8a;
}

.document-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #6b7280;
}

.document-actions {
  display: flex;
  gap: 8px;
}

.document-progress {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.progress-label {
  font-size: 12px;
  color: #6b7280;
  margin-right: 16px;
}

.progress-bar {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}

.progress-text {
  font-size: 12px;
  color: #6b7280;
  min-width: 70px;
}
</style>