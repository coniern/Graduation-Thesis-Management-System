<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../../utils/axios'

// 答辩分组信息
const defenseGroup = ref(null)

// 答辩信息
const defenseInfo = ref(null)

// 答辩成绩
const defenseScore = ref(null)

// 申诉表单
const appealForm = ref({
  title: '',
  content: '',
  scoreId: null
})
const appealDialogVisible = ref(false)

// PPT上传
const uploadRef = ref(null)
const pptUploaded = ref(false)
const pptFileName = ref('')

// 学生ID（实际应该从登录信息中获取）
const studentId = ref(1)

// 加载答辩分组信息
const loadDefenseGroup = async () => {
  try {
    // 实际应该调用后端API获取答辩分组信息
    // const response = await axios.get(`/api/student/defense/group`)
    // defenseGroup.value = response.data
    
    // 模拟答辩分组数据
    defenseGroup.value = {
      groupId: 101,
      groupName: '金融工程答辩组',
      teacher: '李四',
      members: [
        { studentId: 1, studentName: '张三', studentNo: '2021001', topic: '金融科技发展趋势研究' },
        { studentId: 2, studentName: '王五', studentNo: '2021002', topic: '金融风险管理策略' },
        { studentId: 3, studentName: '赵六', studentNo: '2021003', topic: '互联网金融创新' }
      ],
      defenseTime: '2024-06-15 09:00',
      defenseLocation: '金融学院101会议室'
    }
  } catch (error) {
    ElMessage.error('获取答辩分组信息失败')
    console.error('获取答辩分组信息失败:', error)
  }
}

// 加载答辩信息
const loadDefenseInfo = async () => {
  try {
    // 实际应该调用后端API获取答辩信息
    // const response = await axios.get(`/api/student/defense/info`)
    // defenseInfo.value = response.data
    
    // 模拟答辩信息
    defenseInfo.value = {
      defenseId: 1,
      title: '2024届金融专业毕业论文答辩',
      description: '哈尔滨金融学院2024届金融专业毕业论文答辩',
      defenseDate: '2024-06-15',
      defenseTime: '09:00-12:00',
      location: '金融学院101会议室',
      status: '已安排', // 已安排, 已完成, 已延期
      organizer: '金融学院学术委员会'
    }
  } catch (error) {
    ElMessage.error('获取答辩信息失败')
    console.error('获取答辩信息失败:', error)
  }
}

// 加载答辩成绩
const loadDefenseScore = async () => {
  try {
    // 实际应该调用后端API获取答辩成绩
    // const response = await axios.get(`/api/student/defense/score`)
    // defenseScore.value = response.data
    
    // 模拟答辩成绩
    defenseScore.value = {
      scoreId: 1,
      finalScore: 88,
      defenseScore: 90,
      paperScore: 85,
      commentScore: 92,
      comments: [
        {
          reviewer: '李四',
          role: '答辩组长',
          comment: '论文结构清晰，数据分析深入，答辩表现优秀'
        },
        {
          reviewer: '王五',
          role: '答辩委员',
          comment: '研究方法得当，结论具有实际应用价值'
        },
        {
          reviewer: '赵六',
          role: '答辩委员',
          comment: '答辩思路清晰，对问题回答准确'
        }
      ],
      overallComment: '该生论文选题具有实际意义，研究方法科学，数据分析深入，答辩表现优秀。'
    }
  } catch (error) {
    ElMessage.error('获取答辩成绩失败')
    console.error('获取答辩成绩失败:', error)
  }
}

// 上传答辩PPT
const uploadPPT = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file.raw)
    formData.append('studentId', studentId.value)
    
    // 实际应该调用后端API上传PPT
    // const response = await axios.post('/api/student/defense/ppt/upload', formData, {
    //   headers: {
    //     'Content-Type': 'multipart/form-data'
    //   }
    // })
    
    // 模拟上传成功
    pptFileName.value = file.name
    pptUploaded.value = true
    ElMessage.success('PPT上传成功')
    
    // 清空上传队列
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  } catch (error) {
    ElMessage.error('PPT上传失败')
    console.error('PPT上传失败:', error)
  }
}

