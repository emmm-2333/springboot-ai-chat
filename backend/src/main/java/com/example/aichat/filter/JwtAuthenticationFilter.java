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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> whitelist = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/actuator/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(response, "Missing token");
            return;
        }
        String token = auth.substring(7);
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
