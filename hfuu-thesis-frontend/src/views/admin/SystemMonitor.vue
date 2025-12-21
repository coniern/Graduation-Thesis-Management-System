<template>
  <div class="system-monitor-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="系统监控"
    />

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 第一行：全局数据仪表盘 -->
      <div class="dashboard-row">
        <el-card class="dashboard-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>全局数据统计</span>
              <el-dropdown @command="handleRefreshCommand">
                <el-button size="small" type="primary">
                  刷新<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="refresh">立即刷新</el-dropdown-item>
                    <el-dropdown-item command="auto-refresh">自动刷新</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>

          <div class="stats-grid">
            <el-col :span="6" v-for="stat in globalStats" :key="stat.id">
              <div class="stat-item">
                <div class="stat-icon" :style="{ backgroundColor: stat.color + '20' }">
                  <el-icon :size="32" :style="{ color: stat.color }">{{ stat.icon }}</el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ stat.value }}</div>
                  <div class="stat-label">{{ stat.label }}</div>
                  <div class="stat-trend" :class="stat.trend > 0 ? 'trend-up' : 'trend-down'">
                    <el-icon>{{ stat.trend > 0 ? 'CaretTop' : 'CaretBottom' }}</el-icon>
                    {{ Math.abs(stat.trend) }}%
                  </div>
                </div>
              </div>
            </el-col>
          </div>
        </el-card>
      </div>

      <!-- 第二行：院系进度热力图和系统健康状态 -->
      <div class="second-row">
        <!-- 左侧：院系进度热力图 -->
        <el-card class="heatmap-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>各院系进度热力图</span>
            </div>
          </template>

          <div class="heatmap-container">
            <div ref="heatmapChartRef" class="heatmap-chart"></div>
            <div class="heatmap-legend">
              <div class="legend-title">完成率</div>
              <div class="legend-items">
                <div v-for="item in heatmapLegend" :key="item.id" class="legend-item">
                  <div class="legend-color" :style="{ backgroundColor: item.color }"></div>
                  <div class="legend-text">{{ item.text }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 右侧：系统健康状态 -->
        <el-card class="system-health-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>系统健康状态</span>
            </div>
          </template>

          <div class="health-stats">
            <!-- 系统状态概览 -->
            <div class="system-overview">
              <div class="status-indicator">
                <div class="status-dot" :class="systemHealth.overallStatus === '健康' ? 'status-healthy' : 'status-warning'"></div>
                <span class="status-text">{{ systemHealth.overallStatus }}</span>
              </div>
              <div class="uptime">
                <el-icon><Clock /></el-icon>
                <span>运行时间：{{ systemHealth.uptime }}</span>
              </div>
            </div>

            <!-- 健康指标 -->
            <div class="health-metrics">
              <div v-for="metric in systemHealth.metrics" :key="metric.name" class="health-metric">
                <div class="metric-header">
                  <span class="metric-name">{{ metric.name }}</span>
                  <span class="metric-value">{{ metric.value }}</span>
                </div>
                <el-progress
                  :percentage="metric.percentage"
                  :stroke-width="8"
                  :color="getHealthColor(metric.percentage)"
                  :show-text="false"
                />
              </div>
            </div>

            <!-- 服务状态 -->
            <div class="service-status">
              <h4>服务状态</h4>
              <div class="services-list">
                <div v-for="service in systemHealth.services" :key="service.name" class="service-item">
                  <div class="service-name">{{ service.name }}</div>
                  <div class="service-status-indicator">
                    <el-tag :type="service.status === '运行中' ? 'success' : 'danger'">
                      {{ service.status }}
                    </el-tag>
                    <span class="service-response-time">{{ service.responseTime }}ms</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 第三行：实时监控和趋势图表 -->
      <div class="third-row">
        <!-- 左侧：实时监控 -->
        <el-card class="realtime-monitor-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>实时监控</span>
              <el-select v-model="realtimeMonitorType" size="small" @change="handleMonitorTypeChange">
                <el-option label="请求量" value="requests" />
                <el-option label="响应时间" value="response-time" />
                <el-option label="错误率" value="error-rate" />
              </el-select>
            </div>
          </template>

          <div ref="realtimeChartRef" class="realtime-chart"></div>
        </el-card>

        <!-- 右侧：趋势图表 -->
        <el-card class="trend-chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>趋势图表</span>
              <el-select v-model="trendChartType" size="small" @change="handleTrendTypeChange">
                <el-option label="学生人数" value="students" />
                <el-option label="论文完成数" value="completed" />
                <el-option label="查重通过率" value="similarity" />
              </el-select>
            </div>
          </template>

          <div ref="trendChartRef" class="trend-chart"></div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { 
  UserFilled, DocumentChecked, Message, TrendingUp, 
  School, Clock, Refresh, ArrowDown, CaretTop, CaretBottom 
} from '@element-plus/icons-vue'

// 全局数据统计
const globalStats = ref([
  { id: 1, icon: 'UserFilled', label: '总学生数', value: 1200, trend: 5.2, color: '#1e3a8a' },
  { id: 2, icon: 'UserFilled', label: '总导师数', value: 85, trend: 2.4, color: '#3b82f6' },
  { id: 3, icon: 'DocumentChecked', label: '已完成论文', value: 980, trend: 8.7, color: '#67c23a' },
  { id: 4, icon: 'DocumentChecked', label: '待审阅文档', value: 125, trend: -3.1, color: '#e6a23c' },
  { id: 5, icon: 'Message', label: '系统消息', value: 234, trend: 12.3, color: '#f56c6c' },
  { id: 6, icon: 'TrendingUp', label: '查重通过率', value: '89.2%', trend: 4.5, color: '#909399' }
])

// 图表引用
const heatmapChartRef = ref<HTMLElement>()
const realtimeChartRef = ref<HTMLElement>()
const trendChartRef = ref<HTMLElement>()
let heatmapChart: echarts.ECharts | null = null
let realtimeChart: echarts.ECharts | null = null
let trendChart: echarts.ECharts | null = null

// 热力图数据
const heatmapData = [
  ['金融学院', '选题阶段', 85],
  ['金融学院', '开题阶段', 78],
  ['金融学院', '中期阶段', 65],
  ['金融学院', '答辩阶段', 52],
  ['金融学院', '归档阶段', 38],
  ['经济学院', '选题阶段', 92],
  ['经济学院', '开题阶段', 85],
  ['经济学院', '中期阶段', 72],
  ['经济学院', '答辩阶段', 60],
  ['经济学院', '归档阶段', 45],
  ['管理学院', '选题阶段', 78],
  ['管理学院', '开题阶段', 70],
  ['管理学院', '中期阶段', 58],
  ['管理学院', '答辩阶段', 45],
  ['管理学院', '归档阶段', 32],
  ['会计学院', '选题阶段', 88],
  ['会计学院', '开题阶段', 80],
  ['会计学院', '中期阶段', 68],
  ['会计学院', '答辩阶段', 55],
  ['会计学院', '归档阶段', 42],
  ['统计学院', '选题阶段', 90],
  ['统计学院', '开题阶段', 82],
  ['统计学院', '中期阶段', 70],
  ['统计学院', '答辩阶段', 58],
  ['统计学院', '归档阶段', 45]
]

// 热力图图例
const heatmapLegend = [
  { id: 1, color: '#1e3a8a', text: '>80%' },
  { id: 2, color: '#3b82f6', text: '60%-80%' },
  { id: 3, color: '#67c23a', text: '40%-60%' },
  { id: 4, color: '#e6a23c', text: '20%-40%' },
  { id: 5, color: '#f56c6c', text: '<20%' }
]

// 系统健康状态
const systemHealth = ref({
  overallStatus: '健康',
  uptime: '128天 15小时 32分钟',
  metrics: [
    { name: 'CPU使用率', value: '23%', percentage: 23 },
    { name: '内存使用率', value: '45%', percentage: 45 },
    { name: '磁盘使用率', value: '68%', percentage: 68 },
    { name: '数据库连接', value: '125/200', percentage: 62.5 },
    { name: 'API响应时间', value: '128ms', percentage: 25.6 }
  ],
  services: [
    { name: 'Web服务', status: '运行中', responseTime: 128 },
    { name: '数据库服务', status: '运行中', responseTime: 15 },
    { name: 'Redis服务', status: '运行中', responseTime: 2 },
    { name: 'MinIO服务', status: '运行中', responseTime: 56 },
    { name: '消息队列', status: '运行中', responseTime: 8 }
  ]
})

// 实时监控类型
const realtimeMonitorType = ref('requests')
// 趋势图表类型
const trendChartType = ref('students')

// 实时监控数据
const realtimeData = ref({
  requests: Array.from({ length: 30 }, () => Math.floor(Math.random() * 50) + 10),
  'response-time': Array.from({ length: 30 }, () => Math.floor(Math.random() * 100) + 50),
  'error-rate': Array.from({ length: 30 }, () => (Math.random() * 5).toFixed(1))
})

// 趋势图表数据
const trendChartData = ref({
  students: [1050, 1100, 1150, 1200, 1250],
  completed: [750, 820, 890, 980, 1050],
  similarity: [85, 86.5, 87.8, 89.2, 90.5]
})

// 初始化热力图
const initHeatmapChart = () => {
  if (!heatmapChartRef.value) return
  heatmapChart = echarts.init(heatmapChartRef.value)
  
  const option = {
    tooltip: {
      position: 'top'
    },
    grid: {
      height: '60%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: ['选题阶段', '开题阶段', '中期阶段', '答辩阶段', '归档阶段'],
      splitArea: {
        show: true
      }
    },
    yAxis: {
      type: 'category',
      data: ['金融学院', '经济学院', '管理学院', '会计学院', '统计学院'],
      splitArea: {
        show: true
      }
    },
    visualMap: {
      min: 0,
      max: 100,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '15%',
      inRange: {
        color: ['#f56c6c', '#e6a23c', '#67c23a', '#3b82f6', '#1e3a8a']
      }
    },
    series: [
      {
        name: '完成率',
        type: 'heatmap',
        data: heatmapData,
        label: {
          show: true,
          formatter: '{c}%'
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  heatmapChart.setOption(option)
}

// 初始化实时监控图表
const initRealtimeChart = () => {
  if (!realtimeChartRef.value) return
  realtimeChart = echarts.init(realtimeChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
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
      boundaryGap: false,
      data: Array.from({ length: 30 }, (_, i) => i + 1)
    },
    yAxis: {
      type: 'value',
      name: '请求量'
    },
    series: [
      {
        name: '请求量',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#1e3a8a'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#1e3a8a' },
            { offset: 1, color: '#3b82f6' }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: realtimeData.value.requests
      }
    ]
  }
  
  realtimeChart.setOption(option)
}

// 初始化趋势图表
const initTrendChart = () => {
  if (!trendChartRef.value) return
  trendChart = echarts.init(trendChartRef.value)
  
  const option = {
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
      data: ['2021', '2022', '2023', '2024', '2025']
    },
    yAxis: {
      type: 'value',
      name: '学生人数'
    },
    series: [
      {
        name: '学生人数',
        type: 'bar',
        data: trendChartData.value.students,
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
  
  trendChart.setOption(option)
}

// 更新实时监控图表
const updateRealtimeChart = () => {
  if (!realtimeChart) return
  
  let yAxisName = ''
  if (realtimeMonitorType.value === 'requests') {
    yAxisName = '请求量'
  } else if (realtimeMonitorType.value === 'response-time') {
    yAxisName = '响应时间(ms)'
  } else {
    yAxisName = '错误率(%)'
  }
  
  realtimeChart.setOption({
    yAxis: {
      name: yAxisName
    },
    series: [
      {
        name: yAxisName,
        data: realtimeData.value[realtimeMonitorType.value]
      }
    ]
  })
}

// 更新趋势图表
const updateTrendChart = () => {
  if (!trendChart) return
  
  let yAxisName = ''
  if (trendChartType.value === 'students') {
    yAxisName = '学生人数'
  } else if (trendChartType.value === 'completed') {
    yAxisName = '论文完成数'
  } else {
    yAxisName = '查重通过率(%)'
  }
  
  trendChart.setOption({
    yAxis: {
      name: yAxisName
    },
    series: [
      {
        name: yAxisName,
        data: trendChartData.value[trendChartType.value]
      }
    ]
  })
}

// 监听实时监控类型变化
watch(realtimeMonitorType, () => {
  updateRealtimeChart()
})

// 监听趋势图表类型变化
watch(trendChartType, () => {
  updateTrendChart()
})

// 获取健康状态颜色
const getHealthColor = (percentage: number) => {
  if (percentage < 60) return '#67c23a'
  if (percentage < 80) return '#e6a23c'
  return '#f56c6c'
}

// 处理刷新命令
const handleRefreshCommand = (command: string) => {
  if (command === 'refresh') {
    // 模拟刷新数据
    ElMessage.success('数据已刷新')
  } else if (command === 'auto-refresh') {
    ElMessage.info('自动刷新功能开发中')
  }
}

// 处理实时监控类型变化
const handleMonitorTypeChange = () => {
  console.log('实时监控类型变化', realtimeMonitorType.value)
}

// 处理趋势图表类型变化
const handleTrendTypeChange = () => {
  console.log('趋势图表类型变化', trendChartType.value)
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
  // 这里可以添加返回上一页的逻辑
}

// 组件挂载时初始化图表
onMounted(() => {
  initHeatmapChart()
  initRealtimeChart()
  initTrendChart()
  
  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    heatmapChart?.resize()
    realtimeChart?.resize()
    trendChart?.resize()
  })
  
  // 模拟实时数据更新
  setInterval(() => {
    // 更新实时监控数据
    for (const key in realtimeData.value) {
      realtimeData.value[key as keyof typeof realtimeData.value].shift()
      if (key === 'requests') {
        realtimeData.value[key as keyof typeof realtimeData.value].push(Math.floor(Math.random() * 50) + 10)
      } else if (key === 'response-time') {
        realtimeData.value[key as keyof typeof realtimeData.value].push(Math.floor(Math.random() * 100) + 50)
      } else {
        realtimeData.value[key as keyof typeof realtimeData.value].push((Math.random() * 5).toFixed(1))
      }
    }
    updateRealtimeChart()
  }, 2000)
})
</script>

<style scoped>
.system-monitor-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 全局数据仪表盘 */
.dashboard-row {
  margin-bottom: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
  padding: 20px 0;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #1e3a8a;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 4px;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.trend-up {
  color: #67c23a;
}

.trend-down {
  color: #f56c6c;
}

/* 第二行 */
.second-row {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 20px;
  margin-bottom: 20px;
}

@media (max-width: 1200px) {
  .second-row {
    grid-template-columns: 1fr;
  }
}

/* 热力图卡片 */
.heatmap-card {
  flex: 1;
}

.heatmap-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.heatmap-chart {
  height: 400px;
  width: 100%;
}

.heatmap-legend {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 20px;
}

.legend-title {
  font-weight: bold;
  color: #1e3a8a;
}

.legend-items {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.legend-color {
  width: 24px;
  height: 24px;
  border-radius: 4px;
}

/* 系统健康卡片 */
.system-health-card {
  flex-shrink: 0;
}

.health-stats {
  padding: 20px 0;
}

.system-overview {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 16px;
  background-color: #f8fafc;
  border-radius: 8px;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.status-healthy {
  background-color: #67c23a;
  animation: pulse 2s infinite;
}

.status-warning {
  background-color: #f56c6c;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}

.status-text {
  font-weight: bold;
  color: #1e3a8a;
}

.uptime {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.health-metrics {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.health-metric {
  padding: 0 16px;
}

.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.metric-name {
  font-size: 14px;
  color: #64748b;
}

.metric-value {
  font-size: 16px;
  font-weight: bold;
  color: #1e3a8a;
}

.service-status {
  padding: 16px;
  background-color: #f8fafc;
  border-radius: 8px;
}

.service-status h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  color: #1e3a8a;
}

.services-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.service-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #e2e8f0;
}

.service-item:last-child {
  border-bottom: none;
}

.service-name {
  font-size: 14px;
  color: #334155;
}

.service-status-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
}

.service-response-time {
  font-size: 12px;
  color: #64748b;
}

/* 第三行 */
.third-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 1200px) {
  .third-row {
    grid-template-columns: 1fr;
  }
}

/* 图表容器 */
.heatmap-chart,
.realtime-chart,
.trend-chart {
  height: 300px;
  width: 100%;
}

/* 卡片样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>