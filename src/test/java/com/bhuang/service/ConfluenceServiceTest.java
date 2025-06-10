//package com.bhuang.service;
//
//import com.bhuang.config.ConfluenceConfig;
//import com.bhuang.dto.ConfluenceResponseDTO;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Disabled;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.io.IOException;
//
//@SpringBootTest
//@TestPropertySource(properties = {
//    "confluence.url=xxx",
//    "confluence.username=xxx",
//    "confluence.api-token=xxxx",
//    "confluence.default-space-key=TEST",
//    "confluence.read-only-mode=xxx"
//})
//class ConfluenceServiceTest {
//
//    @Autowired
//    private ConfluenceService confluenceService;
//
//    @Autowired
//    private ConfluenceConfig confluenceConfig;
//
//    @Test
//    @Disabled("配置测试已禁用")
//    void testConfluenceConfig() {
//        System.out.println("=== Confluence 配置测试 ===");
//        System.out.println("URL: " + confluenceConfig.getUrl());
//        System.out.println("用户名: " + confluenceConfig.getUsername());
//        System.out.println("是否为 Cloud: " + confluenceConfig.isCloud());
//        System.out.println("默认空间: " + confluenceConfig.getDefaultSpaceKey());
//        System.out.println("只读模式: " + confluenceConfig.isReadOnlyMode());
//        System.out.println("API 基础 URL: " + confluenceConfig.getApiBaseUrl());
//
//        // 注意：在测试环境中不暴露敏感信息
//        String authHeader = confluenceConfig.getAuthorizationHeader();
//        System.out.println("认证头类型: " + (authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 10)) + "..." : "未配置"));
//    }
//
//    @Test
//    void testSearchContent() {
//        System.out.println("=== 测试搜索内容 ===");
//        try {
//            // 使用简单的 CQL 查询搜索所有页面
//            String query = "type=page";
//            ConfluenceResponseDTO result = confluenceService.searchContent(query, 10, 0);
//
//            System.out.println("搜索结果:");
//            if (result.getResults() != null && !result.getResults().isEmpty()) {
//                result.getResults().forEach(page -> {
//                    System.out.println("- " + page.getTitle() + " (ID: " + page.getId() + ")");
//                    if (page.getSpace() != null) {
//                        System.out.println("  空间: " + page.getSpace().getKey() + " - " + page.getSpace().getName());
//                    }
//                });
//                System.out.println("✅ 搜索成功！共找到 " + result.getResults().size() + " 个页面");
//            } else {
//                System.out.println("❌ 没有找到匹配的页面");
//            }
//        } catch (IOException e) {
//            System.err.println("❌ 搜索测试失败: " + e.getMessage());
//            // 在真实测试中，我们可能希望断言失败
//            // throw new RuntimeException("搜索测试失败", e);
//        }
//    }
//
//    @Test
//    @Disabled("创建页面测试已禁用")
//    void testCreateMarkdownPage() {
//        System.out.println("=== 测试创建 Markdown 页面 ===");
//
//        String markdownContent = """
//            # 测试页面
//
//            这是一个通过 **ConfluenceService** 创建的测试页面。
//
//            ## 功能特性
//
//            1. 支持 Markdown 语法
//            2. 自动转换为 Confluence Storage 格式
//            3. 支持代码块
//
//            ```java
//            public class HelloWorld {
//                public static void main(String[] args) {
//                    System.out.println("Hello, Confluence!");
//                }
//            }
//            ```
//
//            ## 列表示例
//
//            - 项目 1
//            - 项目 2
//            - 项目 3
//
//            > 这是一个引用块
//
//            **注意**: 这是一个测试页面，仅用于验证功能。
//            """;
//
//        try {
//            ConfluenceResponseDTO result = confluenceService.createMarkdownPageQuick(
//                "Confluence MCP 服务测试页面",
//                markdownContent
//            );
//
//            System.out.println("✅ 页面创建成功!");
//            System.out.println("页面ID: " + result.getId());
//            System.out.println("页面链接: " + result.getCompleteUrl());
//
//        } catch (IOException e) {
//            System.err.println("⚠️ 创建测试失败（预期的，因为使用的是测试配置或只读模式）: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @Disabled("获取空间测试已禁用")
//    void testGetSpaces() {
//        System.out.println("=== 测试获取空间列表 ===");
//        try {
//            ConfluenceResponseDTO result = confluenceService.getSpaces(10, 0);
//
//            System.out.println("空间列表:");
//            if (result.getResults() != null) {
//                result.getResults().forEach(space -> {
//                    System.out.println("- " + space.getTitle() + " (ID: " + space.getId() + ")");
//                });
//            }
//        } catch (IOException e) {
//            System.err.println("⚠️ 获取空间测试失败（预期的，因为使用的是测试配置）: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @Disabled("获取用户信息测试已禁用")
//    void testGetCurrentUser() {
//        System.out.println("=== 测试获取当前用户信息 ===");
//        try {
//            ConfluenceResponseDTO result = confluenceService.getCurrentUser();
//
//            System.out.println("当前用户信息:");
//            System.out.println("- 类型: " + result.getType());
//            System.out.println("- 标题: " + result.getTitle());
//            System.out.println("- ID: " + result.getId());
//
//        } catch (IOException e) {
//            System.err.println("⚠️ 获取用户信息测试失败（预期的，因为使用的是测试配置）: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @Disabled("Markdown 转换测试已禁用")
//    void testMarkdownConversion() {
//        System.out.println("=== 测试 Markdown 转换功能 ===");
//
//        String markdownContent = """
//            # 标题 1
//            ## 标题 2
//
//            这是一个 **粗体** 和 *斜体* 的示例。
//
//            - 列表项 1
//            - 列表项 2
//
//            ```java
//            System.out.println("Hello World");
//            ```
//            """;
//
//        System.out.println("原始 Markdown:");
//        System.out.println(markdownContent);
//        System.out.println("\n" + "=".repeat(50));
//
//        // 注意：这里我们无法直接测试私有方法，但可以通过创建页面来间接测试
//        try {
//            // 由于是只读模式，这会在尝试创建时失败，但会先执行 Markdown 转换
//            confluenceService.createMarkdownPageQuick("转换测试", markdownContent);
//        } catch (IOException e) {
//            System.out.println("✅ Markdown 转换功能已执行（创建失败是预期的）");
//        }
//    }
//
//    @Test
//    void testSearchLipsMdHowToRun() {
//        System.out.println("=== 测试搜索 lips-md 运行相关内容 ===");
//        try {
//            // 使用多个查询策略搜索 lips-md 相关内容
//            String[] queries = {
//                "text ~ \"lips-md\" AND text ~ \"run\"",
//                "text ~ \"lips-md\" AND text ~ \"运行\"",
//                "text ~ \"lips-md\" AND (text ~ \"how\" OR text ~ \"怎么\" OR text ~ \"如何\")",
//                "title ~ \"lips-md\"",
//                "text ~ \"lips-md\""
//            };
//
//            boolean foundResults = false;
//
//            for (String query : queries) {
//                System.out.println("\n🔍 搜索查询: " + query);
//
//                ConfluenceResponseDTO result = confluenceService.searchContent(query, 20, 0);
//
//                if (result.getResults() != null && !result.getResults().isEmpty()) {
//                    foundResults = true;
//                    System.out.println("✅ 找到 " + result.getResults().size() + " 个相关结果:");
//
//                    result.getResults().forEach(page -> {
//                        System.out.println("📄 " + page.getTitle() + " (ID: " + page.getId() + ")");
//                        if (page.getSpace() != null) {
//                            System.out.println("   空间: " + page.getSpace().getKey() + " - " + page.getSpace().getName());
//                        }
//                        if (page.getCompleteUrl() != null) {
//                            System.out.println("   链接: " + page.getCompleteUrl());
//                        }
//
//                        // 如果有内容预览，显示前200个字符
//                        String content = page.getContent();
//                        if (content != null && !content.trim().isEmpty()) {
//                            String preview = content.length() > 200 ?
//                                content.substring(0, 200) + "..." : content;
//                            // 移除HTML标签获得纯文本预览
//                            preview = preview.replaceAll("<[^>]+>", "").trim();
//                            if (!preview.isEmpty()) {
//                                System.out.println("   预览: " + preview);
//                            }
//                        }
//                        System.out.println();
//                    });
//                } else {
//                    System.out.println("❌ 该查询没有找到匹配结果");
//                }
//            }
//
//            if (!foundResults) {
//                System.out.println("⚠️ 所有查询都没有找到关于 'lips-md' 的相关内容");
//                System.out.println("建议检查:");
//                System.out.println("1. 确认 'lips-md' 相关文档确实存在于 Confluence 中");
//                System.out.println("2. 检查当前用户是否有权限访问相关空间");
//                System.out.println("3. 尝试在 Confluence Web 界面中手动搜索验证");
//            } else {
//                System.out.println("🎉 搜索完成！找到了 lips-md 相关内容");
//            }
//
//        } catch (IOException e) {
//            System.err.println("❌ 搜索 lips-md 相关内容失败: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void testGetLipsMdRunGuide() {
//        System.out.println("=== 获取 LIPS-MD 本地运行指南详细内容 ===");
//        try {
//            // 页面ID: 536529972 - "How to run LIPS-MD locally"
//            String pageId = "536529972";
//
//            // 使用带有body.storage展开的API调用
//            ConfluenceResponseDTO page = confluenceService.getPageWithExpansions(pageId, "body.storage,space,version");
//
//            if (page != null) {
//                System.out.println("📄 页面标题: " + page.getTitle());
//                System.out.println("🆔 页面ID: " + page.getId());
//                System.out.println("📊 页面状态: " + page.getStatus());
//
//                if (page.getSpace() != null) {
//                    System.out.println("🌐 所属空间: " + page.getSpace().getKey() + " - " + page.getSpace().getName());
//                }
//
//                if (page.getVersion() != null) {
//                    System.out.println("📝 版本号: " + page.getVersion().getNumber());
//                }
//
//                if (page.getCompleteUrl() != null) {
//                    System.out.println("🔗 页面链接: " + page.getCompleteUrl());
//                }
//
//                // 获取Storage格式的页面内容（这是原始的Confluence存储格式）
//                String storageContent = null;
//                if (page.getBody() != null && page.getBody().getStorage() != null) {
//                    storageContent = page.getBody().getStorage().getValue();
//                }
//
//                if (storageContent != null && !storageContent.trim().isEmpty()) {
//                    System.out.println("\n📖 页面内容 (Storage Format):");
//                    System.out.println("=".repeat(100));
//
//                    // 简单清理Storage格式的内容，保留结构
//                    String cleanContent = storageContent
//                        .replaceAll("<ac:structured-macro[^>]*>.*?</ac:structured-macro>", "[宏内容]")
//                        .replaceAll("<ac:parameter[^>]*>.*?</ac:parameter>", "")
//                        .replaceAll("<ac:rich-text-body>", "")
//                        .replaceAll("</ac:rich-text-body>", "")
//                        .replaceAll("<p>", "\n")
//                        .replaceAll("</p>", "")
//                        .replaceAll("<h1>", "\n# ")
//                        .replaceAll("</h1>", "")
//                        .replaceAll("<h2>", "\n## ")
//                        .replaceAll("</h2>", "")
//                        .replaceAll("<h3>", "\n### ")
//                        .replaceAll("</h3>", "")
//                        .replaceAll("<li>", "\n- ")
//                        .replaceAll("</li>", "")
//                        .replaceAll("<ul>", "")
//                        .replaceAll("</ul>", "")
//                        .replaceAll("<ol>", "")
//                        .replaceAll("</ol>", "")
//                        .replaceAll("<strong>", "**")
//                        .replaceAll("</strong>", "**")
//                        .replaceAll("<em>", "*")
//                        .replaceAll("</em>", "*")
//                        .replaceAll("<code>", "`")
//                        .replaceAll("</code>", "`")
//                        .replaceAll("<[^>]+>", "")  // 移除剩余的HTML标签
//                        .replaceAll("&nbsp;", " ")
//                        .replaceAll("&quot;", "\"")
//                        .replaceAll("&amp;", "&")
//                        .replaceAll("&lt;", "<")
//                        .replaceAll("&gt;", ">")
//                        .replaceAll("\\n\\s*\\n", "\n\n")  // 压缩多个空行
//                        .trim();
//
//                    // 限制显示长度
//                    if (cleanContent.length() > 2000) {
//                        System.out.println(cleanContent.substring(0, 2000) + "\n\n... [内容截断，总长度: " + cleanContent.length() + " 字符] ...");
//                    } else {
//                        System.out.println(cleanContent);
//                    }
//
//                    System.out.println("=".repeat(100));
//
//                    // 尝试提取关键的运行步骤
//                    System.out.println("\n🔍 关键信息提取:");
//                    String[] lines = cleanContent.split("\n");
//                    boolean inStepsSection = false;
//                    int stepCount = 0;
//
//                    for (String line : lines) {
//                        line = line.trim();
//                        if (line.toLowerCase().contains("step") || line.toLowerCase().contains("运行") ||
//                            line.toLowerCase().contains("setup") || line.toLowerCase().contains("配置")) {
//                            inStepsSection = true;
//                            stepCount++;
//                            if (stepCount <= 10) { // 显示前10个步骤
//                                System.out.println("📌 " + line);
//                            }
//                        } else if (inStepsSection && line.startsWith("-") && stepCount <= 10) {
//                            System.out.println("   " + line);
//                        }
//                    }
//                } else {
//                    System.out.println("⚠️ 页面Storage内容为空，尝试获取View内容");
//                    String viewContent = page.getContent();
//                    if (viewContent != null) {
//                        System.out.println("📖 View格式内容长度: " + viewContent.length() + " 字符");
//                    }
//                }
//
//                System.out.println("✅ 成功获取 LIPS-MD 本地运行指南！");
//
//            } else {
//                System.out.println("❌ 未找到指定的页面");
//            }
//
//        } catch (IOException e) {
//            System.err.println("❌ 获取页面内容失败: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}