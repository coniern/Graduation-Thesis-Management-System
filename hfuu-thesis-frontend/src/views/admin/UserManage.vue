<template>
  <div class="user-manage-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="用户管理"
    />

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 第一行：搜索和筛选 -->
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="searchForm" label-width="80px">
          <el-form-item label="用户类型">
            <el-select v-model="searchForm.userType" placeholder="全部" clearable>
              <el-option label="全部" value="" />
              <el-option label="学生" value="student" />
              <el-option label="导师" value="teacher" />
              <el-option label="管理员" value="admin" />
            </el-select>
          </el-form-item>

          <el-form-item label="院系">
            <el-select v-model="searchForm.department" placeholder="全部" clearable>
              <el-option label="全部" value="" />
              <el-option label="金融学院" value="金融学院" />
              <el-option label="经济学院" value="经济学院" />
              <el-option label="管理学院" value="管理学院" />
              <el-option label="会计学院" value="会计学院" />
              <el-option label="统计学院" value="统计学院" />
            </el-select>
          </el-form-item>

          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="全部" clearable>
              <el-option label="全部" value="" />
              <el-option label="启用" value="enabled" />
              <el-option label="禁用" value="disabled" />
            </el-select>
          </el-form-item>

          <el-form-item label="搜索">
            <el-input
              v-model="searchForm.keyword"
              placeholder="姓名/学号/工号"
              clearable
              @keyup.enter="handleSearch"
              prefix-icon="Search"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button type="success" @click="handleAddUser">新增用户</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 第二行：用户列表 -->
      <el-card class="user-list-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>用户列表</span>
            <el-tag type="info" size="small">{{ filteredUsers.length }}名用户</el-tag>
          </div>
        </template>

        <!-- 批量操作 -->
        <div class="batch-operations">
          <el-button
            type="primary"
            size="small"
            :disabled="selectedUsers.length === 0"
            @click="handleBatchAssignRole"
          >
            批量分配角色
          </el-button>
          <el-button
            type="success"
            size="small"
            :disabled="selectedUsers.length === 0"
            @click="handleBatchEnable"
          >
            批量启用
          </el-button>
          <el-button
            type="warning"
            size="small"
            :disabled="selectedUsers.length === 0"
            @click="handleBatchDisable"
          >
            批量禁用
          </el-button>
          <el-button
            type="danger"
            size="small"
            :disabled="selectedUsers.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>

        <!-- 用户列表表格 -->
        <el-table
          :data="paginatedUsers"
          v-loading="loading"
          border
          stripe
          size="small"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="name" label="姓名" width="100">
            <template #default="scope">
              <div class="user-name">
                <el-avatar :size="24" :src="scope.row.avatar || defaultAvatar" />
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="userType" label="用户类型" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.userType === 'student' ? 'primary' : scope.row.userType === 'teacher' ? 'success' : 'warning'">
                {{ scope.row.userType === 'student' ? '学生' : scope.row.userType === 'teacher' ? '导师' : '管理员' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="department" label="院系" width="120" />
          <el-table-column prop="major" label="专业" width="120" />
          <el-table-column prop="className" label="班级" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="enabled"
                inactive-value="disabled"
                @change="handleStatusChange(scope.row)"
              >
                <template #active>
                  <span>启用</span>
                </template>
                <template #inactive>
                  <span>禁用</span>
                </template>
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleViewUser(scope.row)"
              >
                查看
              </el-button>
              <el-button
                type="info"
                size="small"
                @click="handleEditUser(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDeleteUser(scope.row.id)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredUsers.length"
            @size-change="handlePageSizeChange"
            @current-change="handleCurrentPageChange"
          />
        </div>
      </el-card>

      <!-- 第三行：导师指导学生数监控 -->
      <el-card class="teacher-monitor-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>导师指导学生数监控</span>
          </div>
        </template>

        <div class="monitor-container">
          <div ref="teacherMonitorChartRef" class="monitor-chart"></div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'
import { UserFilled, Search } from '@element-plus/icons-vue'

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 用户数据类型定义
interface User {
  id: number
  username: string
  name: string
  userType: 'student' | 'teacher' | 'admin'
  department: string
  major: string
  className?: string
  status: 'enabled' | 'disabled'
  avatar?: string
  createTime: string
}

// 搜索表单
const searchForm = ref({
  keyword: '',
  userType: '',
  department: '',
  status: ''
})

// 用户列表数据
const users = ref<User[]>([
  {
    id: 1,
    username: 'admin',
    name: '管理员',
    userType: 'admin',
    department: '系统管理',
    major: '',
    status: 'enabled',
    createTime: '2025-01-01 00:00:00'
  },
  {
    id: 2,
    username: 'teacher001',
    name: '张教授',
    userType: 'teacher',
    department: '金融学院',
    major: '金融学',
    status: 'enabled',
    createTime: '2025-01-02 10:30:00'
  },
  {
    id: 3,
    username: 'teacher002',
    name: '李副教授',
    userType: 'teacher',
    department: '金融学院',
    major: '金融工程',
    status: 'enabled',
    createTime: '2025-01-03 14:20:00'
  },
  {
    id: 4,
    username: 'student001',
    name: '张三',
    userType: 'student',
    department: '金融学院',
    major: '金融学',
    className: '金融2201',
    status: 'enabled',
    createTime: '2025-02-01 09:00:00'
  },
  {
    id: 5,
    username: 'student002',
    name: '李四',
    userType: 'student',
    department: '金融学院',
    major: '金融工程',
    className: '金融2202',
    status: 'enabled',
    createTime: '2025-02-02 10:15:00'
  },
  {
    id: 6,
    username: 'student003',
    name: '王五',
    userType: 'student',
    department: '经济学院',
    major: '经济学',
    className: '经济2201',
    status: 'enabled',
    createTime: '2025-02-03 11:30:00'
  },
  {
    id: 7,
    username: 'student004',
    name: '赵六',
    userType: 'student',
    department: '管理学院',
    major: '管理学',
    className: '管理2201',
    status: 'enabled',
    createTime: '2025-02-04 13:45:00'
  },
  {
    id: 8,
    username: 'student005',
    name: '孙七',
    userType: 'student',
    department: '会计学院',
    major: '会计学',
    className: '会计2201',
    status: 'enabled',
    createTime: '2025-02-05 15:20:00'
  },
  {
    id: 9,
    username: 'student006',
    name: '周八',
    userType: 'student',
    department: '统计学院',
    major: '统计学',
    className: '统计2201',
    status: 'enabled',
    createTime: '2025-02-06 09:30:00'
  },
  {
    id: 10,
    username: 'student007',
    name: '吴九',
    userType: 'student',
    department: '金融学院',
    major: '金融学',
    className: '金融2201',
    status: 'enabled',
    createTime: '2025-02-07 10:45:00'
  }
])

// 导师指导学生数数据
const teacherStudentCount = ref([
  { name: '张教授', value: 15 },
  { name: '李副教授', value: 12 },
  { name: '王讲师', value: 10 },
  { name: '刘教授', value: 18 },
  { name: '陈副教授', value: 9 },
  { name: '杨讲师', value: 11 },
  { name: '赵教授', value: 16 },
  { name: '黄副教授', value: 13 }
])

// 加载状态
const loading = ref(false)
// 选择的用户
const selectedUsers = ref<User[]>([])
// 分页
const currentPage = ref(1)
const pageSize = ref(10)

// 图表引用
const teacherMonitorChartRef = ref<HTMLElement>()
let teacherMonitorChart: echarts.ECharts | null = null

// 筛选后的用户列表
const filteredUsers = computed(() => {
  return users.value.filter(user => {
    // 关键词筛选
    if (searchForm.value.keyword) {
      const keyword = searchForm.value.keyword.toLowerCase()
      if (!user.name.toLowerCase().includes(keyword) && !user.username.toLowerCase().includes(keyword)) {
        return false
      }
    }
    // 用户类型筛选
    if (searchForm.value.userType && user.userType !== searchForm.value.userType) {
      return false
    }
    // 院系筛选
    if (searchForm.value.department && user.department !== searchForm.value.department) {
      return false
    }
    // 状态筛选
    if (searchForm.value.status && user.status !== searchForm.value.status) {
      return false
    }
    return true
  })
})

// 分页后的用户列表
const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredUsers.value.slice(start, end)
})

