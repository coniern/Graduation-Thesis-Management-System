import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia } from 'pinia'
import './style.css'
import App from './App.vue'
import LoginView from './views/LoginView.vue'
import HomeView from './views/HomeView.vue'

// 创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('./views/ProfileView.vue')
    },
    // 学生功能
    {
      path: '/topic/apply',
      name: 'topicApply',
      component: () => import('./views/student/TopicApplyView.vue')
    },
    {
      path: '/document/upload',
      name: 'documentUpload',
      component: () => import('./views/student/DocumentUploadView.vue')
    },
    {
      path: '/progress',
      name: 'progress',
      component: () => import('./views/student/ProgressView.vue')
    },
    {
      path: '/evaluation',
      name: 'evaluation',
      component: () => import('./views/student/EvaluationView.vue')
    },
    // 教师功能
    {
      path: '/topic/manage',
      name: 'topicManage',
      component: () => import('./views/teacher/TopicManageView.vue')
    },
    {
      path: '/student/manage',
      name: 'studentManage',
      component: () => import('./views/teacher/StudentManageView.vue')
    },
    {
      path: '/document/review',
      name: 'documentReview',
      component: () => import('./views/teacher/DocumentReviewView.vue')
    },
    {
      path: '/evaluation/give',
      name: 'evaluationGive',
      component: () => import('./views/teacher/EvaluationGiveView.vue')
    },
    // 管理员功能
    {
      path: '/user/manage',
      name: 'userManage',
      component: () => import('./views/admin/UserManageView.vue')
    },
    {
      path: '/department/manage',
      name: 'departmentManage',
      component: () => import('./views/admin/DepartmentManageView.vue')
    },
    {
      path: '/system/config',
      name: 'systemConfig',
      component: () => import('./views/admin/SystemConfigView.vue')
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: () => import('./views/admin/StatisticsView.vue')
    }
  ]
})

// 创建Pinia
const pinia = createPinia()

// 创建应用
const app = createApp(App)

// 注册插件
app.use(ElementPlus)
app.use(router)
app.use(pinia)

// 挂载应用
app.mount('#app')
