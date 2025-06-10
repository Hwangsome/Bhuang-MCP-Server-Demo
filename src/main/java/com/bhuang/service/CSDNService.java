package com.bhuang.service;

import com.bhuang.config.CSDNConfig;
import com.bhuang.dto.ArticleRequestDTO;
import com.bhuang.dto.ArticleResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

/**
 * CSDN文章发布服务
 */
@Service
public class CSDNService {

    private static final Logger logger = LoggerFactory.getLogger(CSDNService.class);

    @Autowired
    private CSDNConfig csdnConfig;

    private CSDNApiService csdnApiService;
    private static final String BASE_URL = "https://bizapi.csdn.net/";
    
    // Markdown 解析器
    private Parser markdownParser;
    private HtmlRenderer htmlRenderer;

    @PostConstruct
    public void init() {
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        csdnApiService = retrofit.create(CSDNApiService.class);
        
        // 初始化 Markdown 解析器
        MutableDataSet options = new MutableDataSet();
        markdownParser = Parser.builder(options).build();
        htmlRenderer = HtmlRenderer.builder(options).build();
    }

    /**
     * 将 Markdown 内容转换为 HTML
     * 
     * @param markdown Markdown 格式的内容
     * @return HTML 格式的内容
     */
    private String convertMarkdownToHtml(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }
        
        try {
            Node document = markdownParser.parse(markdown);
            return htmlRenderer.render(document);
        } catch (Exception e) {
            logger.error("Markdown 转换失败: " + e.getMessage());
            // 如果转换失败，返回原始内容
            return "<p>" + markdown.replace("\n", "</p>\n<p>") + "</p>";
        }
    }

    private ArticleResponseDTO publishArticle(String cookie, ArticleRequestDTO articleRequest) throws IOException {
        // 如果没有传入cookie，尝试从配置中获取
        if (cookie == null || cookie.trim().isEmpty()) {
            cookie = csdnConfig.getCookie();
        }

        // 如果没有设置categories，从配置中获取
        if (articleRequest.getCategories() == null || articleRequest.getCategories().trim().isEmpty()) {
            articleRequest.setCategories(csdnConfig.getCategories());
        }

        logger.info("标题: " + articleRequest.getTitle());
        logger.info("内容长度: " + (articleRequest.getContent() != null ? articleRequest.getContent().length() : 0) + " 字符");
        logger.info("标签: " + articleRequest.getTags());
        logger.info("分类: " + articleRequest.getCategories());
        logger.info("状态: " + (articleRequest.getStatus() == 0 ? "发布" : "草稿"));
        logger.info("Cookie长度: " + (cookie != null ? cookie.length() : 0));

        Call<ArticleResponseDTO> call = csdnApiService.publishArticle(cookie, articleRequest);
        Response<ArticleResponseDTO> response = call.execute();

        if (response.isSuccessful()) {
            ArticleResponseDTO result = response.body();
            logger.info("=== 发布结果 ===");
            logger.info("响应码: " + result.getCode());
            logger.info("消息: " + result.getMsg());

            // 处理不同类型的data字段
            if (result.isDataString()) {
                logger.info("Data (字符串): " + result.getDataAsString());
            } else {
                ArticleResponseDTO.ArticleData articleData = result.getArticleData();
                if (articleData != null) {
                    logger.info("✅ 文章发布成功！");
                    logger.info("📰 文章ID: " + articleData.getArticleId());
                    logger.info("🔗 文章链接: " + articleData.getUrl());
                    logger.info("📝 文章标题: " + articleData.getTitle());
                    logger.info("📄 描述: " + articleData.getDescription());
                } else {
                    logger.warn("⚠️ 数据格式异常: " + result.getData());
                }
            }

            return result;
        } else {
            logger.error("❌ 请求失败: HTTP " + response.code());
            logger.error("错误信息: " + response.message());
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                logger.error("错误详情: " + errorBody);
            }
            throw new IOException("CSDN API请求失败: " + response.code() + " " + response.message());
        }
    }

    /**
     * 发布Markdown文章到CSDN
     * 
     * @param title 文章标题
     * @param markdownContent Markdown格式的文章内容
     * @param tags 文章标签（多个用英文逗号隔开）
     * @param description 文章简述
     * @return 文章发布响应
     * @throws IOException 网络请求异常
     */
    @Tool(description = "发布Markdown文章到CSDN，自动转换Markdown为HTML格式")
    public ArticleResponseDTO publishMarkdownArticle(String title, String markdownContent, String tags, String description) throws IOException {
        // 转换Markdown为HTML
        String htmlContent = convertMarkdownToHtml(markdownContent);
        // 创建文章请求对象
        ArticleRequestDTO request = new ArticleRequestDTO();
        request.setTitle(title);
        request.setContent(htmlContent);
        request.setTags(tags);
        request.setDescription(description);
        request.setCategories(csdnConfig.getCategories());
        request.setStatus(0); // 发布
        request.setType("original");
        request.setReadType("public");
        request.setCheckOriginal(false);
        request.setAuthorizedStatus(false);
        request.setSource("pc_postedit");
        request.setNotAutoSaved(1);
        request.setIsNew(1);
        request.setCoverType(1);
        
        // 发布文章
        ArticleResponseDTO response = publishArticle(csdnConfig.getCookie(), request);
        
        // 标记为Markdown转换
        if (response != null) {
            response.markAsMarkdownConverted(markdownContent);
        }
        
        return response;
    }

    /**
     * 检测内容是否为Markdown格式
     * 
     * @param content 待检测的内容
     * @return 如果是Markdown格式返回true，否则返回false
     */
    private boolean isMarkdownContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        // 检测常见的Markdown语法标记
        return content.contains("# ") ||      // 标题
               content.contains("## ") ||     // 二级标题
               content.contains("### ") ||    // 三级标题
               content.contains("**") ||      // 粗体
               content.contains("*") ||       // 斜体
               content.contains("```") ||     // 代码块
               content.contains("`") ||       // 行内代码
               content.contains("- ") ||      // 列表
               content.contains("* ") ||      // 列表
               content.contains("1. ") ||     // 有序列表
               content.contains("[") ||       // 链接
               content.contains("> ") ||      // 引用
               content.contains("---") ||     // 分割线
               content.contains("***") ||     // 分割线
               content.matches(".*\\n#{1,6}\\s+.*"); // 多行标题
    }

} 