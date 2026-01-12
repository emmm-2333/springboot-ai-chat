package com.example.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class ConversationCreateRequest {

    @NotBlank // 验证字段不能为空
    private String title; // 会话标题
}
