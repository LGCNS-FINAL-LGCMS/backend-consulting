package com.lgcms.consulting.service.ai;

import com.lgcms.consulting.service.ai.tools.AgentTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.lgcms.consulting.service.ai.Prompts.REPORT_SYSTEM_PROMPT;
import static com.lgcms.consulting.service.ai.Prompts.REPORT_USER_PROMPT;

@Service
@RequiredArgsConstructor
public class BedrockService implements AiService {
    private final ChatClient chatClient;
    private final AgentTools agentTools;

    @Override
    public String getReport(Long memberId) {
        String systemPrompt = REPORT_SYSTEM_PROMPT.message;
        String userPrompt = REPORT_USER_PROMPT.message;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(30);

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .tools(agentTools)
                .toolContext(Map.of(
                        "memberId", memberId,
                        "startDate", startDate,
                        "endDate", now
                ))
                .call()
                .content();
    }
}
