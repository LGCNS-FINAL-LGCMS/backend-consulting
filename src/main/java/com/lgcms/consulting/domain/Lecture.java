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
public class Lecture {
    @Id
    private Long id;

    private Long memberId;

    private String title;

    private String level;

    private Long price;

    private Long avgRating;

    private Long reviewCount;

    private Long totalAmount;
}
