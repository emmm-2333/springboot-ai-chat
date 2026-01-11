package com.example.aichat.service;

import com.example.aichat.dto.AuthRequest;
import com.example.aichat.dto.RegisterRequest;
import com.example.aichat.dto.UserRequest;
import com.example.aichat.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> login(AuthRequest request);

    User register(RegisterRequest request);

    Map<String, Object> list(int page, int size);

    User create(UserRequest request);

    User update(Long id, UserRequest request);

    void delete(Long id);

    User findById(Long id);
}
