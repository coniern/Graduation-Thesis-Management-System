<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 评价列表
const evaluations = ref([
  { id: 1, type: '开题报告', score: 85, teacher: '张三', comment: '开题报告结构清晰，研究内容明确，建议进一步完善研究方法部分', date: '2024-03-15' },
  { id: 2, type: '中期报告', score: 88, teacher: '张三', comment: '中期报告完成情况良好，研究进展符合预期，继续保持', date: '2024-04-20' },
  { id: 3, type: '论文终稿', score: 90, teacher: '张三', comment: '论文整体质量较高，研究结论有一定的创新性和实用价值', date: '2024-05-25' },
  { id: 4, type: '论文答辩', score: 92, teacher: '答辩委员会', comment: '答辩表现优秀，对问题回答准确，逻辑清晰', date: '2024-06-10' }
])
</script>

<template>
  <div class="evaluation-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>评价查看</h1>
      <p>查看您的毕业论文评价</p>
    </div>
    
    <!-- 评价列表 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">评价列表</h3>
        </div>
      </template>
      
      <el-table :data="evaluations" style="width: 100%" border>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="type" label="评价类型" width="120" align="center" />
        <el-table-column prop="score" label="分数" width="100" align="center">
          <template #default="scope">
            <div class="score-display">
              <span class="score-value">{{ scope.row.score }}</span>
              <span class="score-label">分</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="teacher" label="评价人" width="120" align="center" />
        <el-table-column prop="date" label="评价时间" width="120" align="center" />
        <el-table-column prop="comment" label="评价内容" min-width="300" />
      </el-table>
    </el-card>
    
    <!-- 成绩统计 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">成绩统计</h3>
        </div>
      </template>
      
      <div class="stats-section">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="12" :md="12" :lg="6">
            <div class="stat-card">
              <div class="stat-content">
                <span class="stat-label">平均成绩</span>
                <h3 class="stat-value">{{ (evaluations.reduce((sum, item) => sum + item.score, 0) / evaluations.length).toFixed(1) }}</h3>
              </div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="12" :md="12" :lg="6">
            <div class="stat-card">
              <div class="stat-content">
                <span class="stat-label">最高成绩</span>
                <h3 class="stat-value">{{ Math.max(...evaluations.map(item => item.score)) }}</h3>
              </div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="12" :md="12" :lg="6">
            <div class="stat-card">
              <div class="stat-content">
                <span class="stat-label">最低成绩</span>
                <h3 class="stat-value">{{ Math.min(...evaluations.map(item => item.score)) }}</h3>
              </div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="12" :md="12" :lg="6">
            <div class="stat-card">
              <div class="stat-content">
                <span class="stat-label">评价次数</span>
                <h3 class="stat-value">{{ evaluations.length }}</h3>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 成绩分布图表 -->
      <div class="chart-section" style="margin-top: 20px;">
        <h4>成绩分布</h4>
        <div class="chart-placeholder">
          <el-empty description="成绩分布图表" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.evaluation-container {
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
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.score-display {
  display: flex;
  align-items: baseline;
  justify-content: center;
}

.score-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.score-label {
  font-size: 14px;
  color: #909399;
  margin-left: 5px;
}

.stats-section {
  padding: 10px 0;
}

.stat-card {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  margin: 0;
  font-size: 32px;
  font-weight: 600;
  color: #303133;
}

.chart-section {
  margin-top: 20px;
}

.chart-section h4 {
  margin: 0 0 15px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f8f9fa;
  border-radius: 8px;
}
</style>