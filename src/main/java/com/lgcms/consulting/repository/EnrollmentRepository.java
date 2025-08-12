package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
