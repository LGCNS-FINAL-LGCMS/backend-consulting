package com.lgcms.consulting.config.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.bedrock.converse.BedrockProxyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {
    private final BedrockProxyChatModel bedrockProxyChatModel;

    @Bean
    public ToolCallingChatOptions bedrockChatOptions() {
        return ToolCallingChatOptions.builder()
                .model("anthropic.claude-3-haiku-20240307-v1:0")
                .temperature(0.3)
                .maxTokens(2000)
                .build();
    }

    @Bean
    public ChatClient bedrockChatClient() {
        return ChatClient.builder(bedrockProxyChatModel)
                .defaultOptions(bedrockChatOptions())
                .build();
    }
}
