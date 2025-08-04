package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Progress;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.CompleteProgressTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    @Query(value = """
            SELECT l.title,
                ROUND(COUNT(CASE WHEN p.progress_rate > 90 THEN 1 END) * 100.0 / COUNT(*)) AS complete_progress
                        FROM progress p
                        JOIN Lecture l ON p.lecture_id = l.id
                        WHERE l.member_id = :memberId
                        GROUP BY l.title
            """, nativeQuery = true)
    List<CompleteProgressTransfer> findCompleteProgressByMemberId(@Param("memberId") Long memberId);
}
