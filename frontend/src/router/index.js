// index.js
// 配置前端路由和导航守卫

import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '../stores/user';

// 路由组件的懒加载
const Login = () => import('../views/Login.vue');
const Register = () => import('../views/Register.vue');
const UserManagement = () => import('../views/UserManagement.vue');
const Chat = () => import('../views/Chat.vue');

// 定义路由规则
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/chat' }, // 默认重定向到聊天页面
    { path: '/login', component: Login }, // 登录页面
    { path: '/register', component: Register }, // 注册页面
    { path: '/users', component: UserManagement }, // 用户管理页面
    { path: '/chat', component: Chat } // 聊天页面
  ]
});

// 添加导航守卫
router.beforeEach((to, from, next) => {
  const store = useUserStore();
  const isAuthPage = to.path === '/login' || to.path === '/register';
  if (!store.token && !isAuthPage) {
    return next('/login'); // 未登录时跳转到登录页面
  }
  next();
});

export default router;
