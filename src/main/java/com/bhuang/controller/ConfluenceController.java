package com.bhuang.controller;

import com.bhuang.dto.ConfluencePageRequestDTO;
import com.bhuang.dto.ConfluenceResponseDTO;
import com.bhuang.service.ConfluenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Confluence REST API 控制器
 */
@RestController
@RequestMapping("/api/confluence")
public class ConfluenceController {

    @Autowired
    private ConfluenceService confluenceService;

    /**
     * 搜索 Confluence 内容
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchContent(
            @RequestParam String query,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer start) {
        try {
            ConfluenceResponseDTO result = confluenceService.searchContent(query, limit, start);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 获取页面详情
     */
    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPage(@PathVariable String pageId) {
        try {
            ConfluenceResponseDTO result = confluenceService.getPage(pageId);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("获取页面失败: " + e.getMessage());
        }
    }

    /**
     * 创建新页面
     */
    @PostMapping("/page")
    public ResponseEntity<?> createPage(@RequestBody ConfluencePageRequestDTO pageRequest) {
        try {
            String content = pageRequest.getBody() != null && pageRequest.getBody().getStorage() != null ? 
                           pageRequest.getBody().getStorage().getValue() : "";
            String spaceKey = pageRequest.getSpace() != null ? pageRequest.getSpace().getKey() : null;
            String parentId = pageRequest.getAncestors() != null && pageRequest.getAncestors().length > 0 ? 
                            pageRequest.getAncestors()[0].getId() : null;
            
            ConfluenceResponseDTO result = confluenceService.createPage(
                pageRequest.getTitle(), content, spaceKey, parentId);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("创建页面失败: " + e.getMessage());
        }
    }

    /**
     * 快速创建页面
     */
    @PostMapping("/page/quick")
    public ResponseEntity<?> createPageQuick(
            @RequestParam String title,
            @RequestParam String content) {
        try {
            ConfluenceResponseDTO result = confluenceService.createPageQuick(title, content);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("快速创建页面失败: " + e.getMessage());
        }
    }

    /**
     * 创建 Markdown 页面
     */
    @PostMapping("/page/markdown")
    public ResponseEntity<?> createMarkdownPage(
            @RequestParam String title,
            @RequestParam String markdownContent,
            @RequestParam(required = false) String spaceKey,
            @RequestParam(required = false) String parentId) {
        try {
            ConfluenceResponseDTO result = confluenceService.createMarkdownPage(
                title, markdownContent, spaceKey, parentId);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("创建 Markdown 页面失败: " + e.getMessage());
        }
    }

    /**
     * 快速创建 Markdown 页面
     */
    @PostMapping("/page/markdown/quick")
    public ResponseEntity<?> createMarkdownPageQuick(
            @RequestParam String title,
            @RequestParam String markdownContent) {
        try {
            ConfluenceResponseDTO result = confluenceService.createMarkdownPageQuick(title, markdownContent);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("快速创建 Markdown 页面失败: " + e.getMessage());
        }
    }

    /**
     * 更新页面
     */
    @PutMapping("/page/{pageId}")
    public ResponseEntity<?> updatePage(
            @PathVariable String pageId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Integer version) {
        try {
            ConfluenceResponseDTO result = confluenceService.updatePage(pageId, title, content, version);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("更新页面失败: " + e.getMessage());
        }
    }

    /**
     * 更新 Markdown 页面
     */
    @PutMapping("/page/{pageId}/markdown")
    public ResponseEntity<?> updateMarkdownPage(
            @PathVariable String pageId,
            @RequestParam String title,
            @RequestParam String markdownContent,
            @RequestParam(required = false) Integer version) {
        try {
            ConfluenceResponseDTO result = confluenceService.updateMarkdownPage(
                pageId, title, markdownContent, version);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("更新 Markdown 页面失败: " + e.getMessage());
        }
    }

    /**
     * 删除页面
     */
    @DeleteMapping("/page/{pageId}")
    public ResponseEntity<?> deletePage(@PathVariable String pageId) {
        try {
            String result = confluenceService.deletePage(pageId);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("删除页面失败: " + e.getMessage());
        }
    }

    /**
     * 获取空间列表
     */
    @GetMapping("/spaces")
    public ResponseEntity<?> getSpaces(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer start) {
        try {
            ConfluenceResponseDTO result = confluenceService.getSpaces(limit, start);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("获取空间列表失败: " + e.getMessage());
        }
    }

    /**
     * 为页面添加标签
     */
    @PostMapping("/page/{pageId}/label")
    public ResponseEntity<?> addPageLabel(
            @PathVariable String pageId,
            @RequestParam String label) {
        try {
            ConfluenceResponseDTO result = confluenceService.addPageLabel(pageId, label);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("添加标签失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/current")
    public ResponseEntity<?> getCurrentUser() {
        try {
            ConfluenceResponseDTO result = confluenceService.getCurrentUser();
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("获取用户信息失败: " + e.getMessage());
        }
    }
} 