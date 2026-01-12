// axios.js
// 配置 Axios 实例和拦截器

import axios from 'axios';
import { useUserStore } from '../stores/user';
import router from '../router';
import { ElMessage } from 'element-plus';

// 创建 Axios 实例
const instance = axios.create({
  baseURL: '/api', // 设置基础 URL
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json' // 默认请求头
  }
});

// 请求拦截器
instance.interceptors.request.use((config) => {
  if (!config.headers['Content-Type']) {
    config.headers['Content-Type'] = 'application/json';
  }
  const store = useUserStore();
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`; // 添加 JWT token 到请求头
  }
  return config;
});

// 响应拦截器
instance.interceptors.response.use(
  (response) => response.data, // 直接返回响应数据
  (error) => {
    if (error.response && error.response.status === 401) {
      const store = useUserStore();
      store.logout(); // 清除用户状态
      router.push('/login'); // 跳转到登录页面
      ElMessage.error('登录已过期，请重新登录');
    } else {
      ElMessage.error(error.response?.data?.message || error.message); // 显示错误信息
    }
    return Promise.reject(error);
  }
);

export default instance;
