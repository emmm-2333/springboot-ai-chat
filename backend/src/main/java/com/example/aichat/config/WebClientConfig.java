package com.example.aichat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient deepSeekClient(@Value("${deepseek.base-url}") String baseUrl,
                                    @Value("${deepseek.api-key}") String apiKey) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30));
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
