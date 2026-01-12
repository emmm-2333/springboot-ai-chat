package com.example.aichat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
@AllArgsConstructor // 自动生成全参构造函数
public class ApiResponse<T> {

    private int code; // 响应状态码
    private String message; // 响应消息
    private T data; // 响应数据
    private Instant timestamp; // 响应时间戳

    public static <T> ApiResponse<T> success(T data) {
        // 返回成功的响应
        return new ApiResponse<>(0, "success", data, Instant.now());
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        // 返回错误的响应
        return new ApiResponse<>(code, message, null, Instant.now());
    }
}
