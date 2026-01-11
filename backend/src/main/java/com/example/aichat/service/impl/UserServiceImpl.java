package com.example.aichat.service.impl;

import com.example.aichat.dto.AuthRequest;
import com.example.aichat.dto.RegisterRequest;
import com.example.aichat.dto.UserRequest;
import com.example.aichat.entity.User;
import com.example.aichat.exception.ApiException;
import com.example.aichat.mapper.UserMapper;
import com.example.aichat.service.UserService;
import com.example.aichat.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Map<String, Object> login(AuthRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(401, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", user);
        return resp;
    }

    @Override
    public User register(RegisterRequest request) {
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new ApiException(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname() : request.getUsername());
        user.setStatus(1);
        userMapper.insert(user);
        return user;
    }

    @Override
    public Map<String, Object> list(int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        List<User> users = userMapper.findPage(offset, size);
        long total = userMapper.count();
        Map<String, Object> resp = new HashMap<>();
        resp.put("items", users);
        resp.put("total", total);
        return resp;
    }

    @Override
    public User create(UserRequest request) {
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new ApiException(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());
        user.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode("123456"));
        }
        userMapper.insert(user);
        return userMapper.findById(user.getId());
    }

    @Override
    public User update(Long id, UserRequest request) {
        User exist = userMapper.findById(id);
        if (exist == null) {
            throw new ApiException(404, "用户不存在");
        }
        exist.setEmail(request.getEmail());
        exist.setNickname(request.getNickname());
        exist.setAvatar(request.getAvatar());
        exist.setStatus(request.getStatus());
        userMapper.update(exist);
        if (StringUtils.hasText(request.getPassword())) {
            userMapper.updatePassword(id, passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.findById(id);
    }

    @Override
    public void delete(Long id) {
        userMapper.delete(id);
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }
}
