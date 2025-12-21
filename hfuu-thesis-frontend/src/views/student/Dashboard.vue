<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="毕业论文管理系统"
    />

    <!-- 整体进度条 -->
    <el-card class="progress-card">
      <template #header>
        <div class="card-header">
          <span>论文整体进度</span>
          <el-tag type="primary" size="small">{{ progressPhase }}</el-tag>
        </div>
      </template>
      <div class="progress-content">
        <el-progress
          :percentage="overallProgress"
          :color="progressColor"
          :stroke-width="20"
          :show-text="false"
        />
        <div class="progress-text">
          <span class="progress-percentage">{{ overallProgress }}%</span>
          <span class="progress-desc">已完成{{ completedSteps }}/5个阶段</span>
        </div>
        <!-- 各阶段进度点 -->
        <div class="progress-steps">
          <div
            v-for="(step, index) in progressSteps"
            :key="step.id"
            class="step-item"
            :class="{ 'active': index <= currentStep, 'completed': index < currentStep }"
          >
            <div class="step-circle">{{ step.id }}</div>
            <div class="step-label">{{ step.name }}</div>
            <div class="step-dot" v-if="index < progressSteps.length - 1"></div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 数据看板 -->
    <div class="dashboard-grid">
      <!-- 待办事项 -->
      <el-card class="dashboard-card todo-card">
        <template #header>
          <div class="card-header">
            <span>待办事项</span>
            <el-badge :value="todoList.length" type="danger" />
          </div>
        </template>
        <el-empty v-if="todoList.length === 0" description="暂无待办事项" />
        <el-list v-else>
          <el-list-item
            v-for="item in todoList"
            :key="item.id"
            :class="{ 'urgent': item.urgent }"
          >
            <template #title>
              <span>{{ item.title }}</span>
              <el-tag :type="item.urgent ? 'danger' : 'warning'" size="small">
                {{ item.urgent ? '紧急' : '一般' }}
              </el-tag>
            </template>
            <template #extra>
              <el-button
                type="primary"
                size="small"
                @click="handleTodoComplete(item.id)"
              >
                完成
              </el-button>
            </template>
          </el-list-item>
        </el-list>
      </el-card>

      <!-- 导师消息 -->
      <el-card class="dashboard-card message-card">
        <template #header>
          <div class="card-header">
            <span>导师消息</span>
            <el-badge :value="unreadMessages" type="primary" />
          </div>
        </template>
        <el-empty v-if="messageList.length === 0" description="暂无消息" />
        <el-list v-else>
          <el-list-item
            v-for="item in messageList"
            :key="item.id"
            :class="{ 'unread': !item.read }"
          >
            <template #avatar>
              <el-avatar :size="40" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            </template>
            <template #title>
              <div class="message-title">
                <span>{{ item.title }}</span>
                <span class="message-time">{{ formatTime(item.time) }}</span>
              </div>
            </template>
            <template #default>
              <div class="message-content">{{ item.content }}</div>
            </template>
          </el-list-item>
        </el-list>
      </el-card>

      <!-- 截止提醒 -->
      <el-card class="dashboard-card deadline-card">
        <template #header>
          <div class="card-header">
            <span>截止提醒</span>
          </div>
        </template>
        <el-empty v-if="deadlineList.length === 0" description="暂无截止提醒" />
        <el-timeline v-else>
          <el-timeline-item
            v-for="item in deadlineList"
            :key="item.id"
            :timestamp="formatDate(item.deadline)"
            :color="getDeadlineColor(item.deadline)"
          >
            <el-card :bordered="false" shadow="hover">
              <div class="deadline-item">
                <h4>{{ item.title }}</h4>
                <p class="deadline-desc">{{ item.description }}</p>
                <div class="deadline-countdown" :class="{ 'urgent': getDeadlineDays(item.deadline) <= 3 }">
                  距离截止还有{{ getDeadlineDays(item.deadline) }}天
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <!-- 金融特色图表 -->
    <div class="charts-grid">
      <!-- 金融数据趋势图 -->
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>金融数据趋势分析</span>
          </div>
        </template>
        <div ref="financialChartRef" class="chart-container"></div>
      </el-card>

      <!-- 论文主题分布 -->
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>论文主题分布</span>
          </div>
        </template>
        <div ref="topicChartRef" class="chart-container"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'

