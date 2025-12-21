<template>
  <div class="topic-select-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="选题管理"
    />

    <!-- 搜索筛选区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="导师姓名">
          <el-input
            v-model="searchForm.teacherName"
            placeholder="请输入导师姓名"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="研究方向">
          <el-select
            v-model="searchForm.researchDirection"
            placeholder="请选择研究方向"
            clearable
            filterable
          >
            <el-option
              v-for="direction in researchDirections"
              :key="direction.value"
              :label="direction.label"
              :value="direction.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="剩余名额">
          <el-select
            v-model="searchForm.remainingQuota"
            placeholder="请选择剩余名额"
            clearable
          >
            <el-option label="大于0" value="0" />
            <el-option label="大于3" value="3" />
            <el-option label="大于5" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 导师列表和志愿选择区域 -->
    <div class="main-content">
      <!-- 导师列表 -->
      <el-card class="teacher-list-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>导师列表</span>
            <el-tag type="info" size="small">{{ filteredTeachers.length }}位导师</el-tag>
          </div>
        </template>
        <div class="teacher-grid">
          <el-card
            v-for="teacher in filteredTeachers"
            :key="teacher.id"
            class="teacher-card"
            shadow="hover"
            :class="{ 'selected': isSelected(teacher.id) }"
          >
            <div class="teacher-header">
              <el-avatar :size="60" :src="teacher.avatar || defaultAvatar" />
              <div class="teacher-info">
                <h3 class="teacher-name">{{ teacher.name }}</h3>
                <div class="teacher-title">{{ teacher.title }}</div>
                <div class="teacher-department">{{ teacher.department }}</div>
              </div>
              <el-tag
                :type="teacher.remainingQuota > 5 ? 'success' : teacher.remainingQuota > 0 ? 'warning' : 'danger'"
                size="small"
              >
                剩余{{ teacher.remainingQuota }}个名额
              </el-tag>
            </div>
            
            <div class="teacher-details">
              <el-descriptions :column="1" :border="false" size="small">
                <el-descriptions-item label="研究方向">
                  <el-tag
                    v-for="direction in teacher.researchDirections"
                    :key="direction"
                    size="small"
                    type="info"
                    effect="light"
                  >
                    {{ direction }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="联系方式">{{ teacher.contactInfo }}</el-descriptions-item>
                <el-descriptions-item label="邮箱">{{ teacher.email }}</el-descriptions-item>
                <el-descriptions-item label="简介">
                  <div class="teacher-bio" :title="teacher.bio">{{ teacher.bio }}</div>
                </el-descriptions-item>
              </el-descriptions>
            </div>
            
            <div class="teacher-actions">
              <el-button
                :type="isSelected(teacher.id) ? 'warning' : 'primary'"
                size="small"
                @click="handleSelectTeacher(teacher)"
              >
                {{ isSelected(teacher.id) ? '取消选择' : '选择导师' }}
              </el-button>
              <el-button size="small" @click="handleViewTopics(teacher.id)">
                查看选题
              </el-button>
            </div>
          </el-card>
        </div>
      </el-card>

      <!-- 志愿选择 -->
      <el-card class="volunteer-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>我的志愿</span>
            <el-tag type="primary" size="small">{{ selectedVolunteers.length }}/5个志愿</el-tag>
          </div>
        </template>
        
        <el-empty v-if="selectedVolunteers.length === 0" description="请从左侧选择导师" />
        
        <div v-else class="volunteer-list">
          <el-alert
            title="提示：请拖拽调整志愿顺序，第一志愿优先级最高"
            type="info"
            :closable="false"
            show-icon
            size="small"
            class="volunteer-alert"
          />
          
          <draggable
            v-model="selectedVolunteers"
            :animation="150"
            handle=".drag-handle"
            ghost-class="ghost"
          >
            <template #item="{ element, index }">
              <div class="volunteer-item">
                <div class="drag-handle">
                  <el-icon><arrow-up-down /></el-icon>
                </div>
                <div class="volunteer-info">
                  <div class="volunteer-rank">{{ index + 1 }}</div>
                  <div class="volunteer-teacher-info">
                    <div class="volunteer-teacher-name">{{ element.name }}</div>
                    <div class="volunteer-teacher-title">{{ element.title }} | {{ element.department }}</div>
                    <div class="volunteer-teacher-direction">
                      <el-tag
                        v-for="direction in element.researchDirections"
                        :key="direction"
                        size="mini"
                        type="info"
                        effect="light"
                      >
                        {{ direction }}
                      </el-tag>
                    </div>
                  </div>
                </div>
                <el-button
                  type="danger"
                  size="small"
                  icon="Delete"
                  circle
                  @click="handleRemoveVolunteer(element.id)"
                />
              </div>
            </template>
          </draggable>
          
          <div class="volunteer-footer">
            <el-button type="primary" size="small" @click="handleSubmitVolunteers" :disabled="selectedVolunteers.length === 0">
              提交志愿
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import draggable from 'vuedraggable'
import { ArrowUpDown, Delete } from '@element-plus/icons-vue'

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 研究方向选项
const researchDirections = [
  { label: '金融市场', value: '金融市场' },
  { label: '金融风险', value: '金融风险' },
  { label: '金融科技', value: '金融科技' },
  { label: '国际金融', value: '国际金融' },
  { label: '公司金融', value: '公司金融' },
  { label: '金融工程', value: '金融工程' },
  { label: '保险学', value: '保险学' },
  { label: '投资学', value: '投资学' }
]

// 搜索表单
const searchForm = ref({
  teacherName: '',
  researchDirection: '',
  remainingQuota: ''
})

// 导师列表数据
const teachers = ref([
  {
    id: 1,
    name: '张三',
    title: '教授',
    department: '金融学院',
    avatar: '',
    researchDirections: ['金融市场', '金融风险'],
    remainingQuota: 8,
    contactInfo: '13800138001',
    email: 'zhangsan@hfuu.edu.cn',
    bio: '张三教授，金融学博士，主要研究方向为金融市场和金融风险，发表论文50余篇，主持国家级项目3项。'
  },
  {
    id: 2,
    name: '李四',
    title: '副教授',
    department: '金融学院',
    avatar: '',
    researchDirections: ['金融科技', '金融工程'],
    remainingQuota: 3,
    contactInfo: '13800138002',
    email: 'lisi@hfuu.edu.cn',
    bio: '李四副教授，金融学硕士，主要研究方向为金融科技和金融工程，发表论文30余篇，主持省部级项目2项。'
  },
  {
    id: 3,
    name: '王五',
    title: '讲师',
    department: '金融学院',
    avatar: '',
    researchDirections: ['国际金融', '投资学'],
    remainingQuota: 5,
    contactInfo: '13800138003',
    email: 'wangwu@hfuu.edu.cn',
    bio: '王五讲师，金融学博士，主要研究方向为国际金融和投资学，发表论文20余篇。'
  },
  {
    id: 4,
    name: '赵六',
    title: '教授',
    department: '金融学院',
    avatar: '',
    researchDirections: ['公司金融', '保险学'],
    remainingQuota: 1,
    contactInfo: '13800138004',
    email: 'zhaoliu@hfuu.edu.cn',
    bio: '赵六教授，金融学博士，主要研究方向为公司金融和保险学，发表论文40余篇，主持国家级项目2项。'
  },
  {
    id: 5,
    name: '孙七',
    title: '副教授',
    department: '金融学院',
    avatar: '',
    researchDirections: ['金融市场', '投资学'],
    remainingQuota: 6,
    contactInfo: '13800138005',
    email: 'sunqi@hfuu.edu.cn',
    bio: '孙七副教授，金融学硕士，主要研究方向为金融市场和投资学，发表论文25余篇。'
  },
  {
    id: 6,
    name: '周八',
    title: '讲师',
    department: '金融学院',
    avatar: '',
    researchDirections: ['金融科技', '国际金融'],
    remainingQuota: 0,
    contactInfo: '13800138006',
    email: 'zhouba@hfuu.edu.cn',
    bio: '周八讲师，金融学博士，主要研究方向为金融科技和国际金融，发表论文15余篇。'
  }
])

// 筛选后的导师列表
const filteredTeachers = computed(() => {
  return teachers.value.filter(teacher => {
    // 姓名筛选
    if (searchForm.value.teacherName && !teacher.name.includes(searchForm.value.teacherName)) {
      return false
    }
    // 研究方向筛选
    if (searchForm.value.researchDirection && !teacher.researchDirections.includes(searchForm.value.researchDirection)) {
      return false
    }
    // 剩余名额筛选
    if (searchForm.value.remainingQuota && teacher.remainingQuota <= Number(searchForm.value.remainingQuota)) {
      return false
    }
    return true
  })
})

// 已选择的志愿列表
const selectedVolunteers = ref([])

// 检查导师是否已被选择
const isSelected = (teacherId: number) => {
  return selectedVolunteers.value.some(volunteer => volunteer.id === teacherId)
}

// 处理导师选择
const handleSelectTeacher = (teacher: any) => {
  if (isSelected(teacher.id)) {
    // 取消选择
    selectedVolunteers.value = selectedVolunteers.value.filter(volunteer => volunteer.id !== teacher.id)
  } else {
    // 选择导师，最多5个志愿
    if (selectedVolunteers.value.length >= 5) {
      ElMessage.warning('最多只能选择5个志愿')
      return
    }
    selectedVolunteers.value.push({
      id: teacher.id,
      name: teacher.name,
      title: teacher.title,
      department: teacher.department,
      researchDirections: teacher.researchDirections
    })
  }
}

// 处理搜索
const handleSearch = () => {
  console.log('搜索', searchForm.value)
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    teacherName: '',
    researchDirection: '',
    remainingQuota: ''
  }
}

