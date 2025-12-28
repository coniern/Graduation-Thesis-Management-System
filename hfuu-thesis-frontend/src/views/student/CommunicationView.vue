<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../../utils/axios'

// 消息列表
const messages = ref([])

// 批注列表
const comments = ref([])

// 选中的消息
const selectedMessage = ref(null)
const messageDialogVisible = ref(false)

// 发送消息表单
const sendMessageForm = ref({
  title: '',
  content: ''
})
const sendDialogVisible = ref(false)

// 预约面谈表单
const appointmentForm = ref({
  date: '',
  time: '',
  location: '',
  purpose: ''
})
const appointmentDialogVisible = ref(false)

// 学生ID（实际应该从登录信息中获取）
const studentId = ref(1)

// 教师ID（实际应该从选题结果中获取）
const teacherId = ref(1001)

// 加载消息列表
const loadMessages = async () => {
  try {
    // 实际应该调用后端API获取消息列表
    // const response = await axios.get(`/api/student/messages`)
    // messages.value = response.data
    
    // 模拟消息数据
    messages.value = [
      {
        id: 1,
        title: '开题报告审阅意见',
        content: '你的开题报告整体结构清晰，但研究方法部分需要进一步细化。请参考相关文献，补充具体的研究设计和数据分析方法。',
        senderId: 1001,
        senderName: '李四',
        receiverId: 1,
        sendTime: '2024-03-20 14:30:00',
        read: false,
        type: 'comment' // comment: 批注, message: 消息, appointment: 预约
      },
      {
        id: 2,
        title: '中期检查提醒',
        content: '请于本周内完成中期检查报告，并上传至系统。如有问题，可随时预约面谈。',
        senderId: 1001,
        senderName: '李四',
        receiverId: 1,
        sendTime: '2024-04-15 09:00:00',
        read: true,
        type: 'message'
      }
    ]
  } catch (error) {
    ElMessage.error('获取消息列表失败')
    console.error('获取消息列表失败:', error)
  }
}

// 加载批注列表
const loadComments = async () => {
  try {
    // 实际应该调用后端API获取批注列表
    // const response = await axios.get(`/api/student/comments`)
    // comments.value = response.data
    
    // 模拟批注数据
    comments.value = [
      {
        id: 1,
        documentId: 1,
        documentName: '开题报告.docx',
        commentContent: '研究背景部分需要补充最新的研究进展。',
        commentLocation: '第1页，第2段',
        teacherId: 1001,
        teacherName: '李四',
        commentTime: '2024-03-20 14:30:00',
        status: 1 // 1: 未处理, 2: 已处理
      },
      {
        id: 2,
        documentId: 1,
        documentName: '开题报告.docx',
        commentContent: '研究意义部分需要更明确地阐述理论价值和实践价值。',
        commentLocation: '第2页，第1段',
        teacherId: 1001,
        teacherName: '李四',
        commentTime: '2024-03-20 14:35:00',
        status: 2
      }
    ]
  } catch (error) {
    ElMessage.error('获取批注列表失败')
    console.error('获取批注列表失败:', error)
  }
}

// 查看消息详情
const viewMessageDetail = (message) => {
  selectedMessage.value = message
  // 标记为已读
  if (!message.read) {
    message.read = true
    // 实际应该调用后端API更新消息状态
    // axios.put(`/api/student/messages/${message.id}/read`)
  }
  messageDialogVisible.value = true
}

// 发送消息
const sendMessage = async () => {
  try {
    // 验证表单
    if (!sendMessageForm.value.title || !sendMessageForm.value.content) {
      ElMessage.warning('请填写标题和内容')
      return
    }
    
    // 实际应该调用后端API发送消息
    // const response = await axios.post(`/api/student/messages/send`, {
    //   ...sendMessageForm.value,
    //   receiverId: teacherId.value
    // })
    
    ElMessage.success('消息发送成功')
    sendDialogVisible.value = false
    sendMessageForm.value = { title: '', content: '' }
    loadMessages()
  } catch (error) {
    ElMessage.error('消息发送失败')
    console.error('消息发送失败:', error)
  }
}

// 预约面谈
const submitAppointment = async () => {
  try {
    // 验证表单
    if (!appointmentForm.value.date || !appointmentForm.value.time || !appointmentForm.value.purpose) {
      ElMessage.warning('请填写完整的预约信息')
      return
    }
    
    // 实际应该调用后端API提交预约
    // const response = await axios.post(`/api/student/appointments`, {
    //   ...appointmentForm.value,
    //   teacherId: teacherId.value
    // })
    
    ElMessage.success('预约提交成功')
    appointmentDialogVisible.value = false
    appointmentForm.value = { date: '', time: '', location: '', purpose: '' }
  } catch (error) {
    ElMessage.error('预约提交失败')
    console.error('预约提交失败:', error)
  }
}

