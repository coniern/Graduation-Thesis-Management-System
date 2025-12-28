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
      component: () => import('./views/HomeView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresAuth: false }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('./views/ProfileView.vue'),
      meta: { requiresAuth: true }
    },
    // 学生主页
    {
      path: '/student/home',
      name: 'studentHome',
      component: () => import('./views/student/StudentHomeView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    // 学生功能
    {
      path: '/topic/apply',
      name: 'topicApply',
      component: () => import('./views/student/TopicApplyView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    {
      path: '/teacher/selection',
      name: 'teacherSelection',
      component: () => import('./views/student/TeacherSelection.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    {
      path: '/document/upload',
      name: 'documentUpload',
      component: () => import('./views/student/DocumentUploadView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    {
      path: '/progress',
      name: 'progress',
      component: () => import('./views/student/ProgressView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    {
      path: '/evaluation',
      name: 'evaluation',
      component: () => import('./views/student/EvaluationView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    {
      path: '/communication',
      name: 'communication',
      component: () => import('./views/student/CommunicationView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    {
      path: '/defense',
      name: 'defense',
      component: () => import('./views/student/DefenseView.vue'),
      meta: { requiresAuth: true, roles: [1] }
    },
    // 公共功能
    {
      path: '/template-library',
      name: 'templateLibrary',
      component: () => import('./views/TemplateLibrary.vue'),
      meta: { requiresAuth: true }
    },
    // 教师主页
    {
      path: '/teacher/home',
      name: 'teacherHome',
      component: () => import('./views/teacher/TeacherHomeView.vue'),
      meta: { requiresAuth: true, roles: [2] }
    },
    // 教师功能
    {
      path: '/topic/manage',
      name: 'topicManage',
      component: () => import('./views/teacher/TopicManageView.vue'),
      meta: { requiresAuth: true, roles: [2] }
    },
    {
      path: '/student/manage',
      name: 'studentManage',
      component: () => import('./views/teacher/StudentManageView.vue'),
      meta: { requiresAuth: true, roles: [2] }
    },
    {
      path: '/student/selection',
      name: 'studentSelection',
      component: () => import('./views/teacher/StudentSelection.vue'),
      meta: { requiresAuth: true, roles: [2] }
    },
    {
      path: '/document/review',
      name: 'documentReview',
      component: () => import('./views/teacher/DocumentReviewView.vue'),
      meta: { requiresAuth: true, roles: [2] }
    },
    {
      path: '/evaluation/give',
      name: 'evaluationGive',
      component: () => import('./views/teacher/EvaluationGiveView.vue'),
      meta: { requiresAuth: true, roles: [2] }
    },
    // 管理员主页
    {
      path: '/admin/home',
      name: 'adminHome',
      component: () => import('./views/admin/AdminHomeView.vue'),
      meta: { requiresAuth: true, roles: [3, 4] }
    },
    // 管理员功能
    {
      path: '/user/manage',
      name: 'userManage',
      component: () => import('./views/admin/UserManageView.vue'),
      meta: { requiresAuth: true, roles: [3, 4] }
    },
    {
      path: '/department/manage',
      name: 'departmentManage',
      component: () => import('./views/admin/DepartmentManageView.vue'),
      meta: { requiresAuth: true, roles: [3, 4] }
    },
    {
      path: '/system/config',
      name: 'systemConfig',
      component: () => import('./views/admin/SystemConfigView.vue'),
      meta: { requiresAuth: true, roles: [4] }
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: () => import('./views/admin/StatisticsView.vue'),
      meta: { requiresAuth: true, roles: [3, 4] }
    }
  ]
})

// 添加路由守卫
router.beforeEach((to, from, next) => {
  // 检查路由是否需要登录
  if (to.meta.requiresAuth && to.meta.requiresAuth === true) {
    // 检查是否已登录，确保localStorage存在
    const token = typeof localStorage !== 'undefined' ? localStorage.getItem('token') : ''
    if (!token) {
      // 未登录，重定向到登录页
      next('/login')
      return
    }
    
    // 检查角色权限
    if (to.meta.roles && Array.isArray(to.meta.roles)) {
      // 获取当前用户角色
      const userInfoStr = typeof localStorage !== 'undefined' ? localStorage.getItem('userInfo') : ''
      let userRole = 0
      if (userInfoStr) {
        const userInfo = JSON.parse(userInfoStr)
        userRole = userInfo.userType || 0
      }
      
      // 检查用户角色是否在允许的角色列表中
      if (!to.meta.roles.includes(userRole)) {
        // 没有权限，重定向到对应角色的主页
        let redirectPath = '/'
        switch (userRole) {
          case 1: // 学生
            redirectPath = '/student/home'
            break
          case 2: // 教师
            redirectPath = '/teacher/home'
            break
          case 3: // 院系管理员
          case 4: // 校级管理员
            redirectPath = '/admin/home'
            break
          default:
            redirectPath = '/login'
        }
        next(redirectPath)
        return
      }
    }
  }
  // 已登录或不需要登录，继续访问
  next()
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
