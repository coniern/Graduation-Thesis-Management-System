import axios from 'axios';

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  },
  withCredentials: true
});

// 测试验证码获取
async function testCaptcha() {
  try {
    console.log('测试验证码获取...');
    const response = await service.get('/api/auth/captcha');
    console.log('验证码响应:', response.data);
    return response.data;
  } catch (error) {
    console.error('获取验证码失败:', error);
    throw error;
  }
}

// 测试登录
async function testLogin() {
  try {
    // 登录请求
    console.log('\n测试登录...');
    const loginResponse = await service.post('/api/auth/login', {
      username: 'student',
      password: '123456'
    });
    
    console.log('登录响应:', loginResponse.data);
    return loginResponse.data;
  } catch (error) {
    console.error('登录失败:', error.response ? error.response.data : error);
    throw error;
  }
}

// 执行测试
testLogin().then(() => {
  console.log('\n测试完成');
}).catch(() => {
  console.log('\n测试失败');
});
