package com.lgcms.consulting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    private Long id;

    private Long memberId;

    private String lectureId;

    private String title;

    private String content;

    private LocalDateTime createdAt;
}
