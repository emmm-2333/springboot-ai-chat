// chat.js
// 提供与聊天相关的 API 调用方法

import request from '../utils/axios';

// 获取会话列表
export const listConversations = () => request.get('/conversations');

// 创建新会话
export const createConversation = (data) => request.post('/conversations', data);

// 获取指定会话的消息列表
export const listMessages = (conversationId) => request.get(`/conversations/${conversationId}/messages`);

// 向指定会话发送消息
export const sendMessage = (conversationId, data) => request.post(`/chat/${conversationId}`, data);
