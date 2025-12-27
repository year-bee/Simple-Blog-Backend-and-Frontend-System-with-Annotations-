import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service = axios.create({
  baseURL: '/api', // 指向 vite.config.ts 中的代理
  timeout: 5000,
  withCredentials: true // 允许携带 Cookie
})

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    
    // 假设后端返回结构: { code: 200, msg: '...', data: ... }
    if (res.code !== 200) {
      ElMessage.error(res.msg || '系统错误')
      
      // 401 未登录
      if (res.code === 401) {
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res.data
    }
  },
  (error) => {
    console.error('Request Error:', error)
    ElMessage.error(error.message || '网络请求失败')
    return Promise.reject(error)
  }
)

export default service