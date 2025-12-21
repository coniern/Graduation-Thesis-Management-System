<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 用户列表
const users = ref([
  { id: 1, username: 'student', realName: '张三', userType: 1, department: '金融学院', major: '金融学', status: '启用' },
  { id: 2, username: 'mentor', realName: '李四', userType: 2, department: '金融学院', major: '', status: '启用' },
  { id: 3, username: 'admin', realName: '王五', userType: 4, department: '教务处', major: '', status: '启用' },
  { id: 4, username: 'student2', realName: '赵六', userType: 1, department: '金融学院', major: '金融学', status: '禁用' }
])

// 用户类型映射
const userTypeMap = {
  1: '学生',
  2: '导师',
  3: '院系管理员',
  4: '校级管理员'
}

// 状态映射
const statusMap = {
  '启用': 'success',
  '禁用': 'danger'
}

// 查看用户详情
const viewUser = (user) => {
  // 模拟查看用户详情
  console.log('查看用户详情:', user)
}

// 切换用户状态
const toggleStatus = (user) => {
  // 模拟切换用户状态
  user.status = user.status === '启用' ? '禁用' : '启用'
}
</script>

<template>
  <div class="user-manage-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>用户管理</h1>
      <p>管理系统用户</p>
    </div>
    
    <!-- 操作按钮 -->
    <div class="action-section">
      <el-button type="primary">
        <el-icon><Plus /></el-icon>
        新增用户
      </el-button>
    </div>
    
    <!-- 用户列表 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">用户列表</h3>
        </div>
      </template>
      
      <el-table :data="users" style="width: 100%" border>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="username" label="用户名" width="120" align="center" />
        <el-table-column prop="realName" label="真实姓名" width="120" align="center" />
        <el-table-column prop="userType" label="用户类型" width="120" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.userType === 1 ? 'info' : scope.row.userType === 2 ? 'warning' : 'success'">
              {{ userTypeMap[scope.row.userType] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="所属部门" width="150" align="center" />
        <el-table-column prop="major" label="专业" width="120" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status]">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewUser(scope.row)" style="margin-right: 5px;">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button :type="scope.row.status === '启用' ? 'warning' : 'success'" size="small" @click="toggleStatus(scope.row)">
              <el-icon><Switch /></el-icon>
              {{ scope.row.status === '启用' ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.user-manage-container {
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
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
</style>