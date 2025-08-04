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

    @Query(value = """
            SELECT
                CASE
                    WHEN progress_rate < 10 THEN '0~9%'
                    WHEN progress_rate < 20 THEN '10~19%'
                    WHEN progress_rate < 30 THEN '20~29%'
                    WHEN progress_rate < 40 THEN '30~39%'
                    WHEN progress_rate < 50 THEN '40~49%'
                    WHEN progress_rate < 60 THEN '50~59%'
                    WHEN progress_rate < 70 THEN '60~69%'
                    WHEN progress_rate < 80 THEN '70~79%'
                    WHEN progress_rate < 90 THEN '80~89%'
                    ELSE '90~100%'
                    END AS rate_group,
                COUNT(*) AS student_count
            FROM
                progress p
            JOIN lecture l ON p.lecture_id = l.id
            WHERE l.member_id = :memberId
            GROUP BY
                rate_group
            ORDER BY
                rate_group;
            """, nativeQuery = true)
    List<DashBoardResponse.ProgressGroupResponse> findProgressGroupByMemberId(@Param("memberId") Long memberId);

    @Query(value = """
            SELECT
                CASE
                    WHEN progress_rate < 10 THEN '0~9%'
                    WHEN progress_rate < 20 THEN '10~19%'
                    WHEN progress_rate < 30 THEN '20~29%'
                    WHEN progress_rate < 40 THEN '30~39%'
                    WHEN progress_rate < 50 THEN '40~49%'
                    WHEN progress_rate < 60 THEN '50~59%'
                    WHEN progress_rate < 70 THEN '60~69%'
                    WHEN progress_rate < 80 THEN '70~79%'
                    WHEN progress_rate < 90 THEN '80~89%'
                    ELSE '90~100%'
                    END AS rate_group,
                COUNT(*) AS student_count
            FROM
                progress p
            JOIN lecture l ON p.lecture_id = l.id
            WHERE l.member_id = :memberId
            AND l.title = :lectureTitle
            GROUP BY
                rate_group
            ORDER BY
                rate_group;
            """, nativeQuery = true)
    List<DashBoardResponse.ProgressGroupResponse> findProgressGroupByMemberIdAndTitle(
            @Param("memberId") Long memberId,
            @Param("lectureTitle") String lectureTitle
    );
}
