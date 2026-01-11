package com.example.aichat.service;

import com.example.aichat.dto.ChatMessageRequest;
import com.example.aichat.dto.ConversationCreateRequest;
import com.example.aichat.entity.Conversation;
import com.example.aichat.entity.Message;

import java.util.List;

public interface ChatService {
    Conversation createConversation(Long userId, ConversationCreateRequest request);

    List<Conversation> listConversations(Long userId);

    List<Message> listMessages(Long userId, Long conversationId);

    Message sendMessage(Long userId, Long conversationId, ChatMessageRequest request);
}
