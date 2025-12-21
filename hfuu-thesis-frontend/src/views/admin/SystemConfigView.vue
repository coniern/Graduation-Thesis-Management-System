<script setup>
import { ref } from 'vue'
import axios from '../../utils/axios'

// 系统配置项
const systemConfigs = ref([
  { id: 1, key: 'system.name', value: '哈尔滨金融学院毕业论文管理系统', description: '系统名称', type: 'text', status: '启用' },
  { id: 2, key: 'system.version', value: 'v1.0.0', description: '系统版本', type: 'text', status: '启用' },
  { id: 3, key: 'system.logo', value: '/logo.png', description: '系统Logo', type: 'file', status: '启用' },
  { id: 4, key: 'topic.registration.deadline', value: '2024-03-15', description: '选题截止日期', type: 'date', status: '启用' },
  { id: 5, key: 'defense.date', value: '2024-06-15', description: '答辩日期', type: 'date', status: '启用' }
])

// 保存配置
const saveConfig = () => {
  // 模拟保存配置
  console.log('保存系统配置')
}
</script>

<template>
  <div class="system-config-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>系统配置</h1>
      <p>管理系统配置项</p>
    </div>
    
    <!-- 操作按钮 -->
    <div class="action-section">
      <el-button type="primary" @click="saveConfig">
        <el-icon><Check /></el-icon>
        保存配置
      </el-button>
    </div>
    
    <!-- 配置列表 -->
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">系统配置项</h3>
        </div>
      </template>
      
      <el-form ref="configFormRef" :model="systemConfigs" label-width="180px" class="config-form">
        <el-row :gutter="20">
          <el-col :xs="24" :md="12" :lg="8" v-for="config in systemConfigs" :key="config.id">
            <el-form-item :label="config.description" :prop="config.key">
              <el-input
                v-if="config.type === 'text'"
                v-model="config.value"
                placeholder="请输入{{ config.description }}"
              />
              <el-date-picker
                v-else-if="config.type === 'date'"
                v-model="config.value"
                type="date"
                placeholder="选择{{ config.description }}"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
              <el-upload
                v-else-if="config.type === 'file'"
                class="upload-demo"
                action="#"
                :auto-upload="false"
                :show-file-list="false"
              >
                <el-button type="primary">
                  <el-icon><Upload /></el-icon>
                  上传{{ config.description }}
                </el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    
    <!-- 系统信息 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">系统信息</h3>
        </div>
      </template>
      
      <div class="system-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="系统名称">{{ systemConfigs.find(config => config.key === 'system.name')?.value }}</el-descriptions-item>
          <el-descriptions-item label="系统版本">{{ systemConfigs.find(config => config.key === 'system.version')?.value }}</el-descriptions-item>
          <el-descriptions-item label="JDK版本">17.0.10</el-descriptions-item>
          <el-descriptions-item label="Spring Boot版本">3.2.0</el-descriptions-item>
          <el-descriptions-item label="前端框架">Vue 3 + TypeScript</el-descriptions-item>
          <el-descriptions-item label="UI组件库">Element Plus</el-descriptions-item>
          <el-descriptions-item label="数据库">MySQL 8.0</el-descriptions-item>
          <el-descriptions-item label="部署环境">开发环境</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.system-config-container {
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

.config-form {
  padding: 10px 0;
}

.system-info {
  padding: 10px 0;
}
</style>