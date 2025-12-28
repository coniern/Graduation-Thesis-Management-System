<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

// 模板列表
const templates = ref([])

// 加载状态
const loading = ref(false)

// 模板类型选项
const templateTypeOptions = [
  { label: '开题报告', value: 1 },
  { label: '中期报告', value: 2 },
  { label: '论文格式', value: 3 },
  { label: '答辩PPT', value: 4 },
  { label: '查重报告', value: 5 }
]

// 筛选条件
const filterForm = ref({
  templateType: undefined,
  keyword: ''
})

// 加载模板列表
const loadTemplates = async () => {
  loading.value = true
  try {
    // 实际应该调用后端API获取模板列表
    // const response = await axios.get('/api/templates', {
    //   params: {
    //     templateType: filterForm.value.templateType,
    //     keyword: filterForm.value.keyword
    //   }
    // })
    // templates.value = response.data
    
    // 模拟模板数据
    templates.value = [
      {
        id: 1,
        name: '哈尔滨金融学院毕业论文开题报告模板.docx',
        type: 1,
        description: '哈尔滨金融学院毕业论文开题报告标准模板',
        size: '1.2MB',
        downloadCount: 156,
        uploadTime: '2024-01-15',
        fileUrl: '#'
      },
      {
        id: 2,
        name: '哈尔滨金融学院毕业论文中期报告模板.docx',
        type: 2,
        description: '哈尔滨金融学院毕业论文中期报告标准模板',
        size: '980KB',
        downloadCount: 123,
        uploadTime: '2024-01-15',
        fileUrl: '#'
      },
      {
        id: 3,
        name: '哈尔滨金融学院毕业论文格式模板.docx',
        type: 3,
        description: '哈尔滨金融学院毕业论文格式标准模板',
        size: '1.5MB',
        downloadCount: 205,
        uploadTime: '2024-01-15',
        fileUrl: '#'
      },
      {
        id: 4,
        name: '哈尔滨金融学院毕业论文答辩PPT模板.pptx',
        type: 4,
        description: '哈尔滨金融学院毕业论文答辩PPT标准模板',
        size: '2.8MB',
        downloadCount: 89,
        uploadTime: '2024-01-15',
        fileUrl: '#'
      },
      {
        id: 5,
        name: '哈尔滨金融学院毕业论文查重报告模板.docx',
        type: 5,
        description: '哈尔滨金融学院毕业论文查重报告标准模板',
        size: '750KB',
        downloadCount: 67,
        uploadTime: '2024-01-15',
        fileUrl: '#'
      }
    ]
  } catch (error) {
    ElMessage.error('获取模板列表失败')
    console.error('获取模板列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 下载模板
const downloadTemplate = async (template) => {
  try {
    // 实际应该调用后端API下载模板
    // const response = await axios.get(`/api/templates/${template.id}/download`, {
    //   responseType: 'blob'
    // })
    
    ElMessage.success(`模板 ${template.name} 下载成功`)
    
    // 更新下载次数
    template.downloadCount += 1
  } catch (error) {
    ElMessage.error('模板下载失败')
    console.error('模板下载失败:', error)
  }
}

// 筛选模板
const filterTemplates = () => {
  loadTemplates()
}

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = {
    templateType: undefined,
    keyword: ''
  }
  loadTemplates()
}

// 页面挂载时加载数据
onMounted(() => {
  loadTemplates()
})
</script>

<template>
  <div class="template-library-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>文档模板库</h1>
      <p>提供各环节标准模板下载</p>
    </div>
    
    <!-- 筛选区域 -->
    <el-card class="filter-card" shadow="hover">
      <div class="filter-content">
        <el-form :model="filterForm" layout="inline">
          <el-form-item label="模板类型">
            <el-select v-model="filterForm.templateType" placeholder="请选择模板类型" clearable>
              <el-option 
                v-for="option in templateTypeOptions" 
                :key="option.value" 
                :label="option.label" 
                :value="option.value" 
              />
            </el-select>
          </el-form-item>
          <el-form-item label="关键词">
            <el-input 
              v-model="filterForm.keyword" 
              placeholder="请输入模板名称或描述" 
              clearable 
              style="width: 300px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="filterTemplates">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="resetFilter">
              <el-icon><RefreshRight /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    
    <!-- 模板列表 -->
    <el-card class="template-list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">模板列表</h3>
        </div>
      </template>
      
      <el-table :data="templates" style="width: 100%" :loading="loading" border stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="模板名称" min-width="300">
          <template #default="scope">
            <div class="template-name">
              <el-icon class="file-icon"><Document /></el-icon>
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="模板类型" width="120" align="center">
          <template #default="scope">
            <el-tag :type="'primary'">
              {{ templateTypeOptions.find(opt => opt.value === scope.row.type)?.label || '未知类型' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="300" />
        <el-table-column prop="size" label="大小" width="100" align="center" />
        <el-table-column prop="downloadCount" label="下载次数" width="120" align="center" />
        <el-table-column prop="uploadTime" label="上传时间" width="120" align="center" />
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small"
              @click="downloadTemplate(scope.row)"
            >
              <el-icon><Download /></el-icon>
              下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.template-library-container {
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

.filter-card {
  margin-bottom: 20px;
}

.filter-content {
  padding: 10px 0;
}

.template-list-card {
  margin-top: 20px;
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

.template-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-icon {
  color: #409eff;
}
</style>