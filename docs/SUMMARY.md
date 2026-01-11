# 项目现状概览

- 技术栈：后端 Spring Boot 3.3.4 + MyBatis + JWT + WebClient，前端 Vue 3 + Vite + Element Plus + Pinia + Axios，数据库 MySQL，Java 21。
- 目录结构：
  - backend/ 后端项目，核心代码在 src/main/java/com/example/aichat，配置在 src/main/resources。
  - frontend/ 前端项目，Vite + Vue 3 入口 src/main.js。
  - docs/ 文档目录（本文件）。
- 主要功能：
  - JWT 认证：登录颁发 Token，过滤器拦截 /api/**，白名单 /api/auth/login、/api/auth/register、/actuator/**。
  - 用户管理：注册、登录、分页列表、创建、更新、删除、详情。
  - 对话功能：创建会话、列出会话、获取消息、发送消息并调用 DeepSeek，消息持久化。
- 配置要点：
  - application.yml 已放置示例 JWT 密钥（32+ 字符）与 DeepSeek 占位符 API Key。
  - schema.sql 包含建库建表与默认 admin 账户（admin/admin123）。
- 前端页面：Login、Register、UserManagement、Chat，路由守卫检查 Token。
- 运行代理：开发态 Vite proxy /api -> http://localhost:8080。

## 关键文件（示例）
- 后端入口与配置：backend/pom.xml，backend/src/main/java/com/example/aichat/AiChatApplication.java
- 安全与过滤：backend/src/main/java/com/example/aichat/util/JwtUtil.java，backend/src/main/java/com/example/aichat/filter/JwtAuthenticationFilter.java，backend/src/main/java/com/example/aichat/config/FilterConfig.java
- 控制器：backend/src/main/java/com/example/aichat/controller/AuthController.java，UserController.java，ChatController.java
- 服务实现：backend/src/main/java/com/example/aichat/service/impl/UserServiceImpl.java，ChatServiceImpl.java
- 数据访问：backend/src/main/java/com/example/aichat/mapper/*.java
- 初始化脚本与配置：backend/src/main/resources/schema.sql，application.yml
- 前端入口与路由：frontend/src/main.js，frontend/src/router/index.js
- 前端视图：frontend/src/views/Login.vue，Register.vue，UserManagement.vue，Chat.vue
