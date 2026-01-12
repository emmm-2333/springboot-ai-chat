package com.example.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class ChatMessageRequest {

    @NotBlank // 验证字段不能为空
    private String content; // 消息内容
}
