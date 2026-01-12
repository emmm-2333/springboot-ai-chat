package com.example.aichat.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class User {

    private Long id; // 用户的唯一标识
    private String username; // 用户名
    @JsonIgnore // 忽略密码字段，避免序列化到 JSON
    private String password; // 用户密码
    private String email; // 用户邮箱
    private String nickname; // 用户昵称
    private String avatar; // 用户头像 URL
    private Integer status; // 用户状态（如启用或禁用）
    private LocalDateTime createdAt; // 用户的创建时间
    private LocalDateTime updatedAt; // 用户的更新时间
}
