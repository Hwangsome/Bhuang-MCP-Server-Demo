# Bhuang MCP Server Demo

## 📋 项目简介

这是一个基于 **Spring AI MCP Server** 构建的 **MCP (Model Context Protocol) stdio 模式服务器**。该项目提供了多种工具和服务，可以通过 MCP 协议与各种 AI 客户端（如 Claude Desktop、Cursor 等）进行集成。

### 🛠️ 主要功能

- **CSDN 文章发布**：支持 Markdown 格式文章自动发布到 CSDN 博客
- **Confluence 集成**：提供 Confluence 空间和页面管理功能
- **天气查询服务**：提供天气信息查询功能
- **MCP stdio 协议支持**：完全兼容 MCP 标准，支持工具调用和资源访问

## 🚀 快速开始

### 构建项目

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包项目
mvn clean package
```

### 运行服务器

**注意**: 如果您通过 MCP 客户端使用此项目，**不需要手动运行服务器**。MCP 客户端会自动启动和管理服务器进程。

以下命令仅在以下情况下使用：
- **开发调试**: 本地开发时测试功能
- **独立运行**: 不通过 MCP 协议使用时
- **功能验证**: 验证服务器是否正常工作

```bash
# 方式1：使用 Maven 运行（开发模式）
mvn spring-boot:run

# 方式2：运行打包后的 JAR（生产模式）
java -jar target/Bhuang-MCP-Server-demo-1.0.0.jar

# 方式3：MCP stdio 模式（通常由 MCP 客户端自动调用）
java -Dspring.ai.mcp.server.stdio=true -jar target/Bhuang-MCP-Server-demo-1.0.0.jar
```

## 🔌 MCP 客户端集成

### Claude Desktop 配置

在 Claude Desktop 的配置文件中添加以下配置：

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
**Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "mcp-server-csdn": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-jar",
        "/path/to/your/target/Bhuang-MCP-Server-demo-1.0.0.jar"
      ],
      "env": {
        "CSDN_COOKIE": "your_csdn_cookie_here",
        "CSDN_CATEGORIES": "springboot,java",
        "CONFLUENCE_URL": "https://your-domain.atlassian.net",
        "CONFLUENCE_USERNAME": "your-email",
        "CONFLUENCE_API_TOKEN": "your-api-token"
      }
    }
  }
}
```

### Cursor 配置

在 Cursor 中使用 MCP 服务器：

```json
{
  "mcp": {
    "servers": {
      "bhuang-server": {
        "command": "java",
        "args": [
          "-Dspring.ai.mcp.server.stdio=true",
          "-jar", 
          "/path/to/Bhuang-MCP-Server-demo-1.0.0.jar"
        ],
        "env": {
          "CSDN_COOKIE": "your_cookie",
          "CSDN_CATEGORIES": "技术分享,Java",
          "CONFLUENCE_URL": "https://your-domain.atlassian.net",
          "CONFLUENCE_USERNAME": "your-email",
          "CONFLUENCE_API_TOKEN": "your-token"
        }
      }
    }
  }
}
```

### 其他 MCP 客户端

任何支持 MCP stdio 协议的客户端都可以使用以下命令启动服务器：

```bash
java -Dspring.ai.mcp.server.stdio=true -jar /path/to/Bhuang-MCP-Server-demo-1.0.0.jar
```

### 📋 配置说明

- **`-Dspring.ai.mcp.server.stdio=true`**: 启用 stdio 模式，让 MCP 客户端可以直接管理服务器进程
- **JAR 路径**: 请将路径替换为您实际的 JAR 文件位置
- **环境变量**: 根据需要配置相应的服务认证信息

## 🔧 可用工具

通过 MCP 协议，客户端可以调用以下工具：

### CSDN 工具
- **`publishMarkdownArticle`**: 发布 Markdown 文章到 CSDN，自动转换 Markdown 为 HTML 格式

### Confluence 工具
- **`searchContent`**: 搜索 Confluence 内容，支持 CQL 查询语法
- **`getPage`**: 获取 Confluence 页面详情
- **`getPageWithExpansions`**: 获取页面详情（带自定义展开参数）
- **`createPage`**: 创建新的 Confluence 页面
- **`createPageQuick`**: 快速创建 Confluence 页面，使用默认空间设置
- **`createMarkdownPage`**: 创建 Markdown 页面，自动转换为 Confluence 格式
- **`createMarkdownPageQuick`**: 快速创建 Markdown 页面，使用默认设置
- **`updatePage`**: 更新 Confluence 页面内容
- **`updateMarkdownPage`**: 更新 Markdown 页面，自动转换为 Confluence 格式
- **`deletePage`**: 删除 Confluence 页面
- **`getSpaces`**: 获取 Confluence 空间列表
- **`addPageLabel`**: 为 Confluence 页面添加标签
- **`getCurrentUser`**: 获取当前 Confluence 用户信息

