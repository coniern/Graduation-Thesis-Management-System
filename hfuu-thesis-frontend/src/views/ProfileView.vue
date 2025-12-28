<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../utils/axios'

// 用户信息
const userInfo = ref({
  userId: 1,
  username: '',
  realName: '',
  userType: 1,
  email: '',
  phone: '',
  department: '',
  major: '',
  studentId: '',
  teacherId: ''
})

// 用户类型映射
const userTypeMap = {
  1: '学生',
  2: '导师',
  3: '院系管理员',
  4: '校级管理员'
}

// 密码表单
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 任务列表
const tasks = ref([])

// 已提交文档列表
const submittedDocuments = ref([])

// 系统通知列表
const notifications = ref([])

// 计算未读通知数
const unreadNotificationCount = computed(() => {
  return notifications.filter(n => !n.read).length
})

// 加载用户信息
const loadUserInfo = async () => {
  // 从localStorage获取用户信息，确保localStorage存在
  const userStr = typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : ''
  if (userStr) {
    userInfo.value = { ...userInfo.value, ...JSON.parse(userStr) }
    
    // 根据用户类型设置默认值
    if (userInfo.value.userType === 1) {
      // 学生默认值
      userInfo.value.studentId = '20210001'
      userInfo.value.major = '金融学'
      userInfo.value.department = '金融学院'
      userInfo.value.email = `${userInfo.value.username}@hfuu.edu.cn`
      userInfo.value.phone = '13800138000'
    } else if (userInfo.value.userType === 2) {
      // 教师默认值
      userInfo.value.teacherId = 'T001'
      userInfo.value.department = '金融学院'
      userInfo.value.email = `${userInfo.value.username}@hfuu.edu.cn`
      userInfo.value.phone = '13900139000'
    } else {
      // 管理员默认值
      userInfo.value.department = '教务处'
      userInfo.value.email = `${userInfo.value.username}@hfuu.edu.cn`
      userInfo.value.phone = '13700137000'
    }
  }
}

// 加载任务列表
const loadTasks = async () => {
  try {
    // 模拟任务数据
    tasks.value = [
      {
        id: 1,
        title: '开题报告提交',
        deadline: '2024-03-30',
        status: 1, // 1: 未完成, 2: 已完成
        description: '提交开题报告初稿，要求字数不少于3000字'
      },
      {
        id: 2,
        title: '中期检查',
        deadline: '2024-04-30',
        status: 1,
        description: '完成中期检查报告，提交论文进展情况'
      },
      {
        id: 3,
        title: '论文初稿提交',
        deadline: '2024-05-15',
        status: 2,
        description: '提交论文初稿，要求完成80%以上内容'
      }
    ]
  } catch (error) {
    ElMessage.error('获取任务列表失败')
    console.error('获取任务列表失败:', error)
  }
}

// 加载已提交文档
const loadSubmittedDocuments = async () => {
  try {
    // 模拟已提交文档数据
    submittedDocuments.value = [
      {
        id: 1,
        name: '开题报告.docx',
        type: '开题报告',
        uploadTime: '2024-03-25 14:30:00',
        status: '已审核',
        downloadUrl: '#'
      },
      {
        id: 2,
        name: '中期报告.docx',
        type: '中期报告',
        uploadTime: '2024-04-28 09:15:00',
        status: '审核中',
        downloadUrl: '#'
      }
    ]
  } catch (error) {
    ElMessage.error('获取已提交文档失败')
    console.error('获取已提交文档失败:', error)
  }
}

// 加载系统通知
const loadNotifications = async () => {
  try {
    // 模拟系统通知数据
    notifications.value = [
      {
        id: 1,
        title: '开题报告提交截止日期提醒',
        content: '开题报告提交截止日期为2024-03-30，请及时提交',
        time: '2024-03-20 10:00:00',
        read: false
      },
      {
        id: 2,
        title: '中期检查安排通知',
        content: '中期检查将于2024-04-30开始，请做好准备',
        time: '2024-04-15 14:30:00',
        read: true
      }
    ]
  } catch (error) {
    ElMessage.error('获取系统通知失败')
    console.error('获取系统通知失败:', error)
  }
}

// 保存用户信息
const saveUserInfo = async () => {
  try {
    // 模拟保存用户信息
    await new Promise(resolve => setTimeout(resolve, 500))
    // 更新localStorage中的用户信息，确保localStorage存在
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    }
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  }
}

// 修改密码
const changePassword = async () => {
  try {
    // 验证表单
    if (!passwordForm.value.oldPassword) {
      ElMessage.warning('请输入原密码')
      return
    }
    
    if (!passwordForm.value.newPassword) {
      ElMessage.warning('请输入新密码')
      return
    }
    
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      ElMessage.warning('两次输入的密码不一致')
      return
    }
    
    // 模拟修改密码
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('密码修改成功')
    
    // 清空表单
    passwordForm.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    ElMessage.error('密码修改失败')
    console.error('密码修改失败:', error)
  }
}

