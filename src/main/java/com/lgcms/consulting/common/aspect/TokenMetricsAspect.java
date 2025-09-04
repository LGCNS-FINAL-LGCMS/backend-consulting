package com.lgcms.consulting.common.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
//import org.springframework.ai.bedrock.titan.api.TitanEmbeddingBedrockApi;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenMetricsAspect {
    private final MeterRegistry meterRegistry;

    @Around("execution(* org.springframework.ai.chat.model.ChatModel.call(..))")
    public Object recordTokenMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if(result instanceof ChatResponse chatResponse) {
            Usage usage = chatResponse.getMetadata().getUsage();
            Integer totalTokens = usage.getTotalTokens();

            Counter.builder("ai.tokens")
                    .description("LLM token usage")
                    .register(meterRegistry)
                    .increment(totalTokens);
        }

        return result;
    }

//    @Around("execution(* org.springframework.ai.bedrock.titan.api.TitanEmbeddingBedrockApi.embedding(..))")
//    public Object recordEmbeddingTokenMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object result = joinPoint.proceed();
//
//        if(result instanceof TitanEmbeddingBedrockApi.TitanEmbeddingResponse titanEmbeddingResponse) {
//            Integer token = titanEmbeddingResponse.inputTextTokenCount();
//
//            Counter.builder("ai.tokens")
//                    .description("LLM token usage")
//                    .register(meterRegistry)
//                    .increment(token);
//        }
//
//        return result;
//    }
}