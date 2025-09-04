package com.lgcms.consulting.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenMetrics {
    String value() default "ai.tokens";
    String description() default "Token usage metrics";
}
