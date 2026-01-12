// user.js
// 使用 Pinia 管理用户状态

import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '', // 存储用户的 JWT token
    profile: JSON.parse(localStorage.getItem('profile') || 'null') // 存储用户信息
  }),
  actions: {
    // 设置用户 token
    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    // 设置用户信息
    setProfile(profile) {
      this.profile = profile;
      localStorage.setItem('profile', JSON.stringify(profile));
    },
    // 用户登出
    logout() {
      this.token = '';
      this.profile = null;
      localStorage.removeItem('token');
      localStorage.removeItem('profile');
    }
  }
});
