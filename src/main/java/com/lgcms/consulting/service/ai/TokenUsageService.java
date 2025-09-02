package com.lgcms.consulting.service.ai;

import com.lgcms.consulting.domain.TokenUsage;
import com.lgcms.consulting.repository.TokenUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenUsageService {
    private final TokenUsageRepository tokenUsageRepository;

    @Transactional
    public void saveTokenUsage(ChatResponse response, String methodName) {
        Usage usage = response.getMetadata().getUsage();
        tokenUsageRepository.save(
                TokenUsage.builder()
                        .methodName(methodName)
                        .inputTokens(usage.getPromptTokens())
                        .outputTokens(usage.getCompletionTokens())
                        .totalTokens(usage.getTotalTokens())
                        .build()
        );
    }
}
