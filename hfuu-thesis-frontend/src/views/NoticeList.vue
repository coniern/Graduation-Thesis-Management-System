<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../utils/axios'

const router = useRouter()

// 通知列表数据
const notices = ref([])
// 加载状态
const loading = ref(false)
// 查询参数
const queryParams = ref({
  noticeType: undefined,
  status: undefined,
  title: ''
})
// 分页参数
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

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

// 加载通知列表
const loadNotices = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/notices', {
      params: {
        noticeType: queryParams.value.noticeType,
        status: queryParams.value.status,
        title: queryParams.value.title,
        page: pagination.value.currentPage,
        pageSize: pagination.value.pageSize
      }
    })
    notices.value = response
    pagination.value.total = response.length
  } catch (error) {
    ElMessage.error('获取通知列表失败')
  } finally {
    loading.value = false
  }
}

// 查询通知
const handleSearch = () => {
  pagination.value.currentPage = 1
  loadNotices()
}

// 重置查询
const handleReset = () => {
  queryParams.value = {
    noticeType: undefined,
    status: undefined,
    title: ''
  }
  pagination.value.currentPage = 1
  loadNotices()
}

// 查看详情
const handleView = (notice) => {
  router.push(`/notice/${notice.noticeId}`)
}

// 发布通知
const handlePublish = () => {
  router.push('/notice/publish')
}

// 删除通知
const handleDelete = (noticeId) => {
  ElMessageBox.confirm('确定要删除这条通知吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/notices/${noticeId}`)
      ElMessage.success('删除成功')
      loadNotices()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadNotices()
})
</script>

<template>
  <div class="notice-list-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>通知公告管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handlePublish">发布通知</el-button>
      </div>
    </div>
    
    <!-- 查询表单 -->
    <el-card class="search-card" shadow="hover">
      <el-form :model="queryParams" layout="inline" size="small">
        <el-form-item label="通知类型">
          <el-select v-model="queryParams.noticeType" placeholder="请选择通知类型" clearable>
            <el-option v-for="option in noticeTypeOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option v-for="option in statusOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 通知列表 -->
    <el-card class="notice-list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h3 class="card-title">通知列表</h3>
        </div>
      </template>
      <el-table :data="notices" style="width: 100%" :loading="loading">
        <el-table-column prop="noticeId" label="通知ID" width="100" />
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="scope">
            <a @click="handleView(scope.row)">{{ scope.row.title }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="noticeType" label="类型" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.noticeType === 1 ? 'info' : scope.row.noticeType === 2 ? 'warning' : 'success'">
              {{ scope.row.noticeType === 1 ? '系统通知' : scope.row.noticeType === 2 ? '公告' : '消息推送' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'info' : scope.row.status === 2 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '未发布' : scope.row.status === 2 ? '已发布' : '已过期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="clickCount" label="点击量" width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.noticeId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="loadNotices"
          @current-change="loadNotices"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.notice-list-container {
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

.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.notice-list-card {
  border-radius: 8px;
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

.pagination-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}
</style>