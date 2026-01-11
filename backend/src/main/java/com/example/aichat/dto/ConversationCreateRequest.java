package com.example.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConversationCreateRequest {
    @NotBlank
    private String title;
}
