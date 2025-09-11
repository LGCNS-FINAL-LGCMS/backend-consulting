package com.lgcms.consulting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate_group", unique = true)
    private String rateGroup;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "member_id", unique = true)
    private Long memberId;

    private Long studentCount;
}
