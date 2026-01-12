// auth.js
// 提供与身份验证相关的 API 调用方法

import request from '../utils/axios';

// 用户登录接口
export const login = (data) => {
  return request.post('/auth/login', data, {
    headers: { 'Content-Type': 'application/json' }
  });
};

// 用户注册接口
export const register = (data) => {
  return request.post('/auth/register', data, {
    headers: { 'Content-Type': 'application/json' }
  });
};