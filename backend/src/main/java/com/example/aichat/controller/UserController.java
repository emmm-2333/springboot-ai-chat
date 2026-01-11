package com.example.aichat.controller;

import com.example.aichat.dto.ApiResponse;
import com.example.aichat.dto.UserRequest;
import com.example.aichat.entity.User;
import com.example.aichat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<?> list(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(userService.list(page, size));
    }

    @PostMapping
    public ApiResponse<User> create(@Valid @RequestBody UserRequest request) {
        return ApiResponse.success(userService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ApiResponse.success(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> detail(@PathVariable Long id) {
        return ApiResponse.success(userService.findById(id));
    }
}
