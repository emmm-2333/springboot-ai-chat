package com.example.aichat.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration // 声明为配置类
public class WebClientConfig {

    @Bean
    public WebClient deepSeekClient(@Value("${deepseek.base-url}") String baseUrl,
            @Value("${deepseek.api-key}") String apiKey) {
        // 配置 HttpClient，设置超时时间
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30));
        // 配置 WebClient，设置基础 URL 和默认请求头
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
