package com.lgcms.consulting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    private Long id;

    private Long memberId;

    private Long lectureId;

    private String content;

    private String star;

    private String nickname;

    private String details;

    private String etc;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
