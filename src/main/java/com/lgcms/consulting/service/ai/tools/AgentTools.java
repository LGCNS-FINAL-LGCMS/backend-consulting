package com.lgcms.consulting.service.ai.tools;

import com.lgcms.consulting.service.ai.TokenUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import static com.lgcms.consulting.service.ai.Prompts.REVIEW_TOOL_PROMPT;
import static com.lgcms.consulting.service.ai.Prompts.QUESTION_TOOL_PROMPT;

@Component
@RequiredArgsConstructor
public class AgentTools {
    private final ChatClient chatClient;
    private final DataTools dataTools;
    private final TokenUsageService tokenUsageService;

    @Tool(name = "analyzeReviews", description = """
            Analyzes course review data including star ratings, difficulty appropriateness, usefulness scores, and textual feedback to identify satisfaction patterns and improvement areas.
            """)
    public String analyzeReviews(ToolContext toolContext) {
        String prompt = REVIEW_TOOL_PROMPT.message;
        ChatResponse response = getResponse(toolContext, prompt);
        tokenUsageService.saveTokenUsage(response, "analyzeReviews");
        return response.getResult().getOutput().getText();
    }

    @Tool(name = "analyzeQAPatterns", description = """
            Analyzes student questions to identify learning difficulties, content clarity issues, and knowledge gaps. Categorizes questions by type, detects frequency patterns, and highlights challenging topics that may require additional content or clarification.
            """)
    public String analyzeQAPatterns(ToolContext toolContext) {
        String prompt = QUESTION_TOOL_PROMPT.message;
        ChatResponse response = getResponse(toolContext, prompt);
        tokenUsageService.saveTokenUsage(response, "analyzeQAPatterns");
        return response.getResult().getOutput().getText();
    }

    public ChatResponse getResponse(ToolContext toolContext, String prompt) {
        return chatClient.prompt(prompt)
                .tools(dataTools)
                .toolContext(toolContext.getContext())
                .call()
                .chatResponse();
    }
}
