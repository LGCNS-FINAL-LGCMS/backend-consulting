package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
