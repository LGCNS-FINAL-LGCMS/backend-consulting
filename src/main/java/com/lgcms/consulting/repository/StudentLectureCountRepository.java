package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.StudentLectureCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentLectureCountRepository extends JpaRepository<StudentLectureCount, Long> {
    List<StudentLectureCount> findByMemberId(Long id);
}
