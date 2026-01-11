package com.example.aichat.controller;

import com.example.aichat.dto.ApiResponse;
import com.example.aichat.dto.ChatMessageRequest;
import com.example.aichat.dto.ConversationCreateRequest;
import com.example.aichat.entity.Conversation;
import com.example.aichat.entity.Message;
import com.example.aichat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/conversations")
    public ApiResponse<List<Conversation>> listConversations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.success(chatService.listConversations(userId));
    }

    @PostMapping("/conversations")
    public ApiResponse<Conversation> createConversation(HttpServletRequest request,
                                                        @Valid @RequestBody ConversationCreateRequest body) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.success(chatService.createConversation(userId, body));
    }

    @GetMapping("/conversations/{id}/messages")
    public ApiResponse<List<Message>> listMessages(HttpServletRequest request, @PathVariable("id") Long id) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.success(chatService.listMessages(userId, id));
    }

    @PostMapping("/chat/{conversationId}")
    public ApiResponse<Message> sendMessage(HttpServletRequest request,
                                            @PathVariable Long conversationId,
                                            @Valid @RequestBody ChatMessageRequest body) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.success(chatService.sendMessage(userId, conversationId, body));
    }
}
