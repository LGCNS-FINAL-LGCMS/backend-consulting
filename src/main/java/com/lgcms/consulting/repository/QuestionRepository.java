package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
