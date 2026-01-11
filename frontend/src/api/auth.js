import request from '../utils/axios';

export const login = (data) => {
  return request.post('/auth/login', data, {
    headers: { 'Content-Type': 'application/json' }
  });
};

export const register = (data) => {
  return request.post('/auth/register', data, {
    headers: { 'Content-Type': 'application/json' }
  });
};