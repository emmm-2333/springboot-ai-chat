package com.example.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversationCreateRequest {
    @NotBlank
    private String title;
}
