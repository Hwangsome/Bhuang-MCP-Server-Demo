package com.bhuang;

import com.bhuang.service.WeatherService;
import com.bhuang.service.CSDNService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class BhuangMcpServerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BhuangMcpServerDemoApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }


    @Bean
    public ToolCallbackProvider csdnTools(CSDNService csdnService) {
        return MethodToolCallbackProvider.builder().toolObjects(csdnService).build();
    }

}