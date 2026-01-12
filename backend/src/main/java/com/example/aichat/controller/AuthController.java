package com.example.aichat.controller;

import com.example.aichat.dto.ApiResponse;
import com.example.aichat.dto.AuthRequest;
import com.example.aichat.dto.RegisterRequest;
import com.example.aichat.entity.User;
import com.example.aichat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 标注为 REST 控制器
@RequestMapping("/api/auth") // 定义基础路径
@RequiredArgsConstructor // 自动生成构造函数
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody AuthRequest request) {
        // 处理用户登录请求
        return ApiResponse.success(userService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        // 处理用户注册请求
        return ApiResponse.success(userService.register(request));
    }
}
