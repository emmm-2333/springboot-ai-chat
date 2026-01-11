package com.example.aichat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data, Instant.now());
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, Instant.now());
    }
}
