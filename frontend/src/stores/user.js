import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    profile: JSON.parse(localStorage.getItem('profile') || 'null')
  }),
  actions: {
    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    setProfile(profile) {
      this.profile = profile;
      localStorage.setItem('profile', JSON.stringify(profile));
    },
    logout() {
      this.token = '';
      this.profile = null;
      localStorage.removeItem('token');
      localStorage.removeItem('profile');
    }
  }
});
