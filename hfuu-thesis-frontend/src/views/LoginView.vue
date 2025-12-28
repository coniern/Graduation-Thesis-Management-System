<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Reading, Right, User, Lock } from '@element-plus/icons-vue'
import axios from '../utils/axios'

const router = useRouter()

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 表单引用
const loginFormRef = ref()

// 加载状态
const loading = ref(false)

// 输入框焦点状态
const usernameFocus = ref(false)
const passwordFocus = ref(false)

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return

  // 表单验证
  loginFormRef.value.validate(async (valid) => {
    if (!valid) {
      return false
    }

    try {
      // 设置加载状态
      loading.value = true

      // 调用后端登录接口
      const response = await axios.post('/api/auth/login', loginForm)

      // 保存登录信息
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem('token', response.token)
        localStorage.setItem('userInfo', JSON.stringify(response.userInfo))
        localStorage.setItem('isLoggedIn', 'true')
      }

      // 提示登录成功
      ElMessage.success('登录成功')

      // 根据用户角色跳转到对应主页
      const userType = response.userInfo.userType
      let redirectPath = '/'
      
      // 根据角色设置重定向路径
      switch (userType) {
        case 1: // 学生
          redirectPath = '/student/home'
          break
        case 2: // 导师
          redirectPath = '/teacher/home'
          break
        case 3: // 院系管理员
        case 4: // 校级管理员
          redirectPath = '/admin/home'
          break
        default:
          redirectPath = '/'
      }
      
      // 跳转到对应角色主页
      router.push(redirectPath)
    } catch (error) {
        // 调试信息：打印详细错误
        console.log('登录失败详细错误:', error)
        console.log('错误响应:', error.response)

        // 提示登录失败
        let errorMsg = error.message || '登录失败，请重试'
        if (error.response?.data?.message) {
          errorMsg = error.response.data.message
        }
        ElMessage.error(errorMsg)
    } finally {
      // 取消加载状态
      loading.value = false
    }
  })
}

// 忘记密码
const handleForgotPassword = () => {
  ElMessageBox.prompt('请输入您的用户名，我们将发送重置密码链接到您的邮箱', '忘记密码', {
    confirmButtonText: '发送',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '请输入用户名'
  }).then(async ({ value }) => {
    try {
      // 调用后端忘记密码接口
      await axios.post('/api/auth/forgot-password', { username: value })
      ElMessage.success('重置密码链接已发送到您的邮箱，请查收')
    } catch (error) {
      ElMessage.error('发送失败，请稍后重试')
    }
  }).catch(() => {
    // 取消操作
  })
}
</script>

<template>
  <div class="login-container">
    <!-- 背景装饰元素 -->
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="login-card">
      <div class="login-header">
        <div class="logo-container">
          <div class="logo">
            <el-icon class="logo-icon"><Reading /></el-icon>
          </div>
        </div>
        <h2>毕业论文管理系统</h2>
        <p>哈尔滨金融学院</p>
      </div>
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form" label-position="top">
        <el-form-item label="用户名" prop="username" class="custom-form-item">
          <div class="input-wrapper">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名" 
              prefix-icon="User" 
              class="custom-input"
              :class="{ 'input-focus': usernameFocus }"
              @focus="usernameFocus = true"
              @blur="usernameFocus = false"
            />
          </div>
        </el-form-item>
        <el-form-item label="密码" prop="password" class="custom-form-item">
          <div class="input-wrapper">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="请输入密码" 
              prefix-icon="Lock" 
              show-password 
              class="custom-input"
              :class="{ 'input-focus': passwordFocus }"
              @focus="passwordFocus = true"
              @blur="passwordFocus = false"
            />
          </div>
        </el-form-item>
        <div class="form-footer">
          <el-button type="text" @click="handleForgotPassword" class="forgot-password">忘记密码？</el-button>
        </div>
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleLogin" 
            :loading="loading" 
            block
            class="login-button"
          >
            <template v-if="!loading">
              <el-icon class="button-icon"><Right /></el-icon>
              登录系统
            </template>
            <template v-else>
              登录中...
            </template>
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <p>© {{ new Date().getFullYear() }} 哈尔滨金融学院 版权所有</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 重置默认样式，确保页面全屏显示 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  width: 100%;
  min-height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  margin: 0;
  padding: 0;
}

