package com.bhuang.service;

import com.bhuang.dto.ArticleRequestDTO;
import com.bhuang.dto.ArticleResponseDTO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * CSDN API Retrofit接口
 */
public interface CSDNApiService {

    /**
     * 发布/保存文章到CSDN
     * 
     * @param cookie Cookie头信息
     * @param articleRequest 文章请求数据
     * @return 文章发布响应
     */
    @POST("blog-console-api/v1/postedit/saveArticle")
    @Headers({
        "accept: application/json, text/plain, */*",
        "accept-language: en,zh-CN;q=0.9,zh;q=0.8,hy;q=0.7",
        "content-type: application/json;",
        "origin: https://mpbeta.csdn.net",
        "priority: u=1, i",
        "referer: https://mpbeta.csdn.net/",
        "sec-ch-ua: \"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"",
        "sec-ch-ua-mobile: ?0",
        "sec-ch-ua-platform: \"macOS\"",
        "sec-fetch-dest: empty",
        "sec-fetch-mode: cors",
        "sec-fetch-site: same-site",
        "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36",
        "x-ca-key: 203803574",
        "x-ca-nonce: 17731954-35a9-40e3-94c8-78489bd4f93d",
        "x-ca-signature: 53+fXIO90fW95gbK2vG+J/CC50NLAAAPQNG2/fFTwy8=",
        "x-ca-signature-headers: x-ca-key,x-ca-nonce"
    })
    Call<ArticleResponseDTO> publishArticle(
            @Header("Cookie") String cookie,
            @Body ArticleRequestDTO articleRequest
    );
} 