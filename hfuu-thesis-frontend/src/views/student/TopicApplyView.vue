<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../../utils/axios'

// 选题列表
const topics = ref([])
// 原始选题列表备份，用于筛选和重置
const originalTopics = ref([])

// 筛选条件
const filterForm = ref({
  keyword: '',
  teacher: '',
  major: '',
  topicType: ''
})

// 申请状态
const applyStatus = ref('')

// 我的选题志愿
const myVolunteers = ref([
  { id: 1, topicId: null, topicName: '', volunteerLevel: '第一志愿' },
  { id: 2, topicId: null, topicName: '', volunteerLevel: '第二志愿' }
])

// 自拟题目对话框
const selfTopicDialogVisible = ref(false)
const selfTopicForm = ref({
  topicName: '',
  topicDescription: '',
  topicRequirements: '',
  expectedTeacher: ''
})

// 查看选题结果对话框
const topicResultDialogVisible = ref(false)
const topicResult = ref({
  topicId: null,
  topicName: '',
  teacherName: '',
  resultStatus: '未分配',
  assignTime: ''
})

// 加载选题列表
const loadTopics = async () => {
  try {
    const response = await axios.get('/api/student/topics')
    originalTopics.value = response.data
    topics.value = [...originalTopics.value]
  } catch (error) {
    ElMessage.error('获取选题列表失败')
    console.error('获取选题列表失败:', error)
  }
}

// 筛选选题
const filterTopics = () => {
  try {
    // 当前后端API不支持筛选，先在前端实现本地筛选
    let filtered = [...originalTopics.value]
    
    // 关键词筛选
    if (filterForm.value.keyword) {
      const keyword = filterForm.value.keyword.toLowerCase()
      filtered = filtered.filter(topic => 
        topic.topicName.toLowerCase().includes(keyword) ||
        (topic.topicDescription && topic.topicDescription.toLowerCase().includes(keyword))
      )
    }
    
    // 教师筛选
    if (filterForm.value.teacher) {
      const teacher = filterForm.value.teacher.toLowerCase()
      filtered = filtered.filter(topic => 
        topic.teacherName.toLowerCase().includes(teacher)
      )
    }
    
    // 专业筛选
    if (filterForm.value.major) {
      filtered = filtered.filter(topic => 
        topic.major === filterForm.value.major
      )
    }
    
    // 题目类型筛选
    if (filterForm.value.topicType) {
      filtered = filtered.filter(topic => 
        topic.topicType === filterForm.value.topicType
      )
    }
    
    // 更新显示的选题列表
    topics.value = filtered
  } catch (error) {
    ElMessage.error('筛选选题失败')
    console.error('筛选选题失败:', error)
  }
}

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = {
    keyword: '',
    teacher: '',
    major: '',
    topicType: ''
  }
  topics.value = [...originalTopics.value]
}

// 选择选题作为志愿
const selectTopicAsVolunteer = (topic, volunteerIndex) => {
  myVolunteers.value[volunteerIndex].topicId = topic.topicId
  myVolunteers.value[volunteerIndex].topicName = topic.topicName
  ElMessage.success(`已将${topic.title}设为${myVolunteers.value[volunteerIndex].volunteerLevel}`)
}

// 提交选题志愿
const submitVolunteers = async () => {
  try {
    // 验证是否至少选择了一个志愿
    const hasVolunteer = myVolunteers.value.some(v => v.topicId)
    if (!hasVolunteer) {
      ElMessage.warning('请至少选择一个选题志愿')
      return
    }
    
    // 获取第一个选择的志愿
    const selectedVolunteer = myVolunteers.value.find(v => v.topicId)
    if (selectedVolunteer) {
      const response = await axios.post(`/api/student/select-topic/${selectedVolunteer.topicId}`)
      ElMessage.success('选题志愿提交成功')
      applyStatus.value = '选题志愿已提交，等待分配结果'
    }
  } catch (error) {
    ElMessage.error('提交选题志愿失败')
    console.error('提交选题志愿失败:', error)
  }
}