// 提交答辩申请
const submitDefenseApplication = async () => {
  try {
    // 实际应该调用后端API提交申请
    // const response = await axios.post('/api/student/defense/application', {
    //   studentId: studentId.value,
    //   reason: '因参加金融行业实习，申请调整答辩时间'
    // })
    
    await ElMessageBox.confirm('确定要提交答辩申请吗？', '提交确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    ElMessage.success('答辩申请已提交')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交答辩申请失败')
      console.error('提交答辩申请失败:', error)
    }
  }
}

// 提交申诉
const submitAppeal = async () => {
  try {
    // 验证表单
    if (!appealForm.value.title || !appealForm.value.content) {
      ElMessage.warning('请填写申诉标题和内容')
      return
    }
    
    // 实际应该调用后端API提交申诉
    // const response = await axios.post('/api/student/score/appeal', appealForm.value)
    
    ElMessage.success('申诉申请已提交')
    appealDialogVisible.value = false
    appealForm.value = {
      title: '',
      content: '',
      scoreId: null
    }
  } catch (error) {
    ElMessage.error('提交申诉失败')
    console.error('提交申诉失败:', error)
  }
}

// 打开申诉对话框
const openAppealDialog = () => {
  appealForm.value.scoreId = defenseScore.value?.scoreId
  appealDialogVisible.value = true
}

// 页面挂载时加载数据
onMounted(() => {
  loadDefenseGroup()
  loadDefenseInfo()
  loadDefenseScore()
})
</script>

