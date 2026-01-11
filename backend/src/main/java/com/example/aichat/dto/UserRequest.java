package com.example.aichat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    private String username;

    private String password;

    @Email
    private String email;

    private String nickname;

    private String avatar;

    private Integer status;
}
