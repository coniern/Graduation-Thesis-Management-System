<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 进度列表
const progress = ref([
  { id: 1, stage: '选题申请', status: '已完成', date: '2024-03-01', description: '成功申请选题' },
  { id: 2, stage: '开题报告', status: '已完成', date: '2024-03-15', description: '开题报告已通过' },
  { id: 3, stage: '中期检查', status: '已完成', date: '2024-04-20', description: '中期检查通过' },
  { id: 4, stage: '论文撰写', status: '进行中', date: '', description: '正在撰写论文' },
  { id: 5, stage: '论文答辩', status: '未开始', date: '', description: '' },
  { id: 6, stage: '论文归档', status: '未开始', date: '', description: '' }
])

// 状态映射
const statusMap = {
  '已完成': { type: 'success', text: '已完成' },
  '进行中': { type: 'warning', text: '进行中' },
  '未开始': { type: 'info', text: '未开始' }
}
</script>

<template>
  <div class="progress-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>进度查看</h1>
      <p>查看您的毕业论文进度</p>
    </div>
    
    <!-- 进度时间线 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">论文进度时间线</h3>
        </div>
      </template>
      
      <el-timeline>
        <el-timeline-item
          v-for="item in progress"
          :key="item.id"
          :timestamp="item.date"
          :type="statusMap[item.status].type"
          placement="top"
        >
          <div class="timeline-content">
            <h4 class="stage-title">{{ item.stage }}</h4>
            <el-tag :type="statusMap[item.status].type" size="small" style="margin-right: 10px;">
              {{ statusMap[item.status].text }}
            </el-tag>
            <p v-if="item.description" class="stage-description">{{ item.description }}</p>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
    
    <!-- 进度统计 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">进度统计</h3>
        </div>
      </template>
      
      <div class="progress-stats">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="12" :md="6" :lg="6">
            <div class="stat-item">
              <div class="stat-value">{{ progress.filter(p => p.status === '已完成').length }}</div>
              <div class="stat-label">已完成阶段</div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="12" :md="6" :lg="6">
            <div class="stat-item">
              <div class="stat-value">{{ progress.filter(p => p.status === '进行中').length }}</div>
              <div class="stat-label">进行中阶段</div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="12" :md="6" :lg="6">
            <div class="stat-item">
              <div class="stat-value">{{ progress.filter(p => p.status === '未开始').length }}</div>
              <div class="stat-label">未开始阶段</div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="12" :md="6" :lg="6">
            <div class="stat-item">
              <div class="stat-value">{{ Math.round((progress.filter(p => p.status === '已完成').length / progress.length) * 100) }}%</div>
              <div class="stat-label">总体进度</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    
    <!-- 导师反馈 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">导师反馈</h3>
        </div>
      </template>
      
      <div class="feedback-list">
        <div class="feedback-item">
          <div class="feedback-header">
            <span class="feedback-from">张三 导师</span>
            <span class="feedback-date">2024-04-20</span>
          </div>
          <div class="feedback-content">
            <p>中期报告整体不错，但需要进一步完善研究方法部分，建议增加实证分析</p>
          </div>
        </div>
        <div class="feedback-item">
          <div class="feedback-header">
            <span class="feedback-from">张三 导师</span>
            <span class="feedback-date">2024-03-16</span>
          </div>
          <div class="feedback-content">
            <p>开题报告通过，请注意论文撰写过程中的逻辑结构</p>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.progress-container {
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

.timeline-content {
  padding: 10px 0;
}

.stage-title {
  margin: 0 0 5px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.stage-description {
  margin: 5px 0 0;
  font-size: 14px;
  color: #606266;
}

.progress-stats {
  padding: 20px 0;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.feedback-list {
  padding: 10px 0;
}

.feedback-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.feedback-item:last-child {
  border-bottom: none;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.feedback-from {
  font-weight: 500;
  color: #303133;
}

.feedback-date {
  font-size: 12px;
  color: #909399;
}

.feedback-content {
  color: #606266;
  line-height: 1.6;
}
</style>