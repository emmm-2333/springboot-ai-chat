# 密钥管理与安全指南

本指南帮助你在开发、CI/CD 和生产环境中安全管理密钥，避免把敏感信息提交到仓库。

## 目标
- 避免在仓库中硬编码密钥（防止扫描与泄露）。
- 在本地与生产环境安全加载密钥。
- 提供可执行的步骤和命令示例。

## 首选方案：Spring ConfigTree（本地文件，不用环境变量）

后端已配置从本地目录读取机密，无需环境变量：
- 配置位置：`backend/src/main/resources/application.yml`
- 关键项：
  ```yaml
  spring:
    config:
      import: optional:configtree:./backend/secrets/
  deepseek:
    # api-key 由 configtree 导入，文件路径：backend/secrets/deepseek/api-key
    base-url: https://api.deepseek.com
  ```
- 目录结构：
  ```
  backend/
    secrets/
      deepseek/
        api-key          # 文件内容即属性值
  ```
- Git 忽略：`backend/secrets/` 已加入 `.gitignore`，不会被提交到仓库。

使用步骤：
1. 将你的真实密钥写入文件 `backend/secrets/deepseek/api-key`（仅一行，勿含空格或注释）。
2. 启动后端（例如：`mvn spring-boot:run`），Spring 会自动把文件内容映射为属性 `deepseek.api-key`。
3. 如需在服务器/容器中使用：把 `backend/secrets/` 目录以只读卷挂载，路径保持一致即可。

## 生产与容器化部署选项（更安全）

- Kubernetes Secrets：
  ```bash
  kubectl create secret generic deepseek-secret --from-literal=DEEPSEEK_API_KEY=your-new-api-key
  ```
  在 Deployment 中注入为环境变量或挂载为文件（挂载到 `backend/secrets/deepseek/api-key` 路径可复用 configtree）。

- 云托管机密服务：
  - AWS Secrets Manager / SSM Parameter Store
  - Azure Key Vault
  - GCP Secret Manager
  - HashiCorp Vault

  推荐结合 Spring Cloud Config 或相应的客户端集成，在启动时拉取机密。

## CI/CD（不把密钥写入仓库）

- 使用平台 Secrets 管理（如 GitHub Actions Secrets），在流水线中注入环境：
  ```yaml
  - name: Build and run
    env:
      DEEPSEEK_API_KEY: ${{ secrets.DEEPSEEK_API_KEY }}
    run: mvn -DskipTests package
  ```
- 若希望复用 configtree：在构建/部署阶段将机密写入工作目录对应文件，再启动应用。

## 前端注意事项

- 前端（Vite）中的 `.env.local` 仅用于非机密配置（例如接口地址、开关），变量须以 `VITE_` 前缀。
- 任何前端变量最终会暴露到客户端，**不要**在前端保存真实私密密钥。
- 如需调用需密钥的外部服务，请通过后端代理由后端持有密钥。

## 防止未来泄露（建议）

- 预提交钩子与扫描：
  - `pre-commit` + `detect-secrets` 或 `truffleHog` 在本地与 CI 中扫描敏感信息。
- 历史清理（若曾提交过密钥）：
  - 使用 `git-filter-repo` 或 `BFG` 清理历史，并强制推送。此操作会重写历史，需团队协调。
- 密钥轮换与审计：
  - 已泄露密钥必须撤销；启用访问日志与告警；定期轮换密钥，缩短有效期。

## 快速命令示例（本地）

- 写入密钥（Windows）：打开编辑器直接写入 `backend/secrets/deepseek/api-key`。
- 启动后端（会自动读取 configtree）：
  ```bash
  cd backend
  mvn spring-boot:run
  ```

## 常见问题

- 问：为什么不用前端存密钥？
  - 答：前端代码在浏览器端可见，任何打包进客户端的密钥都不安全，应由后端持有。
- 问：仓库仍被平台提示泄露？
  - 答：若历史提交中包含旧密钥，建议进行历史清理（即便已撤销密钥）。

---
如需把以上指南拆分到 `SECURITY.md` 或添加更具体的云端配置示例，请告知我来补充。