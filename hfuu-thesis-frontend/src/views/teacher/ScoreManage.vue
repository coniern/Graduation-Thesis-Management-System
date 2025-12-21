<template>
  <div class="score-manage-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="成绩管理"
    />

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：成绩统计图表 -->
      <div class="left-panel">
        <!-- 成绩分布图表 -->
        <el-card class="score-chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>成绩分布</span>
            </div>
          </template>
          <div ref="scoreDistributionChartRef" class="chart-container"></div>
        </el-card>

        <!-- 成绩趋势图表 -->
        <el-card class="trend-chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>成绩趋势</span>
            </div>
          </template>
          <div ref="scoreTrendChartRef" class="chart-container"></div>
        </el-card>

        <!-- 成绩统计信息 -->
        <el-card class="stats-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>成绩统计</span>
            </div>
          </template>
          <div class="score-stats">
            <el-row :gutter="16">
              <el-col :span="12">
                <div class="stat-item">
                  <div class="stat-label">平均分</div>
                  <div class="stat-value">{{ averageScore }}分</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="stat-item">
                  <div class="stat-label">最高分</div>
                  <div class="stat-value">{{ highestScore }}分</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="stat-item">
                  <div class="stat-label">最低分</div>
                  <div class="stat-value">{{ lowestScore }}分</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="stat-item">
                  <div class="stat-label">优秀率</div>
                  <div class="stat-value">{{ excellentRate }}%</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>

      <!-- 右侧：成绩列表和录入 -->
      <div class="right-panel">
        <!-- 成绩录入表单 -->
        <el-card class="score-form-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>{{ isEditing ? '编辑成绩' : '成绩录入' }}</span>
            </div>
          </template>

          <el-form :model="scoreForm" :rules="scoreRules" ref="scoreFormRef" label-width="100px">
            <el-form-item label="学生姓名" prop="studentName">
              <el-input v-model="scoreForm.studentName" readonly placeholder="请选择学生" />
            </el-form-item>

            <el-form-item label="学号" prop="studentNo">
              <el-input v-model="scoreForm.studentNo" readonly placeholder="学生学号" />
            </el-form-item>

            <el-form-item label="成绩" prop="score">
              <el-slider
                v-model="scoreForm.score"
                :min="0"
                :max="100"
                :marks="{ 0: '0', 60: '60', 80: '80', 100: '100' }"
              />
              <el-input-number
                v-model="scoreForm.score"
                :min="0"
                :max="100"
                style="margin-left: 16px;"
              />
            </el-form-item>

            <el-form-item label="评分等级" prop="grade">
              <el-select v-model="scoreForm.grade" placeholder="自动计算">
                <el-option label="优秀" value="优秀" />
                <el-option label="良好" value="良好" />
                <el-option label="中等" value="中等" />
                <el-option label="及格" value="及格" />
                <el-option label="不及格" value="不及格" />
              </el-select>
            </el-form-item>

            <el-form-item label="评语" prop="comment">
              <el-input
                v-model="scoreForm.comment"
                type="textarea"
                placeholder="输入评语..."
                rows="4"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSubmitScore" :loading="isSubmitting">
                {{ isEditing ? '更新成绩' : '提交成绩' }}
              </el-button>
              <el-button @click="handleResetForm" :disabled="isSubmitting">重置</el-button>
              <el-button v-if="isEditing" type="danger" @click="handleCancelEdit" :disabled="isSubmitting">
                取消编辑
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 成绩列表 -->
        <el-card class="score-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>学生成绩列表</span>
              <el-tag type="info" size="small">{{ scores.length }}名学生</el-tag>
            </div>
          </template>

          <!-- 搜索和筛选 -->
          <div class="score-search">
            <el-input
              v-model="scoreSearchQuery"
              placeholder="搜索学生姓名或学号"
              clearable
              @keyup.enter="handleScoreSearch"
              prefix-icon="Search"
              style="margin-right: 16px;"
            />
            <el-select
              v-model="scoreGradeFilter"
              placeholder="筛选等级"
              clearable
              @change="handleScoreFilter"
            >
              <el-option label="全部" value="" />
              <el-option label="优秀" value="优秀" />
              <el-option label="良好" value="良好" />
              <el-option label="中等" value="中等" />
              <el-option label="及格" value="及格" />
              <el-option label="不及格" value="不及格" />
            </el-select>
          </div>

          <!-- 成绩表格 -->
          <el-table
            :data="filteredScores"
            border
            stripe
            size="small"
            style="width: 100%; margin-top: 16px;"
          >
            <el-table-column prop="studentNo" label="学号" width="120" />
            <el-table-column prop="studentName" label="姓名" width="100" />
            <el-table-column prop="className" label="班级" width="120" />
            <el-table-column prop="score" label="成绩" width="100">
              <template #default="scope">
                <el-tag :type="getScoreType(scope.row.score)">
                  {{ scope.row.score }}分
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="grade" label="等级" width="100">
              <template #default="scope">
                <el-tag :type="getGradeType(scope.row.grade)">
                  {{ scope.row.grade }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="comment" label="评语" show-overflow-tooltip />
            <el-table-column prop="submitTime" label="提交时间" width="160" />
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleEditScore(scope.row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="handleDeleteScore(scope.row.id)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <div class="pagination">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="filteredScores.length"
              @size-change="handlePageSizeChange"
              @current-change="handleCurrentPageChange"
            />
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'

// 成绩数据类型定义
interface Score {
  id: number
  studentId: number
  studentName: string
  studentNo: string
  className: string
  score: number
  grade: string
  comment: string
  submitTime: string
}

// 成绩表单类型定义
interface ScoreForm {
  id?: number
  studentId: number
  studentName: string
  studentNo: string
  score: number
  grade: string
  comment: string
}

// 成绩数据
const scores = ref<Score[]>([
  {
    id: 1,
    studentId: 1,
    studentName: '张三',
    studentNo: '202201001',
    className: '金融2201',
    score: 85,
    grade: '良好',
    comment: '该生论文内容完整，结构清晰，逻辑严谨，达到良好水平。',
    submitTime: '2025-12-20 14:30'
  },
  {
    id: 2,
    studentId: 2,
    studentName: '李四',
    studentNo: '202201002',
    className: '金融2201',
    score: 92,
    grade: '优秀',
    comment: '该生论文内容全面，创新性强，理论依据充分，达到优秀水平。',
    submitTime: '2025-12-20 15:00'
  },
  {
    id: 3,
    studentId: 3,
    studentName: '王五',
    studentNo: '202201003',
    className: '金融2202',
    score: 78,
    grade: '良好',
    comment: '该生论文内容基本完整，结构合理，需要进一步完善。',
    submitTime: '2025-12-20 15:30'
  },
  {
    id: 4,
    studentId: 4,
    studentName: '赵六',
    studentNo: '202201004',
    className: '金融2202',
    score: 65,
    grade: '及格',
    comment: '该生论文内容基本符合要求，存在一些问题，需要修改。',
    submitTime: '2025-12-20 16:00'
  },
  {
    id: 5,
    studentId: 5,
    studentName: '孙七',
    studentNo: '202201005',
    className: '金融2203',
    score: 88,
    grade: '优秀',
    comment: '该生论文质量较高，分析深入，具有一定的创新性。',
    submitTime: '2025-12-20 16:30'
  }
])

// 图表引用
const scoreDistributionChartRef = ref<HTMLElement>()
const scoreTrendChartRef = ref<HTMLElement>()
let scoreDistributionChart: echarts.ECharts | null = null
let scoreTrendChart: echarts.ECharts | null = null

// 成绩统计数据
const averageScore = computed(() => {
  if (scores.value.length === 0) return 0
  const sum = scores.value.reduce((acc, score) => acc + score.score, 0)
  return (sum / scores.value.length).toFixed(1)
})

const highestScore = computed(() => {
  if (scores.value.length === 0) return 0
  return Math.max(...scores.value.map(score => score.score))
})

const lowestScore = computed(() => {
  if (scores.value.length === 0) return 0
  return Math.min(...scores.value.map(score => score.score))
})

const excellentRate = computed(() => {
  if (scores.value.length === 0) return 0
  const excellentCount = scores.value.filter(score => score.score >= 85).length
  return ((excellentCount / scores.value.length) * 100).toFixed(1)
})

// 成绩搜索和筛选
const scoreSearchQuery = ref('')
const scoreGradeFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

// 筛选后的成绩列表
const filteredScores = computed(() => {
  const filtered = scores.value.filter(score => {
    // 搜索筛选
    if (scoreSearchQuery.value && !score.studentName.includes(scoreSearchQuery.value) && !score.studentNo.includes(scoreSearchQuery.value)) {
      return false
    }
    // 等级筛选
    if (scoreGradeFilter.value && score.grade !== scoreGradeFilter.value) {
      return false
    }
    return true
  })
  return filtered
})

// 成绩表单
const scoreFormRef = ref()
const isEditing = ref(false)
const isSubmitting = ref(false)
const scoreForm = ref<ScoreForm>({
  studentId: 0,
  studentName: '',
  studentNo: '',
  score: 60,
  grade: '',
  comment: ''
})

// 成绩表单验证规则
const scoreRules = {
  studentName: [
    { required: true, message: '请选择学生', trigger: 'blur' }
  ],
  studentNo: [
    { required: true, message: '请选择学生', trigger: 'blur' }
  ],
  score: [
    { required: true, message: '请输入成绩', trigger: 'blur' },
    { type: 'number', min: 0, max: 100, message: '成绩必须在0-100之间', trigger: 'blur' }
  ],
  grade: [
    { required: true, message: '请选择评分等级', trigger: 'blur' }
  ]
}

// 监听成绩变化，自动计算等级
watch(() => scoreForm.value.score, (newScore) => {
  if (newScore >= 85) {
    scoreForm.value.grade = '优秀'
  } else if (newScore >= 75) {
    scoreForm.value.grade = '良好'
  } else if (newScore >= 65) {
    scoreForm.value.grade = '中等'
  } else if (newScore >= 60) {
    scoreForm.value.grade = '及格'
  } else {
    scoreForm.value.grade = '不及格'
  }
})

// 处理成绩搜索
const handleScoreSearch = () => {
  console.log('搜索成绩', scoreSearchQuery.value)
  currentPage.value = 1
}

// 处理成绩筛选
const handleScoreFilter = () => {
  console.log('筛选成绩', scoreGradeFilter.value)
  currentPage.value = 1
}

// 处理分页大小变化
const handlePageSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 1
}

// 处理当前页变化
const handleCurrentPageChange = (newPage: number) => {
  currentPage.value = newPage
}

// 获取成绩类型
const getScoreType = (score: number) => {
  if (score >= 85) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

// 获取等级类型
const getGradeType = (grade: string) => {
  switch (grade) {
    case '优秀': return 'success'
    case '良好': return 'primary'
    case '中等': return 'info'
    case '及格': return 'warning'
    default: return 'danger'
  }
}

// 提交成绩
const handleSubmitScore = () => {
  scoreFormRef.value.validate((valid: boolean) => {
    if (valid) {
      isSubmitting.value = true
      setTimeout(() => {
        if (isEditing.value) {
          // 更新成绩
          const index = scores.value.findIndex(score => score.id === scoreForm.value.id)
          if (index !== -1) {
            scores.value[index] = {
              ...scores.value[index],
              score: scoreForm.value.score,
              grade: scoreForm.value.grade,
              comment: scoreForm.value.comment,
              submitTime: new Date().toLocaleString()
            }
          }
          ElMessage.success('成绩更新成功')
        } else {
          // 新增成绩
          const newScore: Score = {
            id: scores.value.length + 1,
            studentId: scoreForm.value.studentId,
            studentName: scoreForm.value.studentName,
            studentNo: scoreForm.value.studentNo,
            className: '金融2201', // 模拟班级
            score: scoreForm.value.score,
            grade: scoreForm.value.grade,
            comment: scoreForm.value.comment,
            submitTime: new Date().toLocaleString()
          }
          scores.value.push(newScore)
          ElMessage.success('成绩提交成功')
        }
        handleResetForm()
        isSubmitting.value = false
        // 重新渲染图表
        updateCharts()
      }, 1000)
    }
  })
}

// 重置表单
const handleResetForm = () => {
  scoreFormRef.value.resetFields()
  isEditing.value = false
  scoreForm.value = {
    studentId: 0,
    studentName: '',
    studentNo: '',
    score: 60,
    grade: '',
    comment: ''
  }
}

// 编辑成绩
const handleEditScore = (score: Score) => {
  isEditing.value = true
  scoreForm.value = {
    id: score.id,
    studentId: score.studentId,
    studentName: score.studentName,
    studentNo: score.studentNo,
    score: score.score,
    grade: score.grade,
    comment: score.comment
  }
}

// 取消编辑
const handleCancelEdit = () => {
  handleResetForm()
}

// 删除成绩
const handleDeleteScore = (scoreId: number) => {
  ElMessageBox.confirm('确定要删除这条成绩吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    scores.value = scores.value.filter(score => score.id !== scoreId)
    ElMessage.success('成绩已删除')
    // 重新渲染图表
    updateCharts()
  }).catch(() => {
    // 取消删除
  })
}

// 初始化成绩分布图表
const initScoreDistributionChart = () => {
  if (!scoreDistributionChartRef.value) return
  scoreDistributionChart = echarts.init(scoreDistributionChartRef.value)
  
  const option = {
    title: {
      text: '成绩分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['优秀', '良好', '中等', '及格', '不及格']
    },
    series: [
      {
        name: '成绩分布',
        type: 'pie',
        radius: '70%',
        center: ['50%', '60%'],
        data: [
          { value: scores.value.filter(score => score.score >= 85).length, name: '优秀', itemStyle: { color: '#67c23a' } },
          { value: scores.value.filter(score => score.score >= 75 && score.score < 85).length, name: '良好', itemStyle: { color: '#409eff' } },
          { value: scores.value.filter(score => score.score >= 65 && score.score < 75).length, name: '中等', itemStyle: { color: '#e6a23c' } },
          { value: scores.value.filter(score => score.score >= 60 && score.score < 65).length, name: '及格', itemStyle: { color: '#909399' } },
          { value: scores.value.filter(score => score.score < 60).length, name: '不及格', itemStyle: { color: '#f56c6c' } }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  scoreDistributionChart.setOption(option)
}

// 初始化成绩趋势图表
const initScoreTrendChart = () => {
  if (!scoreTrendChartRef.value) return
  scoreTrendChart = echarts.init(scoreTrendChartRef.value)
  
  const option = {
    title: {
      text: '成绩趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: scores.value.map(score => score.studentName),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '成绩',
      min: 0,
      max: 100,
      interval: 20
    },
    series: [
      {
        name: '成绩',
        type: 'bar',
        data: scores.value.map(score => score.score),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#1e3a8a' },
            { offset: 1, color: '#3b82f6' }
          ])
        },
        label: {
          show: true,
          position: 'top'
        }
      }
    ]
  }
  
  scoreTrendChart.setOption(option)
}

// 更新图表
const updateCharts = () => {
  if (scoreDistributionChart) {
    scoreDistributionChart.setOption({
      series: [
        {
          data: [
            { value: scores.value.filter(score => score.score >= 85).length, name: '优秀', itemStyle: { color: '#67c23a' } },
            { value: scores.value.filter(score => score.score >= 75 && score.score < 85).length, name: '良好', itemStyle: { color: '#409eff' } },
            { value: scores.value.filter(score => score.score >= 65 && score.score < 75).length, name: '中等', itemStyle: { color: '#e6a23c' } },
            { value: scores.value.filter(score => score.score >= 60 && score.score < 65).length, name: '及格', itemStyle: { color: '#909399' } },
            { value: scores.value.filter(score => score.score < 60).length, name: '不及格', itemStyle: { color: '#f56c6c' } }
          ]
        }
      ]
    })
  }
  
  if (scoreTrendChart) {
    scoreTrendChart.setOption({
      xAxis: {
        data: scores.value.map(score => score.studentName)
      },
      series: [
        {
          data: scores.value.map(score => score.score)
        }
      ]
    })
  }
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
  // 这里可以添加返回上一页的逻辑
}

// 组件挂载时初始化图表
onMounted(() => {
  initScoreDistributionChart()
  initScoreTrendChart()
  
  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    scoreDistributionChart?.resize()
    scoreTrendChart?.resize()
  })
})
</script>

<style scoped>
.score-manage-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr 600px;
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

/* 图表容器 */
.chart-container {
  height: 300px;
  width: 100%;
}

/* 成绩统计 */
.score-stats {
  padding: 20px 0;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background-color: #f8fafc;
  border-radius: 4px;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #1e3a8a;
}

/* 成绩表单 */
.score-form-card {
  margin-bottom: 20px;
}

/* 成绩列表 */
.score-list-card {
  margin-bottom: 20px;
}

.score-search {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

/* 卡片样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>