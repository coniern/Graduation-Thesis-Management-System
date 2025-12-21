<script setup>
import { ref, onMounted } from 'vue'
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

// 加载用户信息
const loadUserInfo = async () => {
  // 从localStorage获取用户信息
  const userStr = localStorage.getItem('userInfo')
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

// 保存用户信息
const saveUserInfo = async () => {
  try {
    // 模拟保存用户信息
    await new Promise(resolve => setTimeout(resolve, 500))
    // 更新localStorage中的用户信息
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 页面挂载时加载用户信息
onMounted(() => {
  loadUserInfo()
})
</script>

<template>
  <div class="profile-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>个人资料</h1>
      <p>管理您的个人信息</p>
    </div>
    
    <!-- 个人资料卡片 -->
    <el-card class="profile-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">基本信息</h3>
        </div>
      </template>
      
      <el-form ref="profileFormRef" :model="userInfo" label-width="120px" class="profile-form">
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
      
      <el-form ref="passwordFormRef" :model="passwordForm" label-width="120px" class="password-form">
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
</style>