/* 登录容器样式 */
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-attachment: fixed;
  background-size: cover;
  padding: 40px 20px;
  margin: 0;
  position: relative;
}

/* 背景装饰元素 */
.background-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  opacity: 0.7;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: 10%;
  animation-delay: -10s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 30%;
  right: -50px;
  animation-delay: -5s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-50px) rotate(180deg);
  }
}

/* 登录卡片样式 */
.login-card {
  width: 100%;
  max-width: 420px;
  background-color: rgba(255, 255, 255, 0.98);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15), 0 10px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: slideUp 0.6s ease-out;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.login-card:hover {
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.2), 0 15px 30px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 登录头部样式 */
.login-header {
  padding: 40px 40px 25px;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  position: relative;
  overflow: hidden;
}

.login-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 100 100"><defs><pattern id="grid" width="10" height="10" patternUnits="userSpaceOnUse"><path d="M 10 0 L 0 0 0 10" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="0.5"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
  opacity: 0.3;
}

.logo-container {
  position: relative;
  z-index: 1;
  margin-bottom: 20px;
}

.logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.logo:hover {
  transform: scale(1.05) rotate(5deg);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2);
}

.logo-icon {
  font-size: 40px;
  color: white;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.login-header h2 {
  margin: 0 0 10px;
  font-size: 28px;
  font-weight: 700;
  position: relative;
  z-index: 1;
  letter-spacing: -0.5px;
}

.login-header p {
  margin: 0;
  font-size: 14px;
  opacity: 0.95;
  position: relative;
  z-index: 1;
  font-weight: 300;
}

/* 表单样式 */
.login-form {
  padding: 40px;
  position: relative;
}

.custom-form-item {
  margin-bottom: 25px;
  transition: all 0.3s ease;
}

.custom-form-item:hover .el-form-item__label {
  color: #667eea;
}

.custom-form-item .el-form-item__label {
  font-size: 14px;
  font-weight: 500;
  color: #4e5969;
  margin-bottom: 10px;
  transition: all 0.3s ease;
  text-align: left;
}

.input-wrapper {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.input-wrapper:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.custom-input {
  border: none;
  border-radius: 12px;
  height: 52px;
  font-size: 15px;
  padding: 0 16px;
  background: #f8f9fa;
  transition: all 0.3s ease;
}

.custom-input:focus {
  background: #ffffff;
  box-shadow: none;
}

.input-focus .input-wrapper {
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15), 0 4px 12px rgba(0, 0, 0, 0.12);
}

.input-focus .custom-input {
  background: #ffffff;
  border-color: #667eea;
}

/* 表单底部样式 */
.form-footer {
  text-align: right;
  margin-bottom: 25px;
}

.forgot-password {
  padding: 0;
  font-size: 13px;
  color: #667eea;
  font-weight: 500;
  transition: all 0.3s ease;
}

.forgot-password:hover {
  color: #5568d3;
  transform: translateX(2px);
}

/* 登录按钮样式 */
.login-button {
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
  letter-spacing: 0.5px;
  overflow: hidden;
  position: relative;
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: all 0.5s ease;
}

.login-button:hover::before {
  left: 100%;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4090 100%);
}

.login-button:active {
  transform: translateY(0);
  box-shadow: 0 6px 15px rgba(102, 126, 234, 0.3);
}

.button-icon {
  margin-right: 8px;
  font-size: 18px;
  transition: transform 0.3s ease;
}

.login-button:hover .button-icon {
  transform: translateX(5px);
}

/* 登录页脚样式 */
.login-footer {
  padding: 0 40px 35px;
  text-align: center;
  font-size: 12px;
  color: #909399;
  position: relative;
}

