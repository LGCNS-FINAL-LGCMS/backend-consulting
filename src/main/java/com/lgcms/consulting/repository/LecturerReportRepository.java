package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.LecturerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LecturerReportRepository extends JpaRepository<LecturerReport, Long> {

    @Query(value = """
            SELECT * FROM lecturer_report AS lr WHERE lr.member_id = :memberId AND DATE(lr.created_at) = DATE(:createdAt)
            """, nativeQuery = true)
    LecturerReport findByMemberIdAndDate(
            @Param("memberId") Long memberId,
            @Param("createdAt") LocalDateTime createdAt
    );
}
