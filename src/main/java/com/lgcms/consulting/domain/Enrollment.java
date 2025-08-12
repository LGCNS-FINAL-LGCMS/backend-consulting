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
public class Enrollment {

    @Id
    private Long id;

    private Long studentId;

    private Long lectureId;

    private LocalDateTime enrollmentAt;
}
