package com.lgcms.consulting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodName;

    private Integer inputTokens;

    private Integer outputTokens;

    private Integer totalTokens;

    private LocalDateTime createdAt;

    @PrePersist
    public void setDate(){
        this.createdAt = LocalDateTime.now();
    }
}
