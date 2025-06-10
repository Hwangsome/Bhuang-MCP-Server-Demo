package com.bhuang.service;

import com.bhuang.config.ConfluenceConfig;
import com.bhuang.dto.ConfluencePageRequestDTO;
import com.bhuang.dto.ConfluenceResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Confluence 页面管理服务
 */
@Service
public class ConfluenceService {

    private static final Logger log = LoggerFactory.getLogger(ConfluenceService.class);

    @Autowired
    private ConfluenceConfig confluenceConfig;

    private ConfluenceApiService confluenceApiService;
    
    // Markdown 解析器
    private Parser markdownParser;
    private HtmlRenderer htmlRenderer;

    @PostConstruct
    public void init() {
        log.info("初始化 Confluence 服务...");
        
        // 检查URL配置
        if (confluenceConfig.getUrl() == null || confluenceConfig.getUrl().trim().isEmpty()) {
            log.warn("Confluence URL 未配置，服务将在测试模式下运行");
            return;
        }
        
        try {
            // 创建ObjectMapper实例
            ObjectMapper objectMapper = new ObjectMapper();
            
            String baseUrl = confluenceConfig.getApiBaseUrl();
            log.info("Confluence API Base URL: {}", baseUrl);
            
            // 创建Retrofit实例
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                    .build();

            confluenceApiService = retrofit.create(ConfluenceApiService.class);
            
            log.info("Confluence API 客户端初始化成功");
        } catch (Exception e) {
            log.error("Confluence API 客户端初始化失败: {}", e.getMessage());
            // 不抛出异常，允许应用继续启动
        }
        
        // 初始化 Markdown 解析器
        MutableDataSet options = new MutableDataSet();
        markdownParser = Parser.builder(options).build();
        htmlRenderer = HtmlRenderer.builder(options).build();
    }

    /**
     * 检查 Confluence API 客户端是否可用
     * 
     * @throws IOException 如果 API 客户端不可用
     */
    private void checkApiAvailable() throws IOException {
        if (confluenceApiService == null) {
            throw new IOException("Confluence API 客户端未初始化，请检查配置");
        }
    }

    /**
     * 将 Markdown 内容转换为 Confluence Storage 格式
     * 
     * @param markdown Markdown 格式的内容
     * @return Confluence Storage 格式的内容
     */
    private String convertMarkdownToConfluenceStorage(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }
        
