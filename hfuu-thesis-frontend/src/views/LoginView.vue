<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
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
      localStorage.setItem('token', response.token)
      localStorage.setItem('userInfo', JSON.stringify(response.userInfo))

      // 提示登录成功
      ElMessage.success('登录成功')

      // 跳转到首页
      router.push('/')
    } catch (error) {
      // 提示登录失败
      ElMessage.error(error.message || '登录失败，请重试')
    } finally {
      // 取消加载状态
      loading.value = false
    }
  })
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>登录</h2>
        <p>哈尔滨金融学院毕业论文管理系统</p>
      </div>
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" block>
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <p>© 2024 哈尔滨金融学院</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  animation: slideUp 0.5s ease-in-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  padding: 40px 40px 20px;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
}

.login-header h2 {
  margin: 0 0 10px;
  font-size: 24px;
  font-weight: 600;
}

.login-header p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.login-form {
  padding: 40px;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.login-form .el-button {
  height: 45px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
}

.login-footer {
  padding: 0 40px 30px;
  text-align: center;
  font-size: 12px;
  color: #909399;
}
</style>