// 搜索
const handleSearch = () => {
  console.log('搜索用户', searchForm.value)
  currentPage.value = 1
}

// 重置
const handleReset = () => {
  searchForm.value = {
    keyword: '',
    userType: '',
    department: '',
    status: ''
  }
  currentPage.value = 1
}

// 新增用户
const handleAddUser = () => {
  console.log('新增用户')
  ElMessage.info('新增用户功能开发中')
}

// 查看用户
const handleViewUser = (user: User) => {
  console.log('查看用户', user)
  ElMessage.info('查看用户功能开发中')
}

// 编辑用户
const handleEditUser = (user: User) => {
  console.log('编辑用户', user)
  ElMessage.info('编辑用户功能开发中')
}

// 删除用户
const handleDeleteUser = (userId: number) => {
  ElMessageBox.confirm('确定要删除这个用户吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    users.value = users.value.filter(user => user.id !== userId)
    ElMessage.success('用户已删除')
  }).catch(() => {
    // 取消删除
  })
}

// 状态变更
const handleStatusChange = (user: User) => {
  console.log('状态变更', user)
  ElMessage.success(`用户${user.status === 'enabled' ? '已启用' : '已禁用'}`)
}

// 批量操作
const handleSelectionChange = (selection: User[]) => {
  selectedUsers.value = selection
}

