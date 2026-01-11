package com.example.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatMessageRequest {
    @NotBlank
    private String content;
}
