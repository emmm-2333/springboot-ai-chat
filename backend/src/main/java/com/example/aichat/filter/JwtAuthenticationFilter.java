package com.example.aichat.filter;

import com.example.aichat.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component // 标注为 Spring 组件
@RequiredArgsConstructor // 自动生成构造函数，注入依赖
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // JWT 工具类，用于解析和验证 Token
    private final AntPathMatcher pathMatcher = new AntPathMatcher(); // 用于路径匹配的工具类
    private final List<String> whitelist = List.of(
            "/api/auth/login", // 登录接口
            "/api/auth/register", // 注册接口
            "/actuator/**" // Spring Actuator 接口
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 判断是否需要过滤当前请求
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // OPTIONS 请求直接放行
        }
        return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path)); // 白名单路径放行
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 执行过滤逻辑
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION); // 获取 Authorization 头部
        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(response, "Missing token"); // 如果没有 Token 或格式不正确，返回未授权
            return;
        }
        String token = auth.substring(7); // 提取 Token
        try {
            Claims claims = jwtUtil.parseClaims(token);
            request.setAttribute("userId", Long.valueOf(claims.get("userId").toString()));
            request.setAttribute("username", claims.getSubject());
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            unauthorized(response, "Invalid or expired token");
        }
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String body = String.format("{\"code\":401,\"message\":\"%s\",\"data\":null}", message);
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}
