import axios from 'axios';
import { useUserStore } from '../stores/user';
import router from '../router';
import { ElMessage } from 'element-plus';

const instance = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
});

instance.interceptors.request.use((config) => {
  if (!config.headers['Content-Type']) {
    config.headers['Content-Type'] = 'application/json';
  }
  const store = useUserStore();
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`;
  }
  return config;
});

instance.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response && error.response.status === 401) {
      const store = useUserStore();
      store.logout();
      router.push('/login');
      ElMessage.error('登录已过期，请重新登录');
    } else {
      ElMessage.error(error.response?.data?.message || error.message);
    }
    return Promise.reject(error);
  }
);

export default instance;
