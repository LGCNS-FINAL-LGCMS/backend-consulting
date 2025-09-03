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
public class LecturerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Column(columnDefinition = "TEXT")
    private String reviewAnalysisResult;

    @Column(columnDefinition = "TEXT")
    private String qnaAnalysisResult;

    @Column(columnDefinition = "TEXT")
    private String overallAnalysisResult;

    private LocalDateTime createdAt;

    @PrePersist
    public void setDate(){
        this.createdAt = LocalDateTime.now();
    }
}
