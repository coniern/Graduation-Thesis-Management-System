<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const route = useRoute()
const router = useRouter()

// 通知详情数据
const notice = ref({
  noticeId: 0,
  title: '',
  content: '',
  publisherName: '',
  publishTime: '',
  startTime: '',
  endTime: '',
  noticeType: 0,
  status: 0,
  clickCount: 0
})
// 加载状态
const loading = ref(false)

// 通知类型映射
const noticeTypeMap = {
  1: '系统通知',
  2: '公告',
  3: '消息推送'
}

// 状态映射
const statusMap = {
  1: '未发布',
  2: '已发布',
  3: '已过期'
}

// 加载通知详情
const loadNoticeDetail = async () => {
  const noticeId = route.params.noticeId
  if (!noticeId) return
  
  loading.value = true
  try {
    const response = await axios.get(`/api/notices/${noticeId}`)
    notice.value = response
  } catch (error) {
    ElMessage.error('获取通知详情失败')
  } finally {
    loading.value = false
  }
}

// 返回列表
const handleBack = () => {
  router.back()
}

// 编辑通知
const handleEdit = () => {
  router.push(`/notice/edit/${notice.value.noticeId}`)
}

// 组件挂载时加载数据
onMounted(() => {
  loadNoticeDetail()
})
</script>

<template>
  <div class="notice-detail-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>通知详情</h1>
      <div class="header-actions">
        <el-button @click="handleBack">返回</el-button>
        <el-button type="primary" @click="handleEdit">编辑</el-button>
      </div>
    </div>
    
    <!-- 通知详情卡片 -->
    <el-card class="notice-detail-card" shadow="hover" :loading="loading">
      <div class="notice-header">
        <h2 class="notice-title">{{ notice.title }}</h2>
        <div class="notice-meta">
          <span class="meta-item">
            <el-tag :type="notice.noticeType === 1 ? 'info' : notice.noticeType === 2 ? 'warning' : 'success'">
              {{ noticeTypeMap[notice.noticeType] }}
            </el-tag>
          </span>
          <span class="meta-item">
            <el-tag :type="notice.status === 1 ? 'info' : notice.status === 2 ? 'success' : 'danger'">
              {{ statusMap[notice.status] }}
            </el-tag>
          </span>
          <span class="meta-item">发布人：{{ notice.publisherName }}</span>
          <span class="meta-item">发布时间：{{ notice.publishTime }}</span>
          <span class="meta-item">有效时间：{{ notice.startTime }} 至 {{ notice.endTime }}</span>
          <span class="meta-item">点击量：{{ notice.clickCount }}</span>
        </div>
      </div>
      <div class="notice-content" v-html="notice.content"></div>
    </el-card>
  </div>
</template>

<style scoped>
.notice-detail-container {
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

.notice-detail-card {
  border-radius: 8px;
}

.notice-header {
  margin-bottom: 20px;
}

.notice-title {
  margin: 0 0 15px;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
}

.notice-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  font-size: 14px;
  color: #606266;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f2f5;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.notice-content {
  font-size: 15px;
  line-height: 1.8;
  color: #303133;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.notice-content h3 {
  margin: 20px 0 10px;
  font-size: 18px;
  font-weight: 600;
}

.notice-content p {
  margin: 10px 0;
}

.notice-content ul, .notice-content ol {
  margin: 10px 0 10px 20px;
}

.notice-content li {
  margin: 5px 0;
}
</style>