// 下载文档
const downloadDocument = (documentId, fileName) => {
  try {
    // 实际应该调用后端API下载文档
    // const response = await axios.get(`/api/file/download/${documentId}`, {
    //   responseType: 'blob'
    // })
    
    ElMessage.success(`文件${fileName}下载成功`)
  } catch (error) {
    ElMessage.error('文件下载失败')
    console.error('文件下载失败:', error)
  }
}

// 标记通知为已读
const markAsRead = (notificationId) => {
  const notification = notifications.value.find(n => n.id === notificationId)
  if (notification) {
    notification.read = true
    // 实际应该调用后端API标记为已读
    // axios.put(`/api/notifications/${notificationId}/read`)
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadUserInfo()
  loadTasks()
  loadSubmittedDocuments()
  loadNotifications()
})
</script>

<template>
  <div class="profile-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>个人中心</h1>
      <p>管理您的个人信息和查看相关内容</p>
    </div>
    
    <!-- 个人资料卡片 -->
    <el-card class="profile-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">基本信息</h3>
        </div>
      </template>
      
      <el-form :model="userInfo" label-width="120px" class="profile-form">
        <el-row :gutter="20">
          <!-- 左侧列 -->
          <el-col :xs="24" :md="12">
            <el-form-item label="用户名">
              <el-input v-model="userInfo.username" readonly />
            </el-form-item>
            
            <el-form-item label="真实姓名">
              <el-input v-model="userInfo.realName" />
            </el-form-item>
            
            <el-form-item label="用户类型">
              <el-input v-model="userTypeMap[userInfo.userType]" readonly />
            </el-form-item>
            
            <el-form-item label="邮箱">
              <el-input v-model="userInfo.email" />
            </el-form-item>
          </el-col>
          
          <!-- 右侧列 -->
          <el-col :xs="24" :md="12">
            <el-form-item label="联系电话">
              <el-input v-model="userInfo.phone" />
            </el-form-item>
            
            <el-form-item label="所属部门">
              <el-input v-model="userInfo.department" />
            </el-form-item>
            
            <el-form-item v-if="userInfo.userType === 1" label="专业">
              <el-input v-model="userInfo.major" />
            </el-form-item>
            
            <el-form-item v-if="userInfo.userType === 1" label="学号">
              <el-input v-model="userInfo.studentId" />
            </el-form-item>
            
            <el-form-item v-else-if="userInfo.userType === 2" label="工号">
              <el-input v-model="userInfo.teacherId" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="saveUserInfo">保存修改</el-button>
          <el-button @click="loadUserInfo">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 修改密码卡片 -->
    <el-card class="profile-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">修改密码</h3>
        </div>
      </template>
      
      <el-form :model="passwordForm" label-width="120px" class="password-form">
        <el-row :gutter="20">
          <el-col :xs="24" :md="12">
            <el-form-item label="原密码">
              <el-input v-model="passwordForm.oldPassword" type="password" />
            </el-form-item>
            
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" />
            </el-form-item>
            
            <el-form-item label="确认密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    
    <!-- 任务列表卡片 -->
    <el-card class="profile-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">任务列表</h3>
        </div>
      </template>
      
      <el-table :data="tasks" style="width: 100%" border stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="title" label="任务标题" min-width="200" />
        <el-table-column prop="deadline" label="截止日期" width="120" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'warning' : 'success'">
              {{ scope.row.status === 1 ? '未完成' : '已完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="任务描述" min-width="300" />
      </el-table>
    </el-card>
    
    <!-- 已提交文档卡片 -->
    <el-card class="profile-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">已提交文档</h3>
        </div>
      </template>
      
      <el-table :data="submittedDocuments" style="width: 100%" border stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="文档名称" min-width="200" />
        <el-table-column prop="type" label="文档类型" width="120" align="center" />
        <el-table-column prop="uploadTime" label="上传时间" width="180" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已审核' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small"
              @click="downloadDocument(scope.row.id, scope.row.name)"
            >
              <el-icon><Download /></el-icon>
              下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 系统通知卡片 -->
    <el-card class="profile-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">系统通知</h3>
          <el-badge :value="unreadNotificationCount" type="danger" />
        </div>
      </template>
      
      <el-empty v-if="notifications.length === 0" description="暂无系统通知" />
      <el-list v-else>
        <el-list-item
          v-for="notification in notifications"
          :key="notification.id"
          :class="{ 'unread': !notification.read }"
          @click="markAsRead(notification.id)"
        >
          <template #title>
            <div class="notification-title">
              <span>{{ notification.title }}</span>
              <span class="notification-time">{{ notification.time }}</span>
            </div>
          </template>
          <template #default>
            <div class="notification-content">{{ notification.content }}</div>
          </template>
        </el-list-item>
      </el-list>
    </el-card>
  </div>
</template>

<style scoped>
.profile-container {
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

.profile-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.profile-card:hover {
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
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

.profile-form {
  padding: 10px 0;
}

.password-form {
  padding: 10px 0;
}

.notification-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.notification-content {
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
</style>