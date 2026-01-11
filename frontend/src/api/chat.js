import request from '../utils/axios';

export const listConversations = () => request.get('/conversations');
export const createConversation = (data) => request.post('/conversations', data);
export const listMessages = (conversationId) => request.get(`/conversations/${conversationId}/messages`);
export const sendMessage = (conversationId, data) => request.post(`/chat/${conversationId}`, data);
