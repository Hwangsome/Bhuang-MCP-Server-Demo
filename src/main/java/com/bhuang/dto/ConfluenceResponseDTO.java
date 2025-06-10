package com.bhuang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Confluence API 响应 DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfluenceResponseDTO {
    
    private String id;
    private String type;
    private String status;
    private String title;
    private Space space;
    private Version version;
    private Body body;
    private List<Ancestor> ancestors;
    private Links links;
    private Extensions extensions;
    
    // 用于搜索结果
    private List<ConfluenceResponseDTO> results;
    private int start;
    private int limit;
    private int size;
    
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public List<Ancestor> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<Ancestor> ancestors) {
        this.ancestors = ancestors;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public List<ConfluenceResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<ConfluenceResponseDTO> results) {
        this.results = results;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // Inner Classes
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Space {
        private String id;
        private String key;
        private String name;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Version {
        private int number;
        private String when;
        private String message;
        private User by;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getWhen() {
            return when;
        }

        public void setWhen(String when) {
            this.when = when;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public User getBy() {
            return by;
        }

        public void setBy(User by) {
            this.by = by;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Storage storage;
        private View view;

        public Storage getStorage() {
            return storage;
        }

        public void setStorage(Storage storage) {
            this.storage = storage;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Storage {
        private String value;
        private String representation;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getRepresentation() {
            return representation;
        }

        public void setRepresentation(String representation) {
            this.representation = representation;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class View {
        private String value;
        private String representation;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getRepresentation() {
            return representation;
        }

        public void setRepresentation(String representation) {
            this.representation = representation;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        private String webui;
        private String editui;
        private String tinyui;
        private String collection;
        private String base;
        private String context;

        public String getWebui() {
            return webui;
        }

        public void setWebui(String webui) {
            this.webui = webui;
        }

        public String getEditui() {
            return editui;
        }

        public void setEditui(String editui) {
            this.editui = editui;
        }

        public String getTinyui() {
            return tinyui;
        }

        public void setTinyui(String tinyui) {
            this.tinyui = tinyui;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Extensions {
        private String position;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private String type;
        private String username;
        private String userKey;
        private String displayName;
        private String accountId;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserKey() {
            return userKey;
        }

        public void setUserKey(String userKey) {
            this.userKey = userKey;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ancestor {
        private String id;
        private String type;
        private String status;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    
    /**
     * 获取页面的完整 URL
     * 
     * @return 完整的页面 URL
     */
    public String getCompleteUrl() {
        if (links != null && links.getBase() != null && links.getWebui() != null) {
            return links.getBase() + links.getWebui();
        }
        return null;
    }

    /**
     * 获取页面内容（优先返回 view 格式，其次 storage 格式）
     * 
     * @return 页面内容
     */
    public String getContent() {
        if (body != null) {
            if (body.getView() != null && body.getView().getValue() != null) {
                return body.getView().getValue();
            } else if (body.getStorage() != null && body.getStorage().getValue() != null) {
                return body.getStorage().getValue();
            }
        }
        return null;
    }
} 