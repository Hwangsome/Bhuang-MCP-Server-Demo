package com.bhuang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

/**
 * CSDN API配置类
 * 支持通过环境变量 CSDN_COOKIE 和 CSDN_CATEGORIES 覆盖默认配置
 */
@Component
@ConfigurationProperties(prefix = "csdn.api")
public class CSDNConfig {

    private static final Logger logger = LoggerFactory.getLogger(CSDNConfig.class);
    
    private String cookie;
    private String categories;
    
    /**
     * 配置加载完成后的初始化
     */
    @PostConstruct
    public void init() {
        logger.info("=== CSDN 配置加载完成 ===");
        logger.info("Categories: " + categories);
        logger.info("Cookie长度: " + (cookie != null ? cookie.length() : 0));
        
        // 环境变量说明
        logger.info("环境变量支持:");
        logger.info("  - CSDN_COOKIE: 覆盖 CSDN 认证 Cookie");
        logger.info("  - CSDN_CATEGORIES: 覆盖文章分类标签");
        logger.info("  - 如果环境变量未设置，将使用配置文件中的默认值");
    }
    
    /**
     * 获取Cookie
     */
    public String getCookie() {
        return cookie;
    }
    
    /**
     * 设置Cookie
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
    
    /**
     * 获取分类
     */
    public String getCategories() {
        return categories;
    }
    
    /**
     * 设置分类
     */
    public void setCategories(String categories) {
        this.categories = categories;
    }
    
    /**
     * 获取配置摘要信息
     */
    public String getConfigSummary() {
        return String.format("CSDN配置 [Categories: %s, Cookie长度: %d]",
                categories, cookie != null ? cookie.length() : 0);
    }
    
    @Override
    public String toString() {
        return "CSDNConfig{" +
                "cookie='" + (cookie != null ? "[已设置-长度:" + cookie.length() + "]" : "null") + '\'' +
                ", categories='" + categories + '\'' +
                '}';
    }
} 