// 批量分配角色
const handleBatchAssignRole = () => {
  console.log('批量分配角色', selectedUsers.value)
  ElMessage.info('批量分配角色功能开发中')
}

// 批量启用
const handleBatchEnable = () => {
  selectedUsers.value.forEach(user => {
    user.status = 'enabled'
  })
  ElMessage.success(`已启用${selectedUsers.value.length}名用户`)
  selectedUsers.value = []
}

// 批量禁用
const handleBatchDisable = () => {
  selectedUsers.value.forEach(user => {
    user.status = 'disabled'
  })
  ElMessage.success(`已禁用${selectedUsers.value.length}名用户`)
  selectedUsers.value = []
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的${selectedUsers.value.length}名用户吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const ids = selectedUsers.value.map(user => user.id)
    users.value = users.value.filter(user => !ids.includes(user.id))
    ElMessage.success(`已删除${selectedUsers.value.length}名用户`)
    selectedUsers.value = []
  }).catch(() => {
    // 取消删除
  })
}

// 分页大小变化
const handlePageSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 1
}

// 当前页变化
const handleCurrentPageChange = (newPage: number) => {
  currentPage.value = newPage
}

// 初始化导师指导学生数图表
const initTeacherMonitorChart = () => {
  if (!teacherMonitorChartRef.value) return
  teacherMonitorChart = echarts.init(teacherMonitorChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: teacherStudentCount.value.map(item => item.name),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '指导学生数',
      min: 0,
      max: 20,
      interval: 5
    },
    series: [
      {
        name: '指导学生数',
        type: 'bar',
        data: teacherStudentCount.value.map(item => item.value),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#1e3a8a' },
            { offset: 1, color: '#3b82f6' }
          ])
        },
        label: {
          show: true,
          position: 'top'
        },
        markLine: {
          data: [
            {
              name: '平均指导数',
              yAxis: Math.round(teacherStudentCount.value.reduce((sum, item) => sum + item.value, 0) / teacherStudentCount.value.length),
              lineStyle: {
                color: '#f56c6c',
                type: 'dashed'
              },
              label: {
                formatter: '平均: {c}',
                position: 'end'
              }
            }
          ]
        }
      }
    ]
  }
  
  teacherMonitorChart.setOption(option)
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
  // 这里可以添加返回上一页的逻辑
}

// 组件挂载时初始化图表
onMounted(() => {
  initTeacherMonitorChart()
  
  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    teacherMonitorChart?.resize()
  })
})
</script>

<style scoped>
.user-manage-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
}

/* 用户列表卡片 */
.user-list-card {
  margin-bottom: 20px;
}

.batch-operations {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #e2e8f0;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

/* 导师监控卡片 */
.teacher-monitor-card {
  margin-bottom: 20px;
}

.monitor-container {
  padding: 20px 0;
}

.monitor-chart {
  height: 400px;
  width: 100%;
}

/* 卡片样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>