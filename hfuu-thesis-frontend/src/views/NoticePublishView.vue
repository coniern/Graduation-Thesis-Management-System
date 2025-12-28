<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../utils/axios'

const router = useRouter()
const route = useRoute()

// 表单数据
const noticeForm = reactive({
  title: '',
  content: '',
  noticeType: 2,
  startTime: '',
  endTime: '',
  status: 1
})

// 表单引用
const noticeFormRef = ref()

// 加载状态
const loading = ref(false)

// 通知类型选项
const noticeTypeOptions = [
  { label: '系统通知', value: 1 },
  { label: '公告', value: 2 },
  { label: '消息推送', value: 3 }
]

// 状态选项
const statusOptions = [
  { label: '未发布', value: 1 },
  { label: '已发布', value: 2 },
  { label: '已过期', value: 3 }
]

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入通知标题', trigger: 'blur' },
    { min: 5, max: 100, message: '标题长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入通知内容', trigger: 'blur' }
  ],
  noticeType: [
    { required: true, message: '请选择通知类型', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (value && noticeForm.startTime && new Date(value) < new Date(noticeForm.startTime)) {
          callback(new Error('结束时间不能早于开始时间'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 保存通知
const saveNotice = async () => {
  if (!noticeFormRef.value) return
  
  noticeFormRef.value.validate(async (valid) => {
    if (!valid) {
      return false
    }
    
    loading.value = true
    try {
      // 调用保存接口
      await axios.post('/api/notices', noticeForm)
      ElMessage.success('保存成功')
      router.push('/notice/list')
    } catch (error) {
      ElMessage.error('保存失败')
    } finally {
      loading.value = false
    }
  })
}

// 返回列表
const handleBack = () => {
  router.push('/notice/list')
}
</script>

<template>
  <div class="notice-publish-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>发布通知</h1>
      <div class="header-actions">
        <el-button @click="handleBack">返回</el-button>
        <el-button type="primary" @click="saveNotice" :loading="loading">保存</el-button>
      </div>
    </div>
    
    <!-- 表单卡片 -->
    <el-card class="form-card" shadow="hover">
      <el-form ref="noticeFormRef" :model="noticeForm" :rules="rules" label-width="120px">
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="noticeForm.title" placeholder="请输入通知标题" maxlength="100" show-word-limit />
        </el-form-item>
        
        <el-form-item label="通知类型" prop="noticeType">
          <el-select v-model="noticeForm.noticeType" placeholder="请选择通知类型">
            <el-option v-for="option in noticeTypeOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="noticeForm.status" placeholder="请选择状态">
            <el-option v-for="option in statusOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="有效时间" prop="startTime">
          <el-date-picker
            v-model="noticeForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 49%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
          <span style="margin: 0 10px;">至</span>
          <el-date-picker
            v-model="noticeForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 49%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        
        <el-form-item label="通知内容" prop="content">
          <el-input
            v-model="noticeForm.content"
            type="textarea"
            placeholder="请输入通知内容"
            rows="15"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item>
          <div class="form-actions">
            <el-button @click="handleBack">取消</el-button>
            <el-button type="primary" @click="saveNotice" :loading="loading">保存</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.notice-publish-container {
  min-height: 100%;
  background-color: #f5f7fa;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.form-card {
  border-radius: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>