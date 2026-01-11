package com.example.aichat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String nickname;
    private String avatar;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
