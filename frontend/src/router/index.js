import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '../stores/user';

const Login = () => import('../views/Login.vue');
const Register = () => import('../views/Register.vue');
const UserManagement = () => import('../views/UserManagement.vue');
const Chat = () => import('../views/Chat.vue');

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/chat' },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/users', component: UserManagement },
    { path: '/chat', component: Chat }
  ]
});

router.beforeEach((to, from, next) => {
  const store = useUserStore();
  const isAuthPage = to.path === '/login' || to.path === '/register';
  if (!store.token && !isAuthPage) {
    return next('/login');
  }
  next();
});

export default router;
