package com.lgcms.consulting.common.aspect;

import com.lgcms.consulting.common.annotation.TokenMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenMetricsAspect {
    private final MeterRegistry meterRegistry;

    @Around("@annotation(tokenMetrics)")
    public Object recordTokenMetrics(ProceedingJoinPoint joinPoint, TokenMetrics tokenMetrics) throws Throwable {
        Object result = joinPoint.proceed();

        if(result instanceof ChatResponse chatResponse) {
            Usage usage = chatResponse.getMetadata().getUsage();
            Integer totalTokens = usage.getTotalTokens();

            Counter.builder(tokenMetrics.value())
                    .description(tokenMetrics.description())
                    .register(meterRegistry)
                    .increment(totalTokens);
        }

        return result;
    }
}