<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from '../../utils/axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UserFilled, StarFilled, MessageFilled, Check, CircleCheck, CircleClose, Timer, Calendar, Delete } from '@element-plus/icons-vue'

// 加载状态
const loading = ref(false)
// 导师列表
const teacherList = ref([])
// 学生选择记录
const studentSelections = ref([])
// 当前登录学生ID
const studentId = ref(1) // 实际应用中应从登录用户信息中获取

// 初始化数据
onMounted(() => {
  fetchTeachers()
  fetchStudentSelections()
})

// 获取所有导师列表
const fetchTeachers = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/selection/teachers')
    teacherList.value = response.data || []
  } catch (error) {
    ElMessage.error('获取导师列表失败')
    console.error('获取导师列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取学生选择记录
const fetchStudentSelections = async () => {
  try {
    const response = await axios.get(`/api/selection/student/${studentId.value}`)
    studentSelections.value = response.data || []
  } catch (error) {
    ElMessage.error('获取选择记录失败')
    console.error('获取选择记录失败:', error)
  }
}

// 检查导师是否已被选择
const isTeacherSelected = (teacherId) => {
  return studentSelections.value.some(selection => 
    selection.teacherId === teacherId && selection.studentStatus === 1
  )
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

// 学生选择导师
const handleSelectTeacher = async (teacher) => {
  try {
    await ElMessageBox.confirm(
      `确定要选择导师 ${teacher.realName} 吗？`,
      '选择导师',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }
    )
    
    loading.value = true
    await axios.post('/api/selection/select', {
      studentId: studentId.value,
      teacherId: teacher.userId
    })
    
    ElMessage.success('选择导师成功')
    // 刷新数据
    await fetchStudentSelections()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('选择导师失败')
      console.error('选择导师失败:', error)
    }
  } finally {
    loading.value = false
  }
}

// 学生放弃选择导师
const handleGiveUpSelection = async (selection) => {
  try {
    await ElMessageBox.confirm(
      `确定要放弃选择导师 ${selection.teacherName} 吗？`,
      '放弃选择',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    await axios.post('/api/selection/give-up', {
      selectionId: selection.selectionId
    })
    
    ElMessage.success('放弃选择成功')
    // 刷新数据
    await fetchStudentSelections()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('放弃选择失败')
      console.error('放弃选择失败:', error)
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="teacher-selection-container">
    <div class="page-header">
      <h2 class="page-title">导师选择</h2>
      <p class="page-subtitle">请选择您感兴趣的导师，每位学生最多可选择3位导师</p>
    </div>
    
    <!-- 导师列表卡片 -->
    <el-card class="selection-card animated-fade-in">
      <template #header>
        <div class="card-header">
          <span class="card-title">导师列表</span>
          <el-tag type="info" size="small">{{ teacherList.length }} 位导师</el-tag>
        </div>
      </template>
      
      <div class="teacher-list">
        <el-skeleton :rows="6" animated v-if="loading" />
        <el-empty v-else-if="teacherList.length === 0" description="暂无导师数据" :image-size="120" />
        
        <el-row :gutter="30" v-else>
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="teacher in teacherList" :key="teacher.userId">
            <el-card class="teacher-card" :shadow="'hover'" @mouseenter="$el.classList.add('card-hover')" @mouseleave="$el.classList.remove('card-hover')">
              <div class="teacher-info">
                <div class="teacher-avatar-container">
                  <el-avatar :size="80" class="teacher-avatar">
                    {{ teacher.realName.charAt(0) }}
                  </el-avatar>
                  <div class="avatar-badge">
                    <el-icon><UserFilled /></el-icon>
                  </div>
                </div>
                <div class="teacher-details">
                  <h3 class="teacher-name">{{ teacher.realName }}</h3>
                  <p class="teacher-username">{{ teacher.username }}</p>
                  <div class="teacher-meta">
                    <el-tag size="small" type="primary" class="department-tag">
                      {{ teacher.department || '计算机科学系' }}
                    </el-tag>
                    <el-tag size="small" type="success" class="course-tag">
                      {{ teacher.specialty || '软件工程' }}
                    </el-tag>
                  </div>
                </div>
              </div>
              
              <div class="teacher-stats">
                <div class="stat-item">
                  <el-icon class="stat-icon"><StarFilled /></el-icon>
                  <span class="stat-value">{{ Math.floor(Math.random() * 5) + 4.5 }}<span class="stat-unit">/5</span></span>
                  <span class="stat-label">评分</span>
                </div>
                <div class="stat-item">
                  <el-icon class="stat-icon"><UserFilled /></el-icon>
                  <span class="stat-value">{{ Math.floor(Math.random() * 20) + 5 }}</span>
                  <span class="stat-label">已带学生</span>
                </div>
                <div class="stat-item">
                  <el-icon class="stat-icon"><MessageFilled /></el-icon>
                  <span class="stat-value">{{ Math.floor(Math.random() * 100) + 20 }}</span>
                  <span class="stat-label">指导次数</span>
                </div>
              </div>
              
              <div class="teacher-bio">
                <p>{{ teacher.bio || '该导师暂无简介信息，欢迎选择。' }}</p>
              </div>
              
              <div class="teacher-actions">
                <el-button 
                  type="primary" 
                  :disabled="isTeacherSelected(teacher.userId)" 
                  @click="handleSelectTeacher(teacher)"
                  :size="'large'"
                  class="select-button"
                >
                  <template #icon><Check /></template>
                  {{ isTeacherSelected(teacher.userId) ? '已选择' : '选择导师' }}
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
    
    <!-- 我的选择记录 -->
    <el-card class="selection-card animated-fade-in" style="margin-top: 30px;">
      <template #header>
        <div class="card-header">
          <span class="card-title">我的选择记录</span>
          <el-tag type="warning" size="small">{{ studentSelections.length }} 条记录</el-tag>
        </div>
      </template>
      
      <div class="selection-list">
        <el-empty v-if="studentSelections.length === 0" description="暂无选择记录" :image-size="120" />
        
        <div class="selection-grid" v-else>
          <div v-for="selection in studentSelections" :key="selection.selectionId" class="selection-item">
            <div class="selection-content">
              <div class="selection-header">
                <div class="selection-teacher-info">
                  <el-avatar :size="50" class="selection-avatar">
                    {{ selection.teacherName.charAt(0) }}
                  </el-avatar>
                  <div class="selection-teacher-details">
                    <h4 class="selection-teacher-name">{{ selection.teacherName }}</h4>
                    <p class="selection-teacher-id">导师ID: {{ selection.teacherId }}</p>
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
                  <span class="detail-label">确认时间:</span>
                  <span class="detail-value">{{ new Date(selection.confirmTime).toLocaleString() }}</span>
                </div>
                <div class="detail-item">
                  <el-icon class="detail-icon"><UserFilled /></el-icon>
                  <span class="detail-label">我的状态:</span>
                  <span class="detail-value">{{ selection.studentStatus === 1 ? '已选择' : '已放弃' }}</span>
                </div>
              </div>
              
              <div class="selection-actions">
                <el-button 
                  type="danger" 
                  size="large" 
                  @click="handleGiveUpSelection(selection)"
                  :disabled="selection.studentStatus === 2"
                  class="giveup-button"
                >
                  <template #icon><Delete /></template>
                  放弃选择
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
.teacher-selection-container {
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

.teacher-list {
  padding: 30px 24px;
}

.teacher-card {
  border-radius: 12px;
  border: 1px solid #f0f0f0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #ffffff 0%, #fafafa 100%);
}

.teacher-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

.card-hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

.teacher-info {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  border-bottom: 1px solid #f5f7fa;
}

.teacher-avatar-container {
  position: relative;
  margin-bottom: 16px;
}

.teacher-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 28px;
  font-weight: 700;
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.teacher-card:hover .teacher-avatar {
  transform: scale(1.1);
}

.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background: white;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  border: 2px solid white;
}

.teacher-name {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 8px 0;
  color: #303133;
}

.teacher-username {
  font-size: 14px;
  color: #909399;
  margin: 0 0 12px 0;
}

.teacher-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
  margin-top: 12px;
}

.department-tag, .course-tag {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 12px;
}

.teacher-stats {
  display: flex;
  justify-content: space-around;
  padding: 16px 20px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.stat-item {
  text-align: center;
}

.stat-icon {
  font-size: 18px;
  color: #667eea;
  margin-bottom: 4px;
}

.stat-value {
  display: block;
  font-size: 16px;
  font-weight: 700;
  color: #303133;
}

.stat-unit {
  font-size: 12px;
  font-weight: 400;
  color: #909399;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.teacher-bio {
  padding: 16px 20px;
  flex: 1;
}

.teacher-bio p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.teacher-actions {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.select-button {
  width: 100%;
  height: 44px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.select-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.select-button:disabled {
  background: #dcdfe6;
  color: #a0a0a0;
  transform: none;
  box-shadow: none;
}

.selection-list {
  padding: 30px 24px;
}

.selection-grid {
  display: grid;
  gap: 20px;
}

.selection-item {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
  transition: all 0.3s ease;
  overflow: hidden;
}

.selection-item:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
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

.selection-teacher-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.selection-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 18px;
  font-weight: 700;
  color: white;
}

.selection-teacher-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.selection-teacher-name {
  font-size: 18px;
  font-weight: 700;
  margin: 0;
  color: #303133;
}

.selection-teacher-id {
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
  justify-content: flex-end;
}

.giveup-button {
  height: 40px;
  font-size: 14px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.giveup-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 76, 76, 0.3);
}

.giveup-button:disabled {
  background: #f56c6c;
  opacity: 0.6;
  transform: none;
  box-shadow: none;
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
  .teacher-selection-container {
    padding: 20px 16px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .teacher-list {
    padding: 20px 16px;
  }
  
  .teacher-card {
    margin-bottom: 20px;
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