<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../../utils/axios'

// 选题列表
const topics = ref([])

// 新增/编辑对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增选题')
const currentTopic = ref({
  topicName: '',
  topicDescription: '',
  topicRequirements: '',
  topicType: '研究型',
  difficulty: 2,
  restrictedMajors: '',
  maxStudentCount: 3
})

// 状态映射
const statusMap = {
  1: { text: '未发布', type: 'info' },
  2: { text: '已发布', type: 'success' },
  3: { text: '已结束', type: 'warning' },
  4: { text: '审核中', type: 'primary' },
  5: { text: '审核驳回', type: 'danger' }
}

// 难易度映射
const difficultyMap = {
  1: '简单',
  2: '中等',
  3: '困难'
}

// 类型选项
const topicTypeOptions = [
  { label: '研究型', value: '研究型' },
  { label: '技术型', value: '技术型' },
  { label: '应用型', value: '应用型' }
]

// 难易度选项
const difficultyOptions = [
  { label: '简单', value: 1 },
  { label: '中等', value: 2 },
  { label: '困难', value: 3 }
]

// 加载题目列表
const loadTopics = async () => {
  try {
    const response = await axios.get('/api/teacher/my-topics')
    topics.value = response.data
  } catch (error) {
    ElMessage.error('获取题目列表失败')
    console.error('获取题目列表失败:', error)
  }
}

// 打开新增对话框
const openAddDialog = () => {
  dialogTitle.value = '新增选题'
  currentTopic.value = {
    topicName: '',
    topicDescription: '',
    topicRequirements: '',
    topicType: '研究型',
    difficulty: 2,
    restrictedMajors: '',
    maxStudentCount: 3
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (topic) => {
  dialogTitle.value = '编辑选题'
  currentTopic.value = { ...topic }
  dialogVisible.value = true
}

// 保存题目
const saveTopic = async () => {
  try {
    if (currentTopic.value.topicId) {
      // 更新题目
      await axios.put(`/api/teacher/update-topic/${currentTopic.value.topicId}`, currentTopic.value)
      ElMessage.success('更新题目成功')
    } else {
      // 新增题目
      await axios.post('/api/teacher/publish-topic', {
        ...currentTopic.value,
        teacherId: JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').userId,
        teacherName: JSON.parse((typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : '') || '{}').realName,
        status: 4, // 状态设为审核中
        currentStudentCount: 0
      })
      ElMessage.success('提交题目成功，等待审核')
    }
    dialogVisible.value = false
    loadTopics()
  } catch (error) {
    ElMessage.error(currentTopic.value.topicId ? '更新题目失败' : '提交题目失败')
    console.error('保存题目失败:', error)
  }
}

// 撤回题目
const withdrawTopic = async (topic) => {
  try {
    await ElMessageBox.confirm('确定要撤回该题目吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.put(`/api/teacher/withdraw-topic/${topic.topicId}`)
    ElMessage.success('撤回题目成功')
    loadTopics()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('撤回题目失败')
      console.error('撤回题目失败:', error)
    }
  }
}

// 删除题目
const deleteTopic = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该题目吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'danger'
    })
    await axios.delete(`/api/teacher/delete-topic/${id}`)
    ElMessage.success('删除题目成功')
    loadTopics()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除题目失败')
      console.error('删除题目失败:', error)
    }
  }
}

// 查看申请列表
const viewApplications = (topic) => {
  // TODO: 实现查看申请列表功能
  console.log('查看选题申请列表:', topic)
}

// 页面挂载时加载数据
onMounted(() => {
  loadTopics()
})
</script>

<template>
  <div class="topic-manage-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>题目管理</h1>
      <p>管理您的毕业论文题目</p>
    </div>
    
    <!-- 操作按钮 -->
    <div class="action-section">
      <el-button type="primary" @click="openAddDialog">
        <el-icon><Plus /></el-icon>
        提交题目
      </el-button>
    </div>
    
    <!-- 选题列表 -->
    <el-card shadow="hover" class="topic-list-card">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">我的题目列表</h3>
        </div>
      </template>
      
      <el-table :data="topics" style="width: 100%" border stripe>
        <el-table-column prop="topicId" label="题目编号" width="100" align="center" />
        <el-table-column prop="topicName" label="题目名称" min-width="300">
          <template #default="scope">
            <div class="topic-title">{{ scope.row.topicName }}</div>
            <div class="topic-type small-text">{{ scope.row.topicType }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难易度" width="100" align="center">
          <template #default="scope">
            <el-tag size="small">{{ difficultyMap[scope.row.difficulty] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="maxStudentCount" label="限选人数" width="100" align="center" />
        <el-table-column prop="currentStudentCount" label="已选人数" width="100" align="center" />
        <el-table-column prop="status" label="审核状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status]?.type">{{ statusMap[scope.row.status]?.text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewApplications(scope.row)" style="margin-right: 5px;">
              <el-icon><View /></el-icon>
              查看申请
            </el-button>
            <el-button type="success" size="small" @click="openEditDialog(scope.row)" style="margin-right: 5px;">
              <el-icon><Edit /></el-icon>
              修改
            </el-button>
            <el-button type="warning" size="small" @click="withdrawTopic(scope.row)" style="margin-right: 5px;">
              <el-icon><CircleClose /></el-icon>
              撤回
            </el-button>
            <el-button type="danger" size="small" @click="deleteTopic(scope.row.topicId)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form :model="currentTopic" label-width="100px" :rules="{}">
        <el-form-item label="题目名称" required>
          <el-input
            v-model="currentTopic.topicName"
            placeholder="请输入题目名称"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="题目描述" required>
          <el-input
            v-model="currentTopic.topicDescription"
            type="textarea"
            placeholder="请输入题目描述"
            :rows="3"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="题目要求" required>
          <el-input
            v-model="currentTopic.topicRequirements"
            type="textarea"
            placeholder="请输入题目要求"
            :rows="3"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="题目类型" required>
          <el-select v-model="currentTopic.topicType" placeholder="请选择题目类型">
            <el-option
              v-for="option in topicTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="难易度" required>
          <el-select v-model="currentTopic.difficulty" placeholder="请选择难易度">
            <el-option
              v-for="option in difficultyOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="限选专业">
          <el-input
            v-model="currentTopic.restrictedMajors"
            placeholder="请输入限选专业，多个专业用逗号分隔"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="最大可选人数" required>
          <el-input-number
            v-model="currentTopic.maxStudentCount"
            :min="1"
            :max="10"
            placeholder="请输入最大可选人数"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveTopic">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.topic-manage-container {
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

.action-section {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
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

.topic-list-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.topic-list-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.topic-title {
  font-weight: 500;
}

.small-text {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>