// 申请自拟题目
const applySelfTopic = async () => {
  try {
    // 验证表单
    if (!selfTopicForm.value.topicName || !selfTopicForm.value.topicDescription) {
      ElMessage.warning('请填写题目名称和描述')
      return
    }
    
    // TODO: 实现申请自拟题目的逻辑
    console.log('申请自拟题目:', selfTopicForm.value)
    selfTopicDialogVisible.value = false
    ElMessage.success('自拟题目申请已提交，等待教师审核')
  } catch (error) {
    ElMessage.error('申请自拟题目失败')
    console.error('申请自拟题目失败:', error)
  }
}

// 查看选题结果
const viewTopicResult = async () => {
  try {
    // TODO: 实现查看选题结果的逻辑
    console.log('查看选题结果')
    topicResultDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取选题结果失败')
    console.error('获取选题结果失败:', error)
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadTopics()
})
</script>

<template>
  <div class="topic-apply-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>选题管理</h1>
      <p>选择您感兴趣的毕业论文选题</p>
    </div>
    
    <!-- 提示信息 -->
    <el-alert v-if="applyStatus" :message="applyStatus" type="success" show-icon :closable="true" @close="applyStatus = ''" />
    
    <!-- 功能区 -->
    <el-row :gutter="20" class="function-row">
      <el-col :xs="24" :md="12" :lg="8" class="volunteer-section">
        <el-card class="function-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">我的选题志愿</h3>
              <el-button type="primary" size="small" @click="submitVolunteers">
                <el-icon><Check /></el-icon>
                提交志愿
              </el-button>
            </div>
          </template>
          <div class="volunteer-list">
            <div v-for="(volunteer, index) in myVolunteers" :key="volunteer.id" class="volunteer-item">
              <div class="volunteer-header">
                <span class="volunteer-level">{{ volunteer.volunteerLevel }}</span>
                <el-button 
                  v-if="volunteer.topicId" 
                  type="danger" 
                  size="small" 
                  @click="volunteer.topicId = null; volunteer.topicName = ''"
                >
                  <el-icon><Delete /></el-icon>
                  取消
                </el-button>
              </div>
              <div class="volunteer-content">
                <div v-if="volunteer.topicId" class="selected-topic">
                  <el-tag type="success">{{ volunteer.topicName }}</el-tag>
                </div>
                <div v-else class="no-topic">
                  尚未选择选题
                </div>
              </div>
            </div>
          </div>
          <div class="self-topic-section">
            <el-button type="warning" size="small" @click="selfTopicDialogVisible = true">
              <el-icon><EditPen /></el-icon>
              申请自拟题目
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12" :lg="16" class="filter-section">
        <el-card class="function-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3 class="card-title">选题筛选</h3>
              <el-button type="primary" size="small" @click="viewTopicResult">
                <el-icon><View /></el-icon>
                查看选题结果
              </el-button>
            </div>
          </template>
          <el-form :model="filterForm" label-width="80px" inline>
            <el-form-item label="关键词">
              <el-input 
                v-model="filterForm.keyword" 
                placeholder="请输入关键词" 
                style="width: 200px;"
                @keyup.enter="filterTopics"
              />
            </el-form-item>
            <el-form-item label="指导教师">
              <el-input 
                v-model="filterForm.teacher" 
                placeholder="请输入教师姓名" 
                style="width: 150px;"
                @keyup.enter="filterTopics"
              />
            </el-form-item>
            <el-form-item label="专业">
              <el-select 
                v-model="filterForm.major" 
                placeholder="请选择专业" 
                style="width: 150px;"
              >
                <el-option label="金融学" value="金融学" />
                <el-option label="金融工程" value="金融工程" />
                <el-option label="保险学" value="保险学" />
                <el-option label="投资学" value="投资学" />
              </el-select>
            </el-form-item>
            <el-form-item label="题目类型">
              <el-select 
                v-model="filterForm.topicType" 
                placeholder="请选择类型" 
                style="width: 120px;"
              >
                <el-option label="研究型" value="研究型" />
                <el-option label="技术型" value="技术型" />
                <el-option label="应用型" value="应用型" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="filterTopics">
                <el-icon><Search /></el-icon>
                搜索
              </el-button>
              <el-button @click="resetFilter">
                <el-icon><RefreshRight /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 选题列表 -->
    <el-card class="topic-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">可选选题列表</h3>
        </div>
      </template>
      
      <el-table :data="topics" style="width: 100%" border stripe>
        <el-table-column prop="topicId" label="选题编号" width="100" align="center" />
        <el-table-column prop="topicName" label="选题名称" min-width="300">
          <template #default="scope">
            <div class="topic-title">{{ scope.row.topicName }}</div>
            <div class="topic-type small-text">{{ scope.row.topicType }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="指导教师" width="120" align="center" />
        <el-table-column prop="maxStudentCount" label="限选人数" width="100" align="center" />
        <el-table-column prop="currentStudentCount" label="已选人数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 2 ? 'success' : scope.row.status === 4 ? 'primary' : 'warning'">
              {{ scope.row.status === 2 ? '可选' : scope.row.status === 4 ? '审核中' : '不可选' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="selectTopicAsVolunteer(scope.row, 0)"
              :disabled="scope.row.status !== 2"
              style="margin-right: 5px;"
            >
              设为第一志愿
            </el-button>
            <el-button 
              type="success" 
              size="small" 
              @click="selectTopicAsVolunteer(scope.row, 1)"
              :disabled="scope.row.status !== 2"
            >
              设为第二志愿
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
  
  <!-- 自拟题目对话框 -->
  <el-dialog
    v-model="selfTopicDialogVisible"
    title="申请自拟题目"
    width="600px"
    destroy-on-close
  >
    <el-form :model="selfTopicForm" label-width="100px">
      <el-form-item label="题目名称" required>
        <el-input
          v-model="selfTopicForm.topicName"
          placeholder="请输入自拟题目名称"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="题目描述" required>
        <el-input
          v-model="selfTopicForm.topicDescription"
          type="textarea"
          placeholder="请输入自拟题目描述"
          :rows="3"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="题目要求">
        <el-input
          v-model="selfTopicForm.topicRequirements"
          type="textarea"
          placeholder="请输入自拟题目要求"
          :rows="3"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="期望指导教师">
        <el-input
          v-model="selfTopicForm.expectedTeacher"
          placeholder="请输入期望的指导教师姓名"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="selfTopicDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="applySelfTopic">提交申请</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 选题结果对话框 -->
  <el-dialog
    v-model="topicResultDialogVisible"
    title="选题结果"
    width="500px"
    destroy-on-close
  >
    <div class="topic-result-content">
      <div class="result-item">
        <span class="result-label">分配题目：</span>
        <span class="result-value">{{ topicResult.topicName || '暂未分配' }}</span>
      </div>
      <div class="result-item">
        <span class="result-label">指导教师：</span>
        <span class="result-value">{{ topicResult.teacherName || '暂未分配' }}</span>
      </div>
      <div class="result-item">
        <span class="result-label">分配状态：</span>
        <el-tag :type="topicResult.resultStatus === '已分配' ? 'success' : 'info'">
          {{ topicResult.resultStatus }}
        </el-tag>
      </div>
      <div v-if="topicResult.assignTime" class="result-item">
        <span class="result-label">分配时间：</span>
        <span class="result-value">{{ topicResult.assignTime }}</span>
      </div>
    </div>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="topicResultDialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.topic-apply-container {
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

.function-row {
  margin-bottom: 20px;
}

.function-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.function-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.volunteer-section {
  height: fit-content;
}

.filter-section {
  height: fit-content;
}

.volunteer-list {
  padding: 10px 0;
}

.volunteer-item {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.volunteer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.volunteer-level {
  font-weight: 600;
  color: #409eff;
}

.volunteer-content {
  min-height: 36px;
  display: flex;
  align-items: center;
}

.selected-topic {
  display: flex;
  align-items: center;
}

.no-topic {
  color: #909399;
  font-style: italic;
}

.self-topic-section {
  margin-top: 20px;
  text-align: center;
}

.small-text {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.topic-title {
  font-weight: 500;
}

.topic-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.topic-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.topic-result-content {
  padding: 20px 0;
}

.result-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.result-label {
  width: 100px;
  font-weight: 500;
  color: #303133;
}

.result-value {
  flex: 1;
  color: #606266;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>