import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8080', // 后端API基础URL
  timeout: 5000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  },
  withCredentials: true // 允许携带凭证
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 从localStorage获取token，确保localStorage存在
    let token = ''
    if (typeof localStorage !== 'undefined') {
      token = localStorage.getItem('token') || ''
    }
    if (token) {
      // 设置请求头Authorization
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const { data } = response
    // 请求需要有code字段且为200
    if (data.code === 200) {
      return data
    } else {
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default service
