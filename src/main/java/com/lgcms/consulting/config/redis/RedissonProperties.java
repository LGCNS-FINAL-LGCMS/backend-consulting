package com.lgcms.consulting.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.data.redis.config")
@Getter
@AllArgsConstructor
public class RedissonProperties {
    private int connectionPoolSize;
    private int connectionMinimumIdleSize;
    private int subscriptionConnectionPoolSize;
    private int subscriptionConnectionMinimumIdleSize;
    private int idleConnectionTimeout;
    private int connectTimeout;
    private int timeout;
}
