package com.example.aichat.service.impl;

import com.example.aichat.dto.ChatMessageRequest;
import com.example.aichat.dto.ConversationCreateRequest;
import com.example.aichat.entity.Conversation;
import com.example.aichat.entity.Message;
import com.example.aichat.exception.ApiException;
import com.example.aichat.mapper.ConversationMapper;
import com.example.aichat.mapper.MessageMapper;
import com.example.aichat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service // 标注为 Spring 服务组件
@RequiredArgsConstructor // 自动生成构造函数，注入依赖
public class ChatServiceImpl implements ChatService {

    private final ConversationMapper conversationMapper; // 会话数据访问对象
    private final MessageMapper messageMapper; // 消息数据访问对象
    private final WebClient deepSeekClient; // WebClient，用于调用外部服务

    @Override
    public Conversation createConversation(Long userId, ConversationCreateRequest request) {
        // 创建新会话
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setTitle(request.getTitle());
        conversationMapper.insert(conversation);
        return conversationMapper.findByIdAndUser(conversation.getId(), userId);
    }

    @Override
    public List<Conversation> listConversations(Long userId) {
        // 获取用户的会话列表
        System.out.println("DEBUG: listConversations called with userId = " + userId);
        List<Conversation> result = conversationMapper.listByUser(userId);
        System.out.println("DEBUG: Found " + result.size() + " conversations for userId = " + userId);
        return result;
    }

    @Override
    public List<Message> listMessages(Long userId, Long conversationId) {
        // 获取指定会话的消息列表
        ensureConversationOwner(userId, conversationId);
        return messageMapper.listByConversation(conversationId);
    }

    @Override
    public Message sendMessage(Long userId, Long conversationId, ChatMessageRequest request) {
        // 向指定会话发送消息
        ensureConversationOwner(userId, conversationId);
        Message userMsg = new Message();
        userMsg.setConversationId(conversationId);
        userMsg.setRole("user");
        userMsg.setContent(request.getContent());
        messageMapper.insert(userMsg);

        String aiContent = callDeepSeek(request.getContent());
        Message aiMsg = new Message();
        aiMsg.setConversationId(conversationId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiContent);
        messageMapper.insert(aiMsg);
        conversationMapper.touch(conversationId);
        return aiMsg;
    }

    private void ensureConversationOwner(Long userId, Long conversationId) {
        if (conversationMapper.findByIdAndUser(conversationId, userId) == null) {
            throw new ApiException(403, "无权访问该会话");
        }
    }

    private String callDeepSeek(String prompt) {
        try {
            Map<String, Object> response = deepSeekClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(Map.of(
                            "model", "deepseek-chat",
                            "temperature", 0.7,
                            "messages", List.of(Map.of(
                                    "role", "user",
                                    "content", prompt
                            ))
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorResume(ex -> Mono.error(new ApiException(502, "调用大模型失败: " + ex.getMessage())))
                    .block();
            if (response == null || response.get("choices") == null) {
                throw new ApiException(502, "大模型返回空响应");
            }
            List<?> choices = (List<?>) response.get("choices");
            if (choices.isEmpty()) {
                throw new ApiException(502, "大模型无结果");
            }
            Object choice = choices.get(0);
            if (choice instanceof Map<?, ?> choiceMap) {
                Object message = choiceMap.get("message");
                if (message instanceof Map<?, ?> msgMap) {
                    Object content = msgMap.get("content");
                    if (content != null) {
                        return content.toString();
                    }
                }
            }
            throw new ApiException(502, "解析大模型响应失败");
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(502, "调用大模型异常: " + ex.getMessage());
        }
    }
}
