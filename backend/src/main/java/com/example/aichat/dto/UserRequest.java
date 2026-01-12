package com.example.aichat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter、setter、toString 等方法
@NoArgsConstructor // 自动生成无参构造函数
public class UserRequest {

    @NotBlank // 验证字段不能为空
    private String username; // 用户名

    private String password; // 用户密码

    @Email // 验证字段为合法邮箱
    private String email; // 用户邮箱

    private String nickname; // 用户昵称

    private String avatar; // 用户头像 URL

    private Integer status; // 用户状态（如启用或禁用）
}
