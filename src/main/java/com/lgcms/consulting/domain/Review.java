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
public class Review {

    @Id
    private Long id;

    private Long memberId;

    private String lectureId;

    private String suggestion;

    private Long star;

    private String nickname;

    private Integer difficulty;

    private Integer usefulness;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
