package com.example.aichat.service;

import java.util.List;

import com.example.aichat.dto.ChatMessageRequest;
import com.example.aichat.dto.ConversationCreateRequest;
import com.example.aichat.entity.Conversation;
import com.example.aichat.entity.Message;

public interface ChatService {

    /**
     * 创建新会话
     *
     * @param userId 用户ID
     * @param request 会话创建请求
     * @return 创建的会话
     */
    Conversation createConversation(Long userId, ConversationCreateRequest request);

    /**
     * 获取用户的会话列表
     *
     * @param userId 用户ID
     * @return 用户的会话列表
     */
    List<Conversation> listConversations(Long userId);

    /**
     * 获取指定会话的消息列表
     *
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @return 指定会话的消息列表
     */
    List<Message> listMessages(Long userId, Long conversationId);

    /**
     * 发送消息到指定会话
     *
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param request 消息发送请求
     * @return 发送的消息
     */
    Message sendMessage(Long userId, Long conversationId, ChatMessageRequest request);
}
