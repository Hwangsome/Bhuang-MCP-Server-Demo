package com.bhuang.service;

import com.bhuang.dto.ConfluencePageRequestDTO;
import com.bhuang.dto.ConfluenceResponseDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * Confluence REST API 接口定义
 */
public interface ConfluenceApiService {

    /**
     * 搜索内容 (使用 CQL)
     */
    @GET("content/search")
    Call<ConfluenceResponseDTO> searchContent(
            @Header("Authorization") String authorization,
            @Query("cql") String cql,
            @Query("limit") Integer limit,
            @Query("start") Integer start,
            @Query("expand") String expand
    );

    /**
     * 获取页面详情
     */
    @GET("content/{pageId}")
    Call<ConfluenceResponseDTO> getPage(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Query("expand") String expand
    );

    /**
     * 获取页面详情（带展开参数）
     */
    @GET("content/{pageId}")
    Call<ConfluenceResponseDTO> getPageWithExpansions(
        @Path("pageId") String pageId,
        @Query("expand") String expand
    );

    /**
     * 获取页面的子页面
     */
    @GET("content/{pageId}/child/page")
    Call<ConfluenceResponseDTO> getPageChildren(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Query("limit") Integer limit,
            @Query("start") Integer start,
            @Query("expand") String expand
    );

    /**
     * 创建页面
     */
    @POST("content")
    Call<ConfluenceResponseDTO> createPage(
            @Header("Authorization") String authorization,
            @Body ConfluencePageRequestDTO pageRequest
    );

    /**
     * 更新页面
     */
    @PUT("content/{pageId}")
    Call<ConfluenceResponseDTO> updatePage(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Body ConfluencePageRequestDTO pageRequest
    );

    /**
     * 删除页面
     */
    @DELETE("content/{pageId}")
    Call<Void> deletePage(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId
    );

    /**
     * 获取页面标签
     */
    @GET("content/{pageId}/label")
    Call<ConfluenceResponseDTO> getPageLabels(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId
    );

    /**
     * 为页面添加标签
     */
    @POST("content/{pageId}/label")
    Call<ConfluenceResponseDTO> addPageLabel(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Body Map<String, Object> labelData
    );

    /**
     * 获取页面评论
     */
    @GET("content/{pageId}/child/comment")
    Call<ConfluenceResponseDTO> getPageComments(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Query("limit") Integer limit,
            @Query("start") Integer start,
            @Query("expand") String expand
    );

    /**
     * 添加页面评论
     */
    @POST("content")
    Call<ConfluenceResponseDTO> addPageComment(
            @Header("Authorization") String authorization,
            @Body ConfluencePageRequestDTO commentRequest
    );

    /**
     * 获取空间列表
     */
    @GET("space")
    Call<ConfluenceResponseDTO> getSpaces(
            @Header("Authorization") String authorization,
            @Query("limit") Integer limit,
            @Query("start") Integer start,
            @Query("expand") String expand
    );

    /**
     * 获取空间详情
     */
    @GET("space/{spaceKey}")
    Call<ConfluenceResponseDTO> getSpace(
            @Header("Authorization") String authorization,
            @Path("spaceKey") String spaceKey,
            @Query("expand") String expand
    );

    /**
     * 获取空间内容
     */
    @GET("content")
    Call<ConfluenceResponseDTO> getSpaceContent(
            @Header("Authorization") String authorization,
            @Query("spaceKey") String spaceKey,
            @Query("type") String type,
            @Query("limit") Integer limit,
            @Query("start") Integer start,
            @Query("expand") String expand
    );

    /**
     * 获取用户信息
     */
    @GET("user")
    Call<ConfluenceResponseDTO> getUser(
            @Header("Authorization") String authorization,
            @Query("accountId") String accountId
    );

    /**
     * 获取当前用户信息
     */
    @GET("user/current")
    Call<ConfluenceResponseDTO> getCurrentUser(
            @Header("Authorization") String authorization
    );

    /**
     * 获取页面历史版本
     */
    @GET("content/{pageId}/history")
    Call<ConfluenceResponseDTO> getPageHistory(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Query("limit") Integer limit,
            @Query("start") Integer start
    );

    /**
     * 获取页面附件
     */
    @GET("content/{pageId}/child/attachment")
    Call<ConfluenceResponseDTO> getPageAttachments(
            @Header("Authorization") String authorization,
            @Path("pageId") String pageId,
            @Query("limit") Integer limit,
            @Query("start") Integer start,
            @Query("expand") String expand
    );
} 