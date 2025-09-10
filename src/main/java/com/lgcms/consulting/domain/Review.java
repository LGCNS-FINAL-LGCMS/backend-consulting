package com.lgcms.consulting.domain;

import jakarta.persistence.Column;
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

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    private Integer star;

    private String nickname;

    private Integer difficulty;

    private Integer usefulness;

    private LocalDateTime createdAt;
}
