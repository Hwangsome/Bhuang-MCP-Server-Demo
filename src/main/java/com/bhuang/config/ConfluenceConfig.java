package com.bhuang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Confluence 配置类
 */
@Component
@ConfigurationProperties(prefix = "confluence")
public class ConfluenceConfig {
    
    /**
     * Confluence 服务器 URL
     */
    private String url;
    
    /**
     * 用户名 (用于 Cloud 版本)
     */
    private String username;
    
    /**
     * API Token (用于 Cloud 版本)
     */
    private String apiToken;
    
    /**
     * Personal Access Token (用于 Server/Data Center 版本)
     */
    private String personalToken;
    
    /**
     * 默认空间 KEY
     */
    private String defaultSpaceKey;
    
    /**
     * SSL 验证开关
     */
    private boolean sslVerify = true;
    
    /**
     * 空间过滤器 (逗号分隔)
     */
    private String spacesFilter;
    
    /**
     * 只读模式
     */
    private boolean readOnlyMode = false;

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getPersonalToken() {
        return personalToken;
    }

    public void setPersonalToken(String personalToken) {
        this.personalToken = personalToken;
    }

    public String getDefaultSpaceKey() {
        return defaultSpaceKey;
    }

    public void setDefaultSpaceKey(String defaultSpaceKey) {
        this.defaultSpaceKey = defaultSpaceKey;
    }

    public boolean isSslVerify() {
        return sslVerify;
    }

    public void setSslVerify(boolean sslVerify) {
        this.sslVerify = sslVerify;
    }

    public String getSpacesFilter() {
        return spacesFilter;
    }

    public void setSpacesFilter(String spacesFilter) {
        this.spacesFilter = spacesFilter;
    }

    public boolean isReadOnlyMode() {
        return readOnlyMode;
    }

    public void setReadOnlyMode(boolean readOnlyMode) {
        this.readOnlyMode = readOnlyMode;
    }
    
    /**
     * 判断是否为 Cloud 版本
     */
    public boolean isCloud() {
        return url != null && url.contains("atlassian.net");
    }
    
    /**
     * 获取认证 header
     */
    public String getAuthHeader() {
        if (personalToken != null && !personalToken.trim().isEmpty()) {
            return "Bearer " + personalToken;
        } else if (username != null && apiToken != null) {
            String credentials = username + ":" + apiToken;
            return "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        }
        return null;
    }
    
    /**
     * 获取 API 基础 URL
     */
    public String getApiBaseUrl() {
        if (url == null) return null;
        
        String baseUrl = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        
        if (isCloud()) {
            // Confluence Cloud 使用 /wiki/rest/api/ 路径
            return baseUrl + "/rest/api/";
        } else {
            // Confluence Server/Data Center 使用 /rest/api/ 路径
            return baseUrl + "/rest/api/";
        }
    }

    /**
     * 获取授权头信息（别名方法）
     * 
     * @return 授权头字符串
     */
    public String getAuthorizationHeader() {
        return getAuthHeader();
    }
} 