// 处理批注
const handleComment = async (commentId) => {
  try {
    await ElMessageBox.confirm('是否已处理该批注？', '处理确认', {
      confirmButtonText: '已处理',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 实际应该调用后端API更新批注状态
    // const response = await axios.put(`/api/student/comments/${commentId}/handle`)
    
    ElMessage.success('批注已处理')
    loadComments()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('处理失败')
      console.error('处理失败:', error)
    }
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadMessages()
  loadComments()
})
</script>

<template>
  <div class="communication-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>指导与交流</h1>
      <p>与指导教师进行沟通交流，查看批注与反馈</p>
    </div>
    
    <!-- 功能按钮区 -->
    <div class="function-buttons">
      <el-button type="primary" @click="sendDialogVisible = true">
        <el-icon><ChatDotRound /></el-icon>
        发送消息
      </el-button>
      <el-button type="success" @click="appointmentDialogVisible = true">
        <el-icon><Calendar /></el-icon>
        预约面谈
      </el-button>
    </div>
    
    <!-- 内容区域 -->
    <div class="content-grid">
      <!-- 消息列表 -->
      <el-card class="content-card message-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <h3 class="card-title">消息中心</h3>
            <el-badge :value="messages.filter(m => !m.read).length" type="danger" />
          </div>
        </template>
        
        <el-empty v-if="messages.length === 0" description="暂无消息" />
        <el-list v-else>
          <el-list-item
            v-for="message in messages"
            :key="message.id"
            :class="{ 'unread': !message.read }"
            @click="viewMessageDetail(message)"
          >
            <template #avatar>
              <el-avatar :size="40">
                {{ message.senderName.charAt(0) }}
              </el-avatar>
            </template>
            <template #title>
              <div class="message-title">
                <span>{{ message.title }}</span>
                <span class="message-time">{{ message.sendTime }}</span>
              </div>
            </template>
            <template #default>
              <div class="message-content">{{ message.content }}</div>
            </template>
          </el-list-item>
        </el-list>
      </el-card>
      
      <!-- 批注列表 -->
      <el-card class="content-card comment-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <h3 class="card-title">教师批注</h3>
          </div>
        </template>
        
        <el-empty v-if="comments.length === 0" description="暂无批注" />
        <el-table v-else :data="comments" style="width: 100%" border stripe>
          <el-table-column prop="documentName" label="文档名称" width="180" />
          <el-table-column prop="commentLocation" label="位置" width="120" align="center" />
          <el-table-column prop="commentContent" label="批注内容" min-width="200">
            <template #default="scope">
              <div class="comment-content">{{ scope.row.commentContent }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="teacherName" label="教师" width="100" align="center" />
          <el-table-column prop="commentTime" label="批注时间" width="180" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.status === 1 ? 'warning' : 'success'">
                {{ scope.row.status === 1 ? '未处理' : '已处理' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template #default="scope">
              <el-button 
                v-if="scope.row.status === 1"
                type="primary" 
                size="small"
                @click="handleComment(scope.row.id)"
              >
                <el-icon><Check /></el-icon>
                已处理
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 消息详情对话框 -->
    <el-dialog
      v-model="messageDialogVisible"
      :title="selectedMessage?.title || '消息详情'"
      width="600px"
      destroy-on-close
    >
      <div v-if="selectedMessage" class="message-detail">
        <div class="message-meta">
          <div class="meta-item">
            <span class="meta-label">发送人：</span>
            <span class="meta-value">{{ selectedMessage.senderName }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">发送时间：</span>
            <span class="meta-value">{{ selectedMessage.sendTime }}</span>
          </div>
        </div>
        <div class="message-body">
          <h4>消息内容：</h4>
          <p>{{ selectedMessage.content }}</p>
        </div>
      </div>
    </el-dialog>
    
    <!-- 发送消息对话框 -->
    <el-dialog
      v-model="sendDialogVisible"
      title="发送消息"
      width="600px"
      destroy-on-close
    >
      <el-form :model="sendMessageForm" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="sendMessageForm.title" placeholder="请输入消息标题" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input
            v-model="sendMessageForm.content"
            type="textarea"
            placeholder="请输入消息内容"
            :rows="6"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="sendDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="sendMessage">发送</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 预约面谈对话框 -->
    <el-dialog
      v-model="appointmentDialogVisible"
      title="预约面谈"
      width="600px"
      destroy-on-close
    >
      <el-form :model="appointmentForm" label-width="100px">
        <el-form-item label="日期" required>
          <el-date-picker
            v-model="appointmentForm.date"
            type="date"
            placeholder="请选择日期"
            style="width: 100%"
            :disabled-date="(time) => time.getTime() < Date.now() - 8.64e7"
          />
        </el-form-item>
        <el-form-item label="时间" required>
          <el-time-picker
            v-model="appointmentForm.time"
            placeholder="请选择时间"
            style="width: 100%"
            format="HH:mm"
            value-format="HH:mm"
          />
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="appointmentForm.location" placeholder="请输入面谈地点" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="目的" required>
          <el-input
            v-model="appointmentForm.purpose"
            type="textarea"
            placeholder="请输入面谈目的"
            :rows="4"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="appointmentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAppointment">提交预约</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.communication-container {
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

.function-buttons {
  margin-bottom: 20px;
}

.function-buttons .el-button {
  margin-right: 10px;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 20px;
}

.content-card {
  height: 600px;
  overflow-y: auto;
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

.message-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-time {
  font-size: 12px;
  color: #909399;
}

.message-content {
  font-size: 13px;
  color: #606266;
  margin-top: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.el-list-item.unread {
  background-color: #eff6ff;
  cursor: pointer;
}

.comment-content {
  font-size: 13px;
  color: #606266;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.message-detail {
  padding: 10px 0;
}

.message-meta {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.meta-item {
  margin-bottom: 5px;
}

.meta-label {
  font-weight: 500;
  color: #303133;
}

.meta-value {
  color: #606266;
}

.message-body {
  margin-top: 20px;
}

.message-body h4 {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .content-card {
    height: auto;
    min-height: 400px;
  }
}
</style>