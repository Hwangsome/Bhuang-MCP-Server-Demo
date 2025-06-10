package com.bhuang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Confluence 页面创建/更新请求 DTO
 */
public class ConfluencePageRequestDTO {
    
    private String type = "page";
    private String title;
    private Space space;
    private Body body;
    private Ancestor[] ancestors;
    private Version version;

    public ConfluencePageRequestDTO() {}

    public ConfluencePageRequestDTO(String title, String content, String spaceKey) {
        this.title = title;
        this.space = new Space(spaceKey);
        this.body = new Body(content);
    }

    public ConfluencePageRequestDTO(String title, String content, String spaceKey, String parentId) {
        this(title, content, spaceKey);
        if (parentId != null) {
            this.ancestors = new Ancestor[]{new Ancestor(parentId)};
        }
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Ancestor[] getAncestors() {
        return ancestors;
    }

    public void setAncestors(Ancestor[] ancestors) {
        this.ancestors = ancestors;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    // Inner Classes
    public static class Space {
        private String key;

        public Space() {}

        public Space(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class Body {
        private Storage storage;

        public Body() {}

        public Body(String content) {
            this.storage = new Storage(content);
        }

        public Body(Storage storage) {
            this.storage = storage;
        }

        public Storage getStorage() {
            return storage;
        }

        public void setStorage(Storage storage) {
            this.storage = storage;
        }
    }

    public static class Storage {
        private String value;
        private String representation = "storage";

        public Storage() {}

        public Storage(String value) {
            this.value = value;
        }

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

    public static class Ancestor {
        private String id;

        public Ancestor() {}

        public Ancestor(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Version {
        private int number;

        public Version() {}

        public Version(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
} 