package com.example.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class AuthRequest {

    @NotBlank // 验证字段不能为空
    private String username; // 用户名

    @NotBlank // 验证字段不能为空
    private String password; // 用户密码
}
