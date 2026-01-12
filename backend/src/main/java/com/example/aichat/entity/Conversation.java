package com.example.aichat.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class Conversation {

    private Long id; // 会话的唯一标识
    private Long userId; // 用户 ID，表示该会话属于哪个用户
    private String title; // 会话的标题
    private LocalDateTime createdAt; // 会话的创建时间
    private LocalDateTime updatedAt; // 会话的更新时间
}
