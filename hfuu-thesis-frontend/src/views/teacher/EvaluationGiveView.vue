<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 评价列表
const evaluations = ref([
  { id: 1, studentName: '张三', studentId: '20210001', topic: '金融科技对传统银行业务的影响研究', evaluationType: '中期报告', status: '待评价', score: '', comment: '' },
  { id: 2, studentName: '李四', studentId: '20210002', topic: '区块链技术在金融领域的应用前景', evaluationType: '开题报告', status: '待评价', score: '', comment: '' },
  { id: 3, studentName: '王五', studentId: '20210003', topic: '普惠金融发展现状及对策研究', evaluationType: '论文终稿', status: '已评价', score: '90', comment: '论文整体质量较高，研究结论有一定的创新性和实用价值' },
  { id: 4, studentName: '赵六', studentId: '20210004', topic: '互联网金融风险监管研究', evaluationType: '中期报告', status: '已评价', score: '85', comment: '中期报告完成情况良好，研究进展符合预期，继续保持' }
])

// 保存评价
const saveEvaluation = (evaluation) => {
  // 模拟保存评价
  evaluation.status = '已评价'
  if (!evaluation.score) {
    evaluation.score = '80' // 默认分数
  }
  if (!evaluation.comment) {
    evaluation.comment = '评价良好'
  }
}

// 状态映射
const statusMap = {
  '待评价': 'warning',
  '已评价': 'success'
}
</script>

<template>
  <div class="evaluation-give-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>评价给出</h1>
      <p>对学生进行评价</p>
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
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center" />
        <el-table-column prop="studentId" label="学号" width="120" align="center" />
        <el-table-column prop="evaluationType" label="评价类型" width="120" align="center" />
        <el-table-column prop="topic" label="选题" min-width="200" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status]">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="分数" width="120" align="center">
          <template #default="scope">
            <el-input-number
              v-if="scope.row.status === '待评价'"
              v-model="scope.row.score"
              :min="0"
              :max="100"
              :precision="0"
              size="small"
            />
            <span v-else>{{ scope.row.score }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评价内容" min-width="200">
          <template #default="scope">
            <el-input
              v-if="scope.row.status === '待评价'"
              v-model="scope.row.comment"
              type="textarea"
              :rows="3"
              placeholder="请输入评价内容"
            />
            <span v-else>{{ scope.row.comment }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === '待评价'"
              type="success"
              size="small"
              @click="saveEvaluation(scope.row)"
            >
              保存
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.evaluation-give-container {
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
</style>