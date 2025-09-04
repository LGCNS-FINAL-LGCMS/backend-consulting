package com.lgcms.consulting.service.ai;

import com.lgcms.consulting.common.annotation.TokenMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LlmCallService {
    private final ChatClient chatClient;

    @TokenMetrics
    ChatResponse getResponseWithTool(String systemPrompt, String userPrompt , Object tools, Map<String, Object> context) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .tools(tools)
                .toolContext(context)
                .call()
                .chatResponse();
    }
    @TokenMetrics
    public ChatResponse getResponseWithTool(String prompt, Object tools, Map<String, Object> context) {
        return chatClient.prompt(prompt)
                .tools(tools)
                .toolContext(context)
                .call()
                .chatResponse();
    }
}
