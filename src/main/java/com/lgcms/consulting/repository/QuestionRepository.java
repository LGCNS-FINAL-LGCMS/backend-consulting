package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Question;
import com.lgcms.consulting.dto.projection.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = """
            SELECT q.title, q.content
            FROM question q JOIN lecture l ON l.id = q.lecture_id
            WHERE l.member_id = :memberId
            LIMIT 30
            """, nativeQuery = true)
    List<ReportDTO.QuestionData> findByLecturerId(
            @Param("memberId") Long lecturerId
    );
}
