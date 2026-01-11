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

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final WebClient deepSeekClient;

    @Override
    public Conversation createConversation(Long userId, ConversationCreateRequest request) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setTitle(request.getTitle());
        conversationMapper.insert(conversation);
        return conversationMapper.findByIdAndUser(conversation.getId(), userId);
    }

    @Override
    public List<Conversation> listConversations(Long userId) {
        return conversationMapper.listByUser(userId);
    }

    @Override
    public List<Message> listMessages(Long userId, Long conversationId) {
        ensureConversationOwner(userId, conversationId);
        return messageMapper.listByConversation(conversationId);
    }

    @Override
    public Message sendMessage(Long userId, Long conversationId, ChatMessageRequest request) {
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
