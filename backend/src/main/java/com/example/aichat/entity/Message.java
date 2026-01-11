package com.example.aichat.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Message {
    private Long id;
    private Long conversationId;
    private String role;
    private String content;
    private Integer tokens;
    private LocalDateTime createdAt;
}
