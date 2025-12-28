<script setup>
import { ref, onMounted } from 'vue'
import axios from '../../utils/axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UserFilled, CircleCheck, CircleClose, Timer, Calendar, Check, Close } from '@element-plus/icons-vue'

// 加载状态
const loading = ref(false)
// 学生选择记录
const studentSelections = ref([])
// 当前登录老师ID
const teacherId = ref(2) // 实际应用中应从登录用户信息中获取

// 初始化数据
onMounted(() => {
  fetchStudentSelections()
})

// 获取选择该导师的学生列表
const fetchStudentSelections = async () => {
  try {
    loading.value = true
    const response = await axios.get(`/api/selection/teacher/${teacherId.value}`)
    studentSelections.value = response.data || []
  } catch (error) {
    ElMessage.error('获取学生选择记录失败')
    console.error('获取学生选择记录失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取选择状态文本
const getSelectionStatusText = (status) => {
  const statusMap = { 
    1: '待确认',
    2: '已同意',
    3: '已拒绝'
  }
  return statusMap[status] || '未知状态'
}

// 导师同意学生选择
const handleApproveSelection = async (selection) => {
  try {
    await ElMessageBox.confirm(
      `确定要同意学生 ${selection.studentName} 的选择吗？`,
      '同意选择',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }
    )
    
    loading.value = true
    await axios.post('/api/selection/approve', {
      selectionId: selection.selectionId
    })
    
    ElMessage.success('同意选择成功')
    // 刷新数据
    await fetchStudentSelections()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('同意选择失败')
      console.error('同意选择失败:', error)
    }
  } finally {
    loading.value = false
  }
}

// 导师拒绝学生选择
const handleRejectSelection = async (selection) => {
  try {
    await ElMessageBox.confirm(
      `确定要拒绝学生 ${selection.studentName} 的选择吗？`,
      '拒绝选择',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    await axios.post('/api/selection/reject', {
      selectionId: selection.selectionId
    })
    
    ElMessage.success('拒绝选择成功')
    // 刷新数据
    await fetchStudentSelections()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('拒绝选择失败')
      console.error('拒绝选择失败:', error)
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="student-selection-container">
    <div class="page-header">
      <h2 class="page-title">学生选择管理</h2>
      <p class="page-subtitle">查看并管理选择您作为导师的学生</p>
    </div>
    
    <el-card class="selection-card animated-fade-in">
      <template #header>
        <div class="card-header">
          <span class="card-title">选择我的学生</span>
          <el-tag type="info" size="small">{{ studentSelections.length }} 位学生</el-tag>
        </div>
      </template>
      
      <div class="selection-list">
        <el-skeleton :rows="6" animated v-if="loading" />
        <el-empty v-else-if="studentSelections.length === 0" description="暂无学生选择记录" :image-size="120" />
        
        <div class="selection-grid" v-else>
          <div v-for="selection in studentSelections" :key="selection.selectionId" class="selection-item">
            <div class="selection-content">
              <div class="selection-header">
                <div class="selection-student-info">
                  <el-avatar :size="60" class="selection-avatar">
                    {{ selection.studentName.charAt(0) }}
                  </el-avatar>
                  <div class="selection-student-details">
                    <h4 class="selection-student-name">{{ selection.studentName }}</h4>
                    <p class="selection-student-id">学生ID: {{ selection.studentId }}</p>
                  </div>
                </div>
                <div class="selection-status">
                  <el-tag 
                    :type="selection.teacherStatus === 2 ? 'success' : (selection.teacherStatus === 3 ? 'danger' : 'warning')"
                    size="large"
                    class="status-tag"
                  >
                    <template #icon>
                      <el-icon v-if="selection.teacherStatus === 2"><CircleCheck /></el-icon>
                      <el-icon v-else-if="selection.teacherStatus === 3"><CircleClose /></el-icon>
                      <el-icon v-else><Timer /></el-icon>
                    </template>
                    {{ getSelectionStatusText(selection.teacherStatus) }}
                  </el-tag>
                </div>
              </div>
              
              <div class="selection-details">
                <div class="detail-item">
                  <el-icon class="detail-icon"><Calendar /></el-icon>
                  <span class="detail-label">选择时间:</span>
                  <span class="detail-value">{{ new Date(selection.selectionTime).toLocaleString() }}</span>
                </div>
                <div class="detail-item" v-if="selection.confirmTime">
                  <el-icon class="detail-icon"><Check /></el-icon>
                  <span class="detail-label">处理时间:</span>
                  <span class="detail-value">{{ new Date(selection.confirmTime).toLocaleString() }}</span>
                </div>
              </div>
              
              <div class="selection-actions" v-if="selection.teacherStatus === 1">
                <el-button 
                  type="success" 
                  size="large" 
                  @click="handleApproveSelection(selection)"
                  class="approve-button"
                >
                  <template #icon><Check /></template>
                  同意
                </el-button>
                <el-button 
                  type="danger" 
                  size="large" 
                  @click="handleRejectSelection(selection)"
                  class="reject-button"
                >
                  <template #icon><Close /></template>
                  拒绝
                </el-button>
              </div>
              <div class="selection-actions" v-else>
                <el-button 
                  type="info" 
                  size="large" 
                  disabled
                  class="processed-button"
                >
                  已处理
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.student-selection-container {
  padding: 30px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 10px;
  letter-spacing: -0.5px;
}

.page-subtitle {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.selection-card {
  margin-bottom: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: none;
  background: white;
  animation: fadeInUp 0.6s ease-out;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 600;
  font-size: 18px;
  color: #303133;
}

.card-title {
  font-weight: 700;
}

.selection-list {
  padding: 30px 24px;
}

.selection-grid {
  display: grid;
  gap: 24px;
}

.selection-item {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.selection-item:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-4px);
}

.selection-content {
  padding: 24px;
}

.selection-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 16px;
}

.selection-student-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.selection-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 20px;
  font-weight: 700;
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.selection-item:hover .selection-avatar {
  transform: scale(1.05);
}

.selection-student-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.selection-student-name {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  color: #303133;
}

.selection-student-id {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.status-tag {
  border-radius: 20px;
  padding: 8px 20px;
  font-size: 14px;
  font-weight: 600;
}

.selection-details {
  display: flex;
  gap: 30px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.detail-icon {
  font-size: 16px;
  color: #667eea;
  width: 20px;
  text-align: center;
}

.detail-label {
  color: #909399;
  font-weight: 500;
}

.detail-value {
  color: #303133;
  font-weight: 600;
}

.selection-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.approve-button, .reject-button, .processed-button {
  height: 44px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 8px;
  min-width: 120px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.approve-button {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
  border: none;
}

.approve-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(82, 196, 26, 0.3);
}

.reject-button {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
  border: none;
}

.reject-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 77, 79, 0.3);
}

.processed-button {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  border: none;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animated-fade-in {
  animation: fadeInUp 0.6s ease-out;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .student-selection-container {
    padding: 20px 16px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .selection-list {
    padding: 20px 16px;
  }
  
  .selection-content {
    padding: 20px 16px;
  }
  
  .selection-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .selection-details {
    flex-direction: column;
    gap: 12px;
  }
  
  .selection-actions {
    justify-content: center;
    width: 100%;
  }
  
  .approve-button, .reject-button, .processed-button {
    width: 100%;
    margin-bottom: 8px;
  }
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #68418d 100%);
}
</style>