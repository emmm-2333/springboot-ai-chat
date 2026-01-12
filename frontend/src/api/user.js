// user.js
// 提供与用户管理相关的 API 调用方法

import request from '../utils/axios';

// 获取用户列表
export const fetchUsers = (params) => request.get('/users', { params });

// 创建新用户
export const createUser = (data) => request.post('/users', data);

// 更新用户信息
export const updateUser = (id, data) => request.put(`/users/${id}`, data);

// 删除用户
export const deleteUser = (id) => request.delete(`/users/${id}`);