        try {
            Node document = markdownParser.parse(markdown);
            String html = htmlRenderer.render(document);
            
            // 简单的HTML到Confluence Storage格式转换
            // 实际应用中可能需要更复杂的转换逻辑
            return html.replace("<h1>", "<h1>").replace("</h1>", "</h1>")
                      .replace("<h2>", "<h2>").replace("</h2>", "</h2>")
                      .replace("<h3>", "<h3>").replace("</h3>", "</h3>")
                      .replace("<p>", "<p>").replace("</p>", "</p>")
                      .replace("<ul>", "<ul>").replace("</ul>", "</ul>")
                      .replace("<ol>", "<ol>").replace("</ol>", "</ol>")
                      .replace("<li>", "<li>").replace("</li>", "</li>")
                      .replace("<code>", "<code>").replace("</code>", "</code>")
                      .replace("<pre>", "<ac:structured-macro ac:name=\"code\"><ac:plain-text-body><![CDATA[")
                      .replace("</pre>", "]]></ac:plain-text-body></ac:structured-macro>");
        } catch (Exception e) {
            System.err.println("Markdown 转换失败: " + e.getMessage());
            // 如果转换失败，返回简单的段落格式
            return "<p>" + markdown.replace("\n", "</p>\n<p>") + "</p>";
        }
    }

    /**
     * 搜索 Confluence 内容
     * 
     * @param query 搜索查询语句（CQL格式）
     * @param limit 返回结果数量限制
     * @param start 分页起始位置
     * @return 搜索结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "搜索 Confluence 内容，支持 CQL 查询语法")
    public ConfluenceResponseDTO searchContent(String query, Integer limit, Integer start) throws IOException {
        checkApiAvailable();
        
        String authHeader = confluenceConfig.getAuthorizationHeader();
        String expand = "body.storage,space,version,ancestors";
        
        System.out.println("=== 搜索 Confluence 内容 ===");
        System.out.println("查询语句: " + query);
        System.out.println("返回数量: " + (limit != null ? limit : "默认"));
        System.out.println("起始位置: " + (start != null ? start : 0));
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.searchContent(
            authHeader, query, limit, start, expand);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 搜索成功");
            System.out.println("返回结果数: " + (result.getResults() != null ? result.getResults().size() : 0));
            return result;
        } else {
            handleErrorResponse(response, "搜索内容");
            throw new IOException("Confluence 搜索失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 获取页面详情
     * 
     * @param pageId 页面ID
     * @return 页面详情
     * @throws IOException 网络请求异常
     */
    @Tool(description = "获取 Confluence 页面详情")
    public ConfluenceResponseDTO getPage(String pageId) throws IOException {
        checkApiAvailable();
        
        String authHeader = confluenceConfig.getAuthorizationHeader();
        String expand = "body.storage,body.view,space,version,ancestors";
        
        System.out.println("=== 获取页面详情 ===");
        System.out.println("页面ID: " + pageId);
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.getPage(authHeader, pageId, expand);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 获取页面成功");
            System.out.println("页面标题: " + result.getTitle());
            System.out.println("页面空间: " + (result.getSpace() != null ? result.getSpace().getKey() : "未知"));
            return result;
        } else {
            handleErrorResponse(response, "获取页面");
            throw new IOException("获取 Confluence 页面失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 获取页面详情（带展开参数）
     */
    @Tool
    public ConfluenceResponseDTO getPageWithExpansions(String pageId, String expand) throws IOException {
        checkApiAvailable();
        
        System.out.println("=== 获取页面详情（带展开参数） ===");
        System.out.println("页面ID: " + pageId);
        System.out.println("展开参数: " + expand);
        
        try {
            Call<ConfluenceResponseDTO> call = confluenceApiService.getPageWithExpansions(pageId, expand);
            Response<ConfluenceResponseDTO> response = call.execute();
            
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("✅ 获取页面成功");
                ConfluenceResponseDTO result = response.body();
                System.out.println("页面标题: " + result.getTitle());
                System.out.println("页面空间: " + (result.getSpace() != null ? result.getSpace().getKey() : "未知"));
                return result;
            } else {
                String errorMsg = "获取页面失败: HTTP " + response.code();
                if (response.errorBody() != null) {
                    errorMsg += " - " + response.errorBody().string();
                }
                System.err.println("❌ " + errorMsg);
                throw new IOException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = "获取页面时发生异常: " + e.getMessage();
            System.err.println("❌ " + errorMsg);
            throw new IOException(errorMsg, e);
        }
    }

    /**
     * 创建新页面
     * 
     * @param title 页面标题
     * @param content 页面内容（HTML格式）
     * @param spaceKey 空间Key（可选，使用配置默认值）
     * @param parentId 父页面ID（可选）
     * @return 创建结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "创建新的 Confluence 页面")
    public ConfluenceResponseDTO createPage(String title, String content, String spaceKey, String parentId) throws IOException {
        checkApiAvailable();
        
        String authHeader = confluenceConfig.getAuthorizationHeader();
        
        // 如果没有指定空间，使用配置中的默认空间
        if (spaceKey == null || spaceKey.trim().isEmpty()) {
            spaceKey = confluenceConfig.getDefaultSpaceKey();
        }
        
        System.out.println("=== 创建 Confluence 页面 ===");
        System.out.println("标题: " + title);
        System.out.println("内容长度: " + (content != null ? content.length() : 0) + " 字符");
        System.out.println("空间: " + spaceKey);
        System.out.println("父页面ID: " + (parentId != null ? parentId : "无"));
        
        // 创建页面请求对象
        ConfluencePageRequestDTO pageRequest = new ConfluencePageRequestDTO(title, content, spaceKey, parentId);
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.createPage(authHeader, pageRequest);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 页面创建成功！");
            System.out.println("📰 页面ID: " + result.getId());
            System.out.println("🔗 页面链接: " + result.getCompleteUrl());
            System.out.println("📝 页面标题: " + result.getTitle());
            System.out.println("📄 空间: " + (result.getSpace() != null ? result.getSpace().getKey() : "未知"));
            return result;
        } else {
            handleErrorResponse(response, "创建页面");
            throw new IOException("创建 Confluence 页面失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 快速创建页面（使用默认设置）
     * 
     * @param title 页面标题
     * @param content 页面内容（HTML格式）
     * @return 创建结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "快速创建 Confluence 页面，使用默认空间设置")
    public ConfluenceResponseDTO createPageQuick(String title, String content) throws IOException {
        return createPage(title, content, null, null);
    }

    /**
     * 创建 Markdown 页面（自动转换为 Confluence Storage 格式）
     * 
     * @param title 页面标题
     * @param markdownContent Markdown 格式的页面内容
     * @param spaceKey 空间Key（可选）
     * @param parentId 父页面ID（可选）
     * @return 创建结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "创建 Markdown 页面，自动转换为 Confluence 格式")
    public ConfluenceResponseDTO createMarkdownPage(String title, String markdownContent, String spaceKey, String parentId) throws IOException {
        System.out.println("=== 开始 Markdown 转换 ===");
        System.out.println("原始 Markdown 长度: " + (markdownContent != null ? markdownContent.length() : 0) + " 字符");
        
        // 将 Markdown 转换为 Confluence Storage 格式
        String confluenceContent = convertMarkdownToConfluenceStorage(markdownContent);
        
        System.out.println("转换后内容长度: " + (confluenceContent != null ? confluenceContent.length() : 0) + " 字符");
        System.out.println("✅ Markdown 转换完成");
        
        // 创建页面
        return createPage(title, confluenceContent, spaceKey, parentId);
    }

    /**
     * 快速创建 Markdown 页面
     * 
     * @param title 页面标题
     * @param markdownContent Markdown 格式的页面内容
     * @return 创建结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "快速创建 Markdown 页面，使用默认设置")
    public ConfluenceResponseDTO createMarkdownPageQuick(String title, String markdownContent) throws IOException {
        return createMarkdownPage(title, markdownContent, null, null);
    }

    /**
     * 更新页面内容
     * 
     * @param pageId 页面ID
     * @param title 新标题
     * @param content 新内容（HTML格式）
     * @param version 当前版本号
     * @return 更新结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "更新 Confluence 页面内容")
    public ConfluenceResponseDTO updatePage(String pageId, String title, String content, Integer version) throws IOException {
        String authHeader = confluenceConfig.getAuthorizationHeader();
        
        System.out.println("=== 更新 Confluence 页面 ===");
        System.out.println("页面ID: " + pageId);
        System.out.println("新标题: " + title);
        System.out.println("内容长度: " + (content != null ? content.length() : 0) + " 字符");
        System.out.println("版本号: " + version);
        
        // 如果没有提供版本号，先获取当前页面信息
        if (version == null) {
            ConfluenceResponseDTO currentPage = getPage(pageId);
            version = currentPage.getVersion().getNumber() + 1;
            System.out.println("自动获取版本号: " + version);
        }
        
        // 创建更新请求对象
        ConfluencePageRequestDTO pageRequest = new ConfluencePageRequestDTO();
        pageRequest.setTitle(title);
        pageRequest.setBody(new ConfluencePageRequestDTO.Body(
            new ConfluencePageRequestDTO.Storage(content)
        ));
        pageRequest.setVersion(new ConfluencePageRequestDTO.Version(version));
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.updatePage(authHeader, pageId, pageRequest);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 页面更新成功！");
            System.out.println("📰 页面ID: " + result.getId());
            System.out.println("🔗 页面链接: " + result.getCompleteUrl());
            System.out.println("📝 新标题: " + result.getTitle());
            System.out.println("🔢 新版本: " + (result.getVersion() != null ? result.getVersion().getNumber() : "未知"));
            return result;
        } else {
            handleErrorResponse(response, "更新页面");
            throw new IOException("更新 Confluence 页面失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 更新 Markdown 页面
     * 
     * @param pageId 页面ID
     * @param title 新标题
     * @param markdownContent Markdown 格式的新内容
     * @param version 当前版本号（可选）
     * @return 更新结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "更新 Markdown 页面，自动转换为 Confluence 格式")
    public ConfluenceResponseDTO updateMarkdownPage(String pageId, String title, String markdownContent, Integer version) throws IOException {
        System.out.println("=== 开始更新 Markdown 页面 ===");
        System.out.println("原始 Markdown 长度: " + (markdownContent != null ? markdownContent.length() : 0) + " 字符");
        
        // 将 Markdown 转换为 Confluence Storage 格式
        String confluenceContent = convertMarkdownToConfluenceStorage(markdownContent);
        
        System.out.println("转换后内容长度: " + (confluenceContent != null ? confluenceContent.length() : 0) + " 字符");
        System.out.println("✅ Markdown 转换完成");
        
        // 更新页面
        return updatePage(pageId, title, confluenceContent, version);
    }

    /**
     * 删除页面
     * 
     * @param pageId 页面ID
     * @return 删除结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "删除 Confluence 页面")
    public String deletePage(String pageId) throws IOException {
        if (confluenceConfig.isReadOnlyMode()) {
            throw new IOException("当前处于只读模式，无法删除页面");
        }
        
        String authHeader = confluenceConfig.getAuthorizationHeader();
        
        System.out.println("=== 删除 Confluence 页面 ===");
        System.out.println("页面ID: " + pageId);
        System.out.println("⚠️ 注意：此操作不可逆");
        
        Call<Void> call = confluenceApiService.deletePage(authHeader, pageId);
        Response<Void> response = call.execute();

        if (response.isSuccessful()) {
            System.out.println("✅ 页面删除成功！");
            return "页面 " + pageId + " 已成功删除";
        } else {
            handleErrorResponse(response, "删除页面");
            throw new IOException("删除 Confluence 页面失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 获取空间列表
     * 
     * @param limit 返回数量限制
     * @param start 分页起始位置
     * @return 空间列表
     * @throws IOException 网络请求异常
     */
    @Tool(description = "获取 Confluence 空间列表")
    public ConfluenceResponseDTO getSpaces(Integer limit, Integer start) throws IOException {
        checkApiAvailable();
        
        String authHeader = confluenceConfig.getAuthorizationHeader();
        String expand = "description,homepage";
        
        System.out.println("=== 获取空间列表 ===");
        System.out.println("返回数量: " + (limit != null ? limit : "默认"));
        System.out.println("起始位置: " + (start != null ? start : 0));
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.getSpaces(authHeader, limit, start, expand);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 获取空间列表成功");
            System.out.println("返回空间数: " + (result.getResults() != null ? result.getResults().size() : 0));
            return result;
        } else {
            handleErrorResponse(response, "获取空间列表");
            throw new IOException("获取 Confluence 空间列表失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 为页面添加标签
     * 
     * @param pageId 页面ID
     * @param label 标签名称
     * @return 添加结果
     * @throws IOException 网络请求异常
     */
    @Tool(description = "为 Confluence 页面添加标签")
    public ConfluenceResponseDTO addPageLabel(String pageId, String label) throws IOException {
        String authHeader = confluenceConfig.getAuthorizationHeader();
        
        System.out.println("=== 添加页面标签 ===");
        System.out.println("页面ID: " + pageId);
        System.out.println("标签: " + label);
        
        // 构建标签数据
        Map<String, Object> labelData = new HashMap<>();
        labelData.put("prefix", "global");
        labelData.put("name", label);
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.addPageLabel(authHeader, pageId, labelData);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 标签添加成功！");
            return result;
        } else {
            handleErrorResponse(response, "添加标签");
            throw new IOException("添加 Confluence 页面标签失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 获取当前用户信息
     * 
     * @return 用户信息
     * @throws IOException 网络请求异常
     */
    @Tool(description = "获取当前 Confluence 用户信息")
    public ConfluenceResponseDTO getCurrentUser() throws IOException {
        checkApiAvailable();
        
        String authHeader = confluenceConfig.getAuthorizationHeader();
        
        System.out.println("=== 获取当前用户信息 ===");
        
        Call<ConfluenceResponseDTO> call = confluenceApiService.getCurrentUser(authHeader);
        Response<ConfluenceResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ConfluenceResponseDTO result = response.body();
            System.out.println("✅ 获取用户信息成功");
            return result;
        } else {
            handleErrorResponse(response, "获取用户信息");
            throw new IOException("获取 Confluence 用户信息失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 处理错误响应
     */
    private void handleErrorResponse(Response<?> response, String operation) throws IOException {
        System.err.println("❌ " + operation + "失败: HTTP " + response.code());
        System.err.println("错误信息: " + response.message());
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                System.err.println("错误详情: " + errorBody);
            } catch (IOException e) {
                System.err.println("无法读取错误详情: " + e.getMessage());
            }
        }
    }
} 