### 天气工具
- **`getWeatherForecastByLocation`**: 获取指定经纬度的天气预报
- **`getAlerts`**: 获取美国各州的天气警报信息

## 🔍 使用示例

在 MCP 客户端中，您可以这样使用：

```
请帮我把以下 Markdown 内容发布到 CSDN：

# Spring Boot 入门教程
这是一个关于 Spring Boot 的入门教程...
```

客户端会自动调用 `publishMarkdownArticle` 工具来完成发布。

```
创建一个名为"API 文档"的 Confluence 页面，内容是：
# API 接口说明
## 用户管理接口
...
```

客户端会自动调用 `createMarkdownPage` 工具来创建页面。

## 🌟 项目特色

### 🔄 智能转换
- **Markdown → HTML**: 自动转换 Markdown 格式为 CSDN 兼容的 HTML
- **Markdown → Confluence Storage**: 自动转换为 Confluence 存储格式
- **格式保留**: 保持原始格式的完整性和可读性

### 🛡️ 安全性
- **环境变量支持**: 敏感信息通过环境变量管理
- **认证机制**: 支持多种认证方式（Cookie、API Token、Personal Token）
- **权限控制**: 支持只读模式和权限验证

### 📊 可观测性
- **结构化日志**: 使用 SLF4J Logger 进行专业日志记录
- **操作记录**: 详细记录每个操作的执行过程
- **错误处理**: 完善的错误处理和友好的错误信息

### ⚡ 高性能
- **连接池**: 使用 Retrofit2 的连接池机制
- **异步支持**: 支持异步操作处理
- **资源管理**: 自动资源管理和清理

## 🔧 故障排除

### 常见问题

#### 1. CSDN Cookie 失效
```bash
# 重新获取 Cookie 并设置环境变量
export CSDN_COOKIE="新的cookie值"
```

#### 2. Confluence 连接失败
```bash
# 检查配置
export CONFLUENCE_URL="https://your-domain.atlassian.net"
export CONFLUENCE_USERNAME="your-email"
export CONFLUENCE_API_TOKEN="your-token"
```

#### 3. 端口占用
```bash
# 修改端口
export SERVER_PORT=8102
mvn spring-boot:run
```

### 日志查看
```bash
# 查看应用日志
tail -f data/log/Bhuang-MCP-Server-demo.log

# 查看实时日志
mvn spring-boot:run | grep -E "(INFO|ERROR|WARN)"
```

## 环境变量配置

### CSDN 配置

本项目支持通过环境变量覆盖默认的CSDN配置：

| 环境变量 | 描述 | 默认值 | 示例 |
|---------|------|--------|------|
| `CSDN_COOKIE` | CSDN认证Cookie | 配置文件中的值 | `export CSDN_COOKIE="your_cookie_here"` |
| `CSDN_CATEGORIES` | 文章分类标签 | `springboot` | `export CSDN_CATEGORIES="java,技术分享"` |

#### 使用方法

1. **使用配置文件默认值**（无需设置环境变量）：
   ```bash
   mvn spring-boot:run
   ```

2. **使用环境变量覆盖**：
   ```bash
   export CSDN_COOKIE="你的实际Cookie值"
   export CSDN_CATEGORIES="自定义分类"
   mvn spring-boot:run
   ```

3. **临时设置环境变量**：
   ```bash
   CSDN_CATEGORIES=测试分类 mvn spring-boot:run
   ```

### Confluence 配置

| 环境变量 | 描述 | 默认值 |
|---------|------|--------|
| `CONFLUENCE_URL` | Confluence服务器URL | 无 |
| `CONFLUENCE_USERNAME` | Cloud版本用户名 | 无 |
| `CONFLUENCE_API_TOKEN` | Cloud版本API Token | 无 |
| `CONFLUENCE_PERSONAL_TOKEN` | Server版本Personal Token | 无 |
| `CONFLUENCE_DEFAULT_SPACE` | 默认空间KEY | 无 |
| `CONFLUENCE_SSL_VERIFY` | SSL验证开关 | `true` |
| `CONFLUENCE_SPACES_FILTER` | 空间过滤器（逗号分隔） | 无 |
| `CONFLUENCE_READ_ONLY` | 只读模式 | `false` |

## 配置优先级

1. **环境变量** - 最高优先级
2. **配置文件** (`application.yml`) - 作为默认值

当环境变量未设置或为空时，系统将自动使用配置文件中的默认值。

## 安全建议

- 在生产环境中，建议使用环境变量来设置敏感信息（如Cookie、API Token等）
- 不要在代码仓库中提交真实的Cookie或Token值
- 配置文件中的值仅作为开发环境的示例或默认值 