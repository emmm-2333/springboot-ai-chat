# 后端修复要点

## 1. JwtAuthenticationFilter 注入错误已修复
- **问题**：FilterConfig 使用 Lombok 的 `@RequiredArgsConstructor` 生成构造器注入，但在某些场景下 Spring 会遇到 Bean 初始化顺序问题。
- **解决**：改用 `@Autowired` 字段注入，避免构造器依赖循环。
- **文件**：[backend/src/main/java/com/example/aichat/config/FilterConfig.java](backend/src/main/java/com/example/aichat/config/FilterConfig.java)

## 2. 类似错误检查清单
如果还有其他 Lombok `@RequiredArgsConstructor` + 构造器注入的报错，改用：
```java
@Configuration
public class XXXConfig {
    @Autowired
    private SomeBean someBean;
    
    // 使用 someBean
}
```

## 3. 重新编译
执行 `mvn clean package` 重新构建，应该不再报 FilterConfig 相关错误。

---

# 前端修复要点

## VSCode 无 npm 脚本显示的原因与解决方案

### 根本原因
- frontend/package.json 不在 VSCode 当前工作区的根目录或未被正确识别。
- VSCode 需要显式配置多文件夹工作区才能找到子目录的 package.json。

### 解决方案
已为你创建工作区文件 **springboot-ai-chat.code-workspace**，包含两个文件夹：
- **Backend**：后端项目（POM 管理）
- **Frontend**：前端项目（npm 脚本）

### 使用步骤
1. 关闭当前 VSCode 窗口。
2. 用工作区文件打开：**File** → **Open Workspace from File** → 选择 `d:\studio\springboot-ai-chat\springboot-ai-chat.code-workspace`。
3. VSCode 左侧栏会显示两个文件夹，右键点击 **Frontend** 文件夹中的 package.json，或在 Explorer 中找到 **NPM Scripts** 侧栏，即可看到 `dev`、`build` 等脚本。
4. 点击运行脚本或在终端手动执行。

### 快速运行前端
若工作区配置生效，你会在 VSCode 底部看到 npm 脚本资源管理器，或直接在 frontend/ 终端运行：
```bash
cd frontend
npm install
npm run dev
```

---

总结：FilterConfig 已修复，工作区已创建。请重新编译后端并用新的工作区文件打开前端项目。
