# 你接下来该做什么

1. 数据库
   - 启动本地 MySQL，创建用户/密码与 application.yml 一致，执行 backend/src/main/resources/schema.sql。
2. 配置密钥
   - 在 backend/src/main/resources/application.yml 中替换 jwt.secret 为你自己的 32+ 字符密钥（或使用环境变量）；填入 deepseek.api-key 为你的真实 Key。
3. 后端运行
   - 进入 backend，执行 `mvn clean package`，然后 `java -jar target/ai-chat-0.0.1-SNAPSHOT.jar`。
4. 前端运行
   - 进入 frontend，执行 `npm install`，然后 `npm run dev`，浏览器访问 http://localhost:5173。
5. 测试流程
   - 先注册或使用默认 admin/admin123 登录。
   - 在用户管理页测试增删改查。
   - 在对话页创建会话、发送消息，确认 DeepSeek 响应正常。
6. 部署提示
   - 生产环境建议通过 Nginx 代理：前端静态资源 root 指向 dist，/api 转发到 8080。
   - 使用环境变量或外部配置管理敏感信息（DB 密码、JWT 密钥、DeepSeek Key）。