// 页面返回处理
const handleBack = () => {
  console.log('返回')
}

// 整体进度
const overallProgress = ref(60)
const completedSteps = ref(3)
const currentStep = ref(2)

// 进度阶段文本
const progressPhase = computed(() => {
  const phases = ['选题阶段', '开题阶段', '中期阶段', '答辩阶段', '归档阶段']
  return phases[currentStep.value] || '未知阶段'
})

// 进度条颜色
const progressColor = computed(() => {
  if (overallProgress.value < 30) return '#ef4444'
  if (overallProgress.value < 70) return '#f59e0b'
  return '#10b981'
})

// 各阶段进度点
const progressSteps = [
  { id: 1, name: '选题' },
  { id: 2, name: '开题' },
  { id: 3, name: '中期' },
  { id: 4, name: '答辩' },
  { id: 5, name: '归档' }
]

// 待办事项列表
const todoList = ref([
  { id: 1, title: '上传开题报告', urgent: true },
  { id: 2, title: '填写中期检查表', urgent: false },
  { id: 3, title: '准备答辩PPT', urgent: false }
])

// 处理待办事项完成
const handleTodoComplete = (id: number) => {
  todoList.value = todoList.value.filter(item => item.id !== id)
}

// 消息列表
const messageList = ref([
  { id: 1, title: '开题报告已审阅', content: '你的开题报告已审阅，请查看批注', time: '2025-12-20 14:30', read: false },
  { id: 2, title: '中期检查提醒', content: '请于本周内完成中期检查', time: '2025-12-19 09:00', read: true }
])

// 未读消息数
const unreadMessages = computed(() => {
  return messageList.value.filter(item => !item.read).length
})

// 格式化时间
const formatTime = (time: string) => {
  return new Date(time).toLocaleString()
}

// 截止提醒列表
const deadlineList = ref([
  { id: 1, title: '开题报告提交', description: '提交开题报告初稿', deadline: '2025-12-25' },
  { id: 2, title: '中期检查', description: '完成中期检查并提交报告', deadline: '2026-03-15' },
  { id: 3, title: '论文终稿提交', description: '提交论文终稿', deadline: '2026-05-10' }
])

// 格式化日期
const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString()
}

// 获取截止日期颜色
const getDeadlineColor = (deadline: string) => {
  const days = getDeadlineDays(deadline)
  if (days <= 3) return '#ef4444'
  if (days <= 7) return '#f59e0b'
  return '#10b981'
}

// 获取距离截止日期天数
const getDeadlineDays = (deadline: string) => {
  const now = new Date()
  const target = new Date(deadline)
  const diffTime = target.getTime() - now.getTime()
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
}

// 金融数据趋势图
const financialChartRef = ref<HTMLElement>()
let financialChart: echarts.ECharts | null = null

// 论文主题分布图
const topicChartRef = ref<HTMLElement>()
let topicChart: echarts.ECharts | null = null