.login-footer p {
  margin: 0;
  transition: all 0.3s ease;
}

.login-footer p:hover {
  color: #667eea;
}
/* 响应式设计 */
/* 适配小屏幕设备 (≤768px) */
@media (max-width: 768px) {
  .login-container {
    padding: 10px;
  }
  
  /* 调整背景装饰元素 */
  .circle-1 {
    width: 200px;
    height: 200px;
    top: -50px;
    left: -50px;
  }
  
  .circle-2 {
    width: 150px;
    height: 150px;
    bottom: -30px;
    right: 5%;
  }
  
  .circle-3 {
    width: 100px;
    height: 100px;
    top: 20%;
    right: -30px;
  }
  
  .login-card {
    max-width: 350px;
    width: 100%;
  }
  
  .login-header {
    padding: 30px 25px 15px;
  }
  
  .logo {
    width: 60px;
    height: 60px;
    margin-bottom: 15px;
  }
  
  .logo-icon {
    font-size: 30px;
  }
  
  .login-header h2 {
    font-size: 22px;
  }
  
  .login-header p {
    font-size: 12px;
  }
  
  .login-form {
    padding: 25px;
  }
  
  .custom-form-item {
    margin-bottom: 15px;
  }
  
  .custom-form-item .el-form-item__label {
    font-size: 14px;
    margin-bottom: 6px;
  }
  
  .custom-input {
    font-size: 14px;
    height: 42px;
  }
  
  .login-button {
    height: 44px;
    font-size: 14px;
  }
  
  .login-footer {
    padding: 0 25px 20px;
    font-size: 11px;
  }
  
  .form-footer {
    margin-bottom: 15px;
  }
  
  .forgot-password {
    font-size: 12px;
  }
}

/* 适配中等屏幕设备 (769px-1366px) */
@media (min-width: 769px) and (max-width: 1366px) {
  .login-container {
    padding: 15px;
  }
  
  /* 调整背景装饰元素 */
  .circle-1 {
    width: 250px;
    height: 250px;
  }
  
  .circle-2 {
    width: 180px;
    height: 180px;
  }
  
  .circle-3 {
    width: 130px;
    height: 130px;
  }
  
  .login-card {
    max-width: 380px;
    width: 100%;
  }
  
  .login-header {
    padding: 35px 30px 20px;
  }
  
  .logo {
    width: 70px;
    height: 70px;
    margin-bottom: 18px;
  }
  
  .logo-icon {
    font-size: 35px;
  }
  
  .login-header h2 {
    font-size: 24px;
  }
  
  .login-header p {
    font-size: 13px;
  }
  
  .login-form {
    padding: 30px;
  }
  
  .custom-form-item {
    margin-bottom: 20px;
  }
  
  .custom-form-item .el-form-item__label {
    font-size: 15px;
    margin-bottom: 7px;
  }
  
  .custom-input {
    font-size: 15px;
    height: 46px;
  }
  
  .login-button {
    height: 48px;
    font-size: 15px;
  }
}

/* 适配大屏幕设备 (1367px-1919px) */
@media (min-width: 1367px) and (max-width: 1919px) {
  .login-card {
    max-width: 400px;
    width: 100%;
  }
}

/* 适配高分辨率屏幕 (≥1920px) */
@media (min-width: 1920px) {
  /* 调整背景装饰元素 */
  .circle-1 {
    width: 350px;
    height: 350px;
  }
  
  .circle-2 {
    width: 250px;
    height: 250px;
  }
  
  .circle-3 {
    width: 200px;
    height: 200px;
  }
  
  .login-card {
    max-width: 450px;
    width: 100%;
    box-shadow: 0 15px 40px rgba(0, 0, 0, 0.25);
  }
  
  .login-header {
    padding: 50px 45px 25px;
  }
  
  .logo {
    width: 90px;
    height: 90px;
    margin-bottom: 25px;
  }
  
  .logo-icon {
    font-size: 45px;
  }
  
  .login-header h2 {
    font-size: 32px;
  }
  
  .login-header p {
    font-size: 16px;
  }
  
  .login-form {
    padding: 45px;
  }
  
  .custom-form-item {
    margin-bottom: 25px;
  }
  
  .custom-form-item .el-form-item__label {
    font-size: 16px;
    margin-bottom: 8px;
  }
  
  .custom-input {
    font-size: 16px;
    height: 56px;
  }
  
  .login-button {
    height: 56px;
    font-size: 18px;
    border-radius: 12px;
  }
  
  .login-footer {
    padding: 0 45px 35px;
    font-size: 14px;
  }
  
  .forgot-password {
    font-size: 14px;
  }
}