// 查看导师选题
const handleViewTopics = (teacherId: number) => {
  console.log('查看导师选题', teacherId)
  ElMessage.info('查看导师选题功能开发中')
}

// 删除志愿
const handleRemoveVolunteer = (teacherId: number) => {
  selectedVolunteers.value = selectedVolunteers.value.filter(volunteer => volunteer.id !== teacherId)
}

// 提交志愿
const handleSubmitVolunteers = () => {
  if (selectedVolunteers.value.length === 0) {
    ElMessage.warning('请至少选择一个志愿')
    return
  }
  console.log('提交志愿', selectedVolunteers.value)
  ElMessage.success('志愿提交成功')
}

// 页面返回处理
const handleBack = () => {
  console.log('返回')
}
</script>

<style scoped>
.topic-select-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.search-card {
  margin-bottom: 20px;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 20px;
}

@media (max-width: 1200px) {
  .main-content {
    grid-template-columns: 1fr;
  }
}

.teacher-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.teacher-card {
  transition: all 0.3s ease;
}

.teacher-card.selected {
  border: 2px solid #3b82f6;
  box-shadow: 0 0 10px rgba(59, 130, 246, 0.3);
}

.teacher-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.teacher-info {
  flex: 1;
}

.teacher-name {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1e3a8a;
}