<template>
  <div class="defense-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>答辩与成绩管理</h1>
      <p>哈尔滨金融学院毕业论文答辩成绩管理系统</p>
    </div>
    
    <!-- 答辩分组信息 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <h3>答辩分组信息</h3>
        </div>
      </template>
      
      <div v-if="defenseGroup" class="defense-group-info">
        <div class="group-details">
          <div class="detail-item">
            <span class="label">分组ID：</span>
            <span class="value">{{ defenseGroup.groupId }}</span>
          </div>
          <div class="detail-item">
            <span class="label">分组名称：</span>
            <span class="value">{{ defenseGroup.groupName }}</span>
          </div>
          <div class="detail-item">
            <span class="label">答辩组长：</span>
            <span class="value">{{ defenseGroup.teacher }}</span>
          </div>
          <div class="detail-item">
            <span class="label">答辩时间：</span>
            <span class="value">{{ defenseGroup.defenseTime }}</span>
          </div>
          <div class="detail-item">
            <span class="label">答辩地点：</span>
            <span class="value">{{ defenseGroup.defenseLocation }}</span>
          </div>
        </div>
        
        <!-- 分组学生列表 -->
        <div class="group-students">
          <h4>分组学生</h4>
          <el-table :data="defenseGroup.members" style="width: 100%" border>
            <el-table-column prop="studentNo" label="学号" width="120" align="center" />
            <el-table-column prop="studentName" label="姓名" width="100" align="center" />
            <el-table-column prop="topic" label="论文题目" min-width="300" />
          </el-table>
        </div>
      </div>
      
      <!-- 答辩申请按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="submitDefenseApplication">
          <el-icon><Document /></el-icon>
          提交答辩申请
        </el-button>
      </div>
    </el-card>
    
    <!-- 答辩信息 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <h3>答辩信息</h3>
        </div>
      </template>
      
      <div v-if="defenseInfo" class="defense-info">
        <div class="info-grid">
          <div class="info-item">
            <span class="label">答辩标题：</span>
            <span class="value">{{ defenseInfo.title }}</span>
          </div>
          <div class="info-item">
            <span class="label">答辩描述：</span>
            <span class="value">{{ defenseInfo.description }}</span>
          </div>
          <div class="info-item">
            <span class="label">答辩时间：</span>
            <span class="value">{{ defenseInfo.defenseDate }} {{ defenseInfo.defenseTime }}</span>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- PPT上传区域 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <h3>上传答辩PPT</h3>
        </div>
      </template>
      
      <div class="upload-section">
        <el-upload
          ref="uploadRef"
          class="upload-btn"
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="uploadPPT"
          accept=".ppt,.pptx"
          :file-list="[]"
        >
          <el-button type="primary" size="large">
            <el-icon><Upload /></el-icon>
            选择PPT文件
          </el-button>
        </el-upload>
        
        <div v-if="pptUploaded" class="upload-success">
          <el-icon><Check /></el-icon>
          PPT已上传：{{ pptFileName }}
        </div>
      </div>
    </el-card>
    
    <!-- 答辩成绩 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <h3>答辩成绩</h3>
        </div>
      </template>
      
      <div v-if="defenseScore" class="score-section">
        <div class="final-score">
          <div class="score-display">
            <span class="score-label">最终成绩：</span>
            <span class="score-value">{{ defenseScore.finalScore }}</span>
          </div>
        </div>
        
        <!-- 成绩构成 -->
        <div class="score-components">
          <h4>成绩构成</h4>
          <el-row :gutter="20">
            <el-col :xs="12" :sm="12" :md="8">
              <div class="score-component">
                <span class="component-name">答辩表现</span>
                <span class="component-value">{{ defenseScore.defenseScore }}</span>
              </div>
            </el-col>
            <el-col :xs="12" :sm="12" :md="8">
              <div class="score-component">
                <span class="component-name">论文质量</span>
                <span class="component-value">{{ defenseScore.paperScore }}</span>
              </div>
            </el-col>
            <el-col :xs="12" :sm="12" :md="8">
              <div class="score-component">
                <span class="component-name">评语成绩</span>
                <span class="component-value">{{ defenseScore.commentScore }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
        
        <!-- 评审意见 -->
        <div class="reviews-section">
          <h4>评审意见</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(comment, index) in defenseScore.comments"
              :key="index"
              :timestamp="comment.reviewer + ' - ' + comment.role"
            >
              {{ comment.comment }}
            </el-timeline-item>
          </el-timeline>
        </div>
        
        <!-- 总体评语 -->
        <div class="overall-comment">
          <h4>总体评语</h4>
          <p>{{ defenseScore.overallComment }}</p>
        </div>
        
        <!-- 申诉按钮 -->
        <div class="appeal-section">
          <el-button type="warning" @click="openAppealDialog">
            <el-icon><Warning /></el-icon>
            成绩申诉
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
  
  <!-- 申诉对话框 -->
  <el-dialog
    v-model="appealDialogVisible"
    title="成绩申诉"
    width="600px"
    destroy-on-close
  >
    <el-form :model="appealForm" label-width="80px">
      <el-form-item label="申诉标题" required>
        <el-input v-model="appealForm.title" placeholder="请输入申诉标题" maxlength="50" show-word-limit />
      </el-form-item>
      <el-form-item label="申诉内容" required>
        <el-input
          v-model="appealForm.content"
          type="textarea"
          :rows="4"
          placeholder="请详细描述您的申诉理由"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="appealDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAppeal">提交申诉</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.defense-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  text-align: center;
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

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.defense-group-info {
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.group-details {
  margin-top: 15px;
}

.detail-item {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.detail-item .label {
  width: 120px;
  font-weight: 500;
  color: #303133;
}

.detail-item .value {
  flex: 1;
  color: #606266;
}

.group-students {
  margin-top: 20px;
}

.group-students h4 {
  margin: 0 0 15px;
  font-size: 16px;
  font-weight: 500;
}

.upload-section {
  margin: 20px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.upload-btn {
  margin-bottom: 15px;
}

.upload-success {
  margin-top: 10px;
  color: #67c23a;
  font-size: 14px;
}

.score-section {
  margin-top: 30px;
}

.score-display {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.score-value {
  font-size: 48px;
  font-weight: 600;
  color: #303133;
}

.score-components {
  margin-top: 30px;
}

.score-component {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.score-component h4 {
  margin: 0 0 15px;
  font-size: 16px;
  font-weight: 500;
}

.reviews-section {
  margin-top: 30px;
}

.overall-comment {
  margin-top: 20px;
  padding: 15px;
  background-color: #ecf5ff;
  border-left: 4px solid #409eff;
  border-radius: 4px;
}

.appeal-section {
  margin-top: 20px;
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>