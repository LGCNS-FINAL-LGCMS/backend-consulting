package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Review;
import com.lgcms.consulting.dto.projection.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(value = """
            SELECT r.suggestion, r.star, r.difficulty, r.usefulness, l.title
            FROM review r JOIN lecture l ON r.lecture_id = l.id
            WHERE l.member_id = :memberId
            ORDER BY r.created_at DESC
            LIMIT 30
            """, nativeQuery = true)
    List<ReportDTO.ReviewData> findByLecturerId(
            @Param("memberId") Long lecturerId
    );
}