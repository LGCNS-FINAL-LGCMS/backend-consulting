package com.lgcms.consulting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
    @Id
    private Long id;

    private Long memberId;

    private Long lectureId;

    private Long progressRate;
}
