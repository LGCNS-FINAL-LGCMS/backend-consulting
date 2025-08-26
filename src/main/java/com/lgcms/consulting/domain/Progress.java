package com.lgcms.consulting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
    @Id
    private Long id;

    private Long memberId;

    private String lectureId;

    private Long progressRate;
}
