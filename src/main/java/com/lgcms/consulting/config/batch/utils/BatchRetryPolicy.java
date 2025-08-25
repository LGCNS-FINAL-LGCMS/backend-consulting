package com.lgcms.consulting.config.batch.utils;

import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BatchRetryPolicy {

    public SimpleRetryPolicy retryPolicy() {

        return new SimpleRetryPolicy(3, Map.of(RuntimeException.class, true));
    }

    public ExponentialBackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000L);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(10000L);
        return backOffPolicy;
    }
}
