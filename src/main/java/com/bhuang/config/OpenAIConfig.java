//package com.bhuang.config;
//
//import io.micrometer.observation.ObservationRegistry;
//import io.modelcontextprotocol.client.McpSyncClient;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.DefaultChatClientBuilder;
//import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
//import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.OpenAiEmbeddingModel;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.ai.tool.ToolCallback;
//import org.springframework.ai.tool.ToolCallbackProvider;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Configuration
//public class OpenAIConfig {
//
//    @Value("${spring.ai.openai.base-url}")
//    private String openAiBaseUrl;
//
//    @Value("${spring.ai.openai.api-key}")
//    private String openAiApiKey;
//
//    @Bean
//    public OpenAiApi openAiApi() {
//        return
//                OpenAiApi.builder()
//                        .baseUrl(openAiBaseUrl)
//                        .apiKey(openAiApiKey)
//                        .build();
//    }
//
//    @Bean
//    public OpenAiChatModel openAiChatModel(OpenAiApi openAiApi, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {
//        return OpenAiChatModel.builder()
//                .openAiApi(openAiApi)
//                .defaultOptions(
//                        OpenAiChatOptions.builder()
//                                .model("deepseek-chat")
//                                .toolCallbacks(syncMcpToolCallbackProvider.getToolCallbacks())
//                                .build()
//                )
//                .build();
//    }
//
//}
