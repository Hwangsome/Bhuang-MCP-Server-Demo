package com.bhuang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSDN文章发布响应DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleResponseDTO {
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleResponseDTO.class);

    private Integer code;
    
    @JsonProperty("traceId")
    private String traceId;
    
    // 使用Object类型来处理可能是字符串或对象的情况
    private Object data;
    
    private String msg;
    
    // 新增：标记内容是否经过Markdown转换
    private boolean markdownConverted = false;
    
    // 新增：存储原始Markdown内容（如果有）
    private String originalMarkdownContent;

    // 内部类：文章数据
    public static class ArticleData {
        private String url;
        
        @JsonProperty("article_id")
        private Long articleId;
        
        private String title;
        
        private String description;

        // Getter和Setter方法
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public Long getArticleId() { return articleId; }
        public void setArticleId(Long articleId) { this.articleId = articleId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "ArticleData{" +
                    "url='" + url + '\'' +
                    ", articleId=" + articleId +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    // Getter和Setter方法
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    /**
     * 获取文章数据对象，如果data是字符串则返回null
     */
    public ArticleData getArticleData() {
        if (data == null) {
            return null;
        }
        
        try {
            // 如果data是LinkedHashMap等对象，转换为ArticleData
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(data, ArticleData.class);
        } catch (Exception e) {
            logger.error("转换ArticleData失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取data字段的字符串值，如果data是对象则返回其toString
     */
    public String getDataAsString() {
        if (data == null) {
            return null;
        }
        return data.toString();
    }
    
    /**
     * 判断data是否为字符串类型
     */
    public boolean isDataString() {
        return data instanceof String;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    // 新增：Markdown相关的getter/setter方法
    public boolean isMarkdownConverted() { return markdownConverted; }
    public void setMarkdownConverted(boolean markdownConverted) { this.markdownConverted = markdownConverted; }

    public String getOriginalMarkdownContent() { return originalMarkdownContent; }
    public void setOriginalMarkdownContent(String originalMarkdownContent) { 
        this.originalMarkdownContent = originalMarkdownContent; 
    }

    /**
     * 标记此响应是从Markdown转换而来
     * 
     * @param originalMarkdown 原始Markdown内容
     */
    public void markAsMarkdownConverted(String originalMarkdown) {
        this.markdownConverted = true;
        this.originalMarkdownContent = originalMarkdown;
    }

    /**
     * 获取内容类型描述
     * 
     * @return 内容类型（HTML或Markdown转换后的HTML）
     */
    public String getContentType() {
        return markdownConverted ? "HTML (Markdown转换)" : "HTML";
    }

    @Override
    public String toString() {
        return "ArticleResponseDTO{" +
                "code=" + code +
                ", traceId='" + traceId + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", markdownConverted=" + markdownConverted +
                ", contentType='" + getContentType() + '\'' +
                '}';
    }
} 