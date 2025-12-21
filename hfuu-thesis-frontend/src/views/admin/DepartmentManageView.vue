<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 部门列表
const departments = ref([
  { id: 1, name: '金融学院', parentId: 0, description: '负责金融相关专业的教学与管理', status: '启用' },
  { id: 2, name: '会计学院', parentId: 0, description: '负责会计相关专业的教学与管理', status: '启用' },
  { id: 3, name: '经济学院', parentId: 0, description: '负责经济相关专业的教学与管理', status: '启用' },
  { id: 4, name: '信息管理学院', parentId: 0, description: '负责信息管理相关专业的教学与管理', status: '禁用' },
  { id: 5, name: '教务处', parentId: 0, description: '负责学校的教学管理工作', status: '启用' }
])

// 查看部门详情
const viewDepartment = (department) => {
  // 模拟查看部门详情
  console.log('查看部门详情:', department)
}

// 新增部门
const addDepartment = () => {
  // 模拟新增部门
  const newDept = {
    id: departments.value.length + 1,
    name: '新部门',
    parentId: 0,
    description: '新部门描述',
    status: '启用'
  }
  departments.value.push(newDept)
}

// 状态映射
const statusMap = {
  '启用': 'success',
  '禁用': 'danger'
}
</script>

<template>
  <div class="department-manage-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>部门管理</h1>
      <p>管理学校部门</p>
    </div>
    
    <!-- 操作按钮 -->
    <div class="action-section">
      <el-button type="primary" @click="addDepartment">
        <el-icon><Plus /></el-icon>
        新增部门
      </el-button>
    </div>
    
    <!-- 部门列表 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">部门列表</h3>
        </div>
      </template>
      
      <el-table :data="departments" style="width: 100%" border>
        <el-table-column prop="id" label="部门ID" width="100" align="center" />
        <el-table-column prop="name" label="部门名称" width="150" align="center" />
        <el-table-column prop="description" label="部门描述" min-width="300" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status]">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDepartment(scope.row)" style="margin-right: 5px;">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button type="warning" size="small">
              <el-icon><EditPen /></el-icon>
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.department-manage-container {
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