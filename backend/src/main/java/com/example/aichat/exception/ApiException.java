package com.example.aichat.exception;

import lombok.Getter;

/**
 * 自定义异常类，用于 API 调用中的异常情况
 */
@Getter // 自动生成 getter 方法
public class ApiException extends RuntimeException {

    private final int code; // 自定义错误码

    /**
     * 构造函数，初始化错误码和错误信息
     *
     * @param code 错误码
     * @param message 错误信息
     */
    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }
}