// 初始化金融数据趋势图
const initFinancialChart = () => {
  if (!financialChartRef.value) return
  financialChart = echarts.init(financialChartRef.value)
  
  const option = {
    title: {
      text: '金融数据趋势分析',
      left: 'center',
      textStyle: {
        fontSize: 14,
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>{a0}: {c0}%<br/>{a1}: {c1}元'
    },
    legend: {
      data: ['收益率', '成交量'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: [
      {
        type: 'value',
        name: '收益率(%)',
        axisLabel: {
          formatter: '{value}%'
        }
      },
      {
        type: 'value',
        name: '成交量(万元)',
        axisLabel: {
          formatter: '{value}万'
        }
      }
    ],
    series: [
      {
        name: '收益率',
        type: 'line',
        stack: 'Total',
        data: [6.7, 7.2, 6.9, 8.1, 7.8, 8.5, 9.2, 8.8, 9.5, 10.2, 9.8, 10.5],
        smooth: true,
        itemStyle: {
          color: '#1e3a8a'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(30, 58, 138, 0.3)' },
            { offset: 1, color: 'rgba(30, 58, 138, 0.1)' }
          ])
        }
      },
      {
        name: '成交量',
        type: 'bar',
        yAxisIndex: 1,
        data: [230, 340, 270, 420, 380, 450, 510, 480, 550, 620, 580, 650],
        itemStyle: {
          color: '#3b82f6'
        }
      }
    ]
  }
  
  financialChart.setOption(option)
}

// 初始化论文主题分布图
const initTopicChart = () => {
  if (!topicChartRef.value) return
  topicChart = echarts.init(topicChartRef.value)
  
  const option = {
    title: {
      text: '论文主题分布',
      left: 'center',
      textStyle: {
        fontSize: 14,
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: ['金融市场', '金融风险', '金融科技', '国际金融', '公司金融', '其他']
    },
    series: [
      {
        name: '主题分布',
        type: 'pie',
        radius: ['40%', '70%'],
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
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 25, name: '金融市场', itemStyle: { color: '#1e3a8a' } },
          { value: 20, name: '金融风险', itemStyle: { color: '#3b82f6' } },
          { value: 18, name: '金融科技', itemStyle: { color: '#60a5fa' } },
          { value: 15, name: '国际金融', itemStyle: { color: '#93c5fd' } },
          { value: 12, name: '公司金融', itemStyle: { color: '#bfdbfe' } },
          { value: 10, name: '其他', itemStyle: { color: '#dbeafe' } }
        ]
      }
    ]
  }
  
  topicChart.setOption(option)
}

// 组件挂载时初始化图表
onMounted(() => {
  initFinancialChart()
  initTopicChart()
  
  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    financialChart?.resize()
    topicChart?.resize()
  })
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.progress-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.progress-content {
  position: relative;
  padding: 20px 0;
}

.progress-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.progress-percentage {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.progress-desc {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.progress-steps {
  display: flex;
  justify-content: space-around;
  margin-top: 30px;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.step-circle {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #e5e7eb;
  color: #6b7280;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  transition: all 0.3s ease;
}

.step-item.active .step-circle {
  background-color: #3b82f6;
  color: white;
}

.step-item.completed .step-circle {
  background-color: #10b981;
  color: white;
}

.step-label {
  margin-top: 8px;
  font-size: 12px;
  color: #6b7280;
}

.step-item.active .step-label,
.step-item.completed .step-label {
  color: #374151;
  font-weight: 500;
}

.step-dot {
  position: absolute;
  top: 15px;
  left: 50%;
  width: calc(100% - 30px);
  height: 2px;
  background-color: #e5e7eb;
  z-index: -1;
}

.step-item.completed ~ .step-item .step-dot {
  background-color: #10b981;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.dashboard-card {
  height: 300px;
  overflow-y: auto;
}

.todo-card .el-list-item.urgent {
  border-left: 4px solid #ef4444;
}

.message-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-time {
  font-size: 12px;
  color: #9ca3af;
}

.message-content {
  font-size: 13px;
  color: #6b7280;
  margin-top: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.el-list-item.unread {
  background-color: #eff6ff;
}

.deadline-countdown {
  margin-top: 8px;
  font-size: 12px;
  color: #6b7280;
}

.deadline-countdown.urgent {
  color: #ef4444;
  font-weight: bold;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  width: 100%;
  height: 350px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-container {
    height: 300px;
  }
}
</style>