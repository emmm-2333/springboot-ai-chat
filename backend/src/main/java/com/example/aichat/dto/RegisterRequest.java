package com.example.aichat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class RegisterRequest {

    @NotBlank // 验证字段不能为空
    @Size(min = 3, max = 32) // 验证字段长度范围
    private String username; // 用户名

    @NotBlank // 验证字段不能为空
    @Size(min = 6, max = 64) // 验证字段长度范围
    private String password; // 用户密码

    @Email // 验证字段为合法邮箱
    private String email; // 用户邮箱

    private String nickname; // 用户昵称
}
