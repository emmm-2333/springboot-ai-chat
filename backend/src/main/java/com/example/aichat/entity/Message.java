package com.example.aichat.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class Message {

    private Long id; // 消息的唯一标识
    private Long conversationId; // 所属会话的 ID
    private String role; // 消息的角色（如用户或 AI）
    private String content; // 消息的内容
    private Integer tokens; // 消息的 token 数量（用于计费或限制）
    private LocalDateTime createdAt; // 消息的创建时间
}
