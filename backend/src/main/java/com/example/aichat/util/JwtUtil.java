package com.example.aichat.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component // 标注为 Spring 组件
public class JwtUtil {

    private final SecretKey secretKey; // 用于签名和验证 JWT 的密钥
    private final long expirationMinutes; // JWT 的过期时间（分钟）

    public JwtUtil(@Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-minutes}") long expirationMinutes) {
        // 构造函数，初始化密钥和过期时间
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(Long userId, String username) {
        // 生成 JWT Token
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username) // 设置主题（通常是用户名）
                .issuedAt(Date.from(now)) // 设置签发时间
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES))) // 设置过期时间
                .claims(Map.of("userId", userId)) // 添加自定义声明
                .signWith(secretKey) // 使用密钥签名
                .compact();
    }

    public Claims parseClaims(String token) {
        // 解析 JWT Token 并返回声明
        return Jwts.parser()
                .verifyWith(secretKey) // 验证签名
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
