package com.a7.job.resume.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiAiConfig {
    @Bean
    public ChatClient chatModel(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }
}