/* 适配超高分辨率屏幕 (≥2560px) */
@media (min-width: 2560px) {
  /* 调整背景装饰元素 */
  .circle-1 {
    width: 450px;
    height: 450px;
    top: -150px;
    left: -150px;
  }
  
  .circle-2 {
    width: 350px;
    height: 350px;
    bottom: -100px;
    right: 15%;
  }
  
  .circle-3 {
    width: 250px;
    height: 250px;
    top: 30%;
    right: -80px;
  }
  
  .login-card {
    max-width: 520px;
    width: 100%;
    border-radius: 24px;
  }
  
  .login-header {
    padding: 60px 50px 30px;
  }
  
  .logo {
    width: 100px;
    height: 100px;
    margin-bottom: 30px;
  }
  
  .logo-icon {
    font-size: 50px;
  }
  
  .login-header h2 {
    font-size: 36px;
  }
  
  .login-header p {
    font-size: 18px;
  }
  
  .login-form {
    padding: 50px;
  }
  
  .custom-form-item {
    margin-bottom: 30px;
  }
  
  .custom-form-item .el-form-item__label {
    font-size: 18px;
    margin-bottom: 10px;
  }
  
  .custom-input {
    font-size: 18px;
    height: 60px;
  }
  
  .login-button {
    height: 60px;
    font-size: 20px;
    border-radius: 14px;
  }
  
  .login-footer {
    padding: 0 50px 40px;
    font-size: 16px;
  }
  
  .form-footer {
    margin-bottom: 25px;
  }
  
  .forgot-password {
    font-size: 16px;
  }
}

/* 适配超宽屏设备 (≥3840px) */
@media (min-width: 3840px) {
  /* 调整背景装饰元素 */
  .circle-1 {
    width: 550px;
    height: 550px;
    top: -200px;
    left: -200px;
  }
  
  .circle-2 {
    width: 450px;
    height: 450px;
    bottom: -150px;
    right: 20%;
  }
  
  .circle-3 {
    width: 350px;
    height: 350px;
    top: 25%;
    right: -120px;
  }
  
  .login-card {
    max-width: 600px;
    width: 100%;
    border-radius: 28px;
    box-shadow: 0 25px 60px rgba(0, 0, 0, 0.3);
  }
  
  .login-header {
    padding: 70px 60px 40px;
  }
  
  .logo {
    width: 120px;
    height: 120px;
    margin-bottom: 35px;
  }
  
  .logo-icon {
    font-size: 60px;
  }
  
  .login-header h2 {
    font-size: 42px;
  }
  
  .login-header p {
    font-size: 22px;
  }
  
  .login-form {
    padding: 60px;
  }
  
  .custom-form-item {
    margin-bottom: 35px;
  }
  
  .custom-form-item .el-form-item__label {
    font-size: 22px;
    margin-bottom: 12px;
  }
  
  .custom-input {
    font-size: 20px;
    height: 65px;
  }
  
  .login-button {
    height: 68px;
    font-size: 22px;
    border-radius: 16px;
  }
  
  .login-footer {
    padding: 0 60px 50px;
    font-size: 18px;
  }
  
  .form-footer {
    margin-bottom: 30px;
  }
  
  .forgot-password {
    font-size: 18px;
  }
}
</style>