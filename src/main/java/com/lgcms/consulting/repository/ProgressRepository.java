package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
