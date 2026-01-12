package com.example.aichat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.aichat.filter.JwtAuthenticationFilter;

@Configuration // 声明为配置类
public class FilterConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        // 注册 JWT 认证过滤器
        registration.setFilter(jwtAuthenticationFilter);
        // 应用过滤器到指定路径
        registration.addUrlPatterns("/api/*");
        // 设置过滤器的优先级
        registration.setOrder(1);
        return registration;
    }
}