.teacher-title {
  font-size: 14px;
  color: #6b7280;
  margin: 4px 0;
}

.teacher-department {
  font-size: 12px;
  color: #9ca3af;
}

.teacher-details {
  margin-bottom: 16px;
}

.teacher-bio {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-size: 13px;
  color: #6b7280;
}

.teacher-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.volunteer-list {
  padding: 10px 0;
}

.volunteer-alert {
  margin-bottom: 16px;
}

.volunteer-item {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.volunteer-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.drag-handle {
  cursor: move;
  padding: 8px;
  color: #9ca3af;
  margin-right: 8px;
}

.drag-handle:hover {
  color: #3b82f6;
}

.volunteer-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
}

.volunteer-rank {
  width: 24px;
  height: 24px;
  background-color: #3b82f6;
  color: white;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  font-weight: bold;
}

.volunteer-teacher-info {
  flex: 1;
}

.volunteer-teacher-name {
  font-size: 14px;
  font-weight: 500;
  color: #1e3a8a;
}

.volunteer-teacher-title {
  font-size: 12px;
  color: #6b7280;
  margin: 2px 0;
}

.volunteer-teacher-direction {
  margin-top: 4px;
}

.volunteer-teacher-direction .el-tag {
  margin-right: 4px;
}

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.volunteer-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>