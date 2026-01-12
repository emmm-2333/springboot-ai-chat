package com.example.aichat.controller;

import com.example.aichat.dto.ApiResponse;
import com.example.aichat.dto.UserRequest;
import com.example.aichat.entity.User;
import com.example.aichat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController // 标注为 REST 控制器
@RequestMapping("/api/users") // 定义用户相关的基础路径
@RequiredArgsConstructor // 自动生成构造函数
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<?> list(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        // 分页获取用户列表
        return ApiResponse.success(userService.list(page, size));
    }

    @PostMapping
    public ApiResponse<User> create(@Valid @RequestBody UserRequest request) {
        // 创建新用户
        return ApiResponse.success(userService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        // 更新指定用户的信息
        return ApiResponse.success(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        // 删除指定用户
        userService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> detail(@PathVariable Long id) {
        // 获取指定用户的详细信息
        return ApiResponse.success(userService.findById(id));
    }
}
