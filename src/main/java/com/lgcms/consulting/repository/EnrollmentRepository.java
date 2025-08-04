package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Enrollment;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.ProfitOverviewTransfer;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.LectureCountPerStudentResponse;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.MonthlyStatusItem;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.ProfitTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query(value = """
                        SELECT e.lecture_id,
                            COUNT(*) * l.price AS value
                        FROM enrollment e
                        JOIN lecture l ON e.lecture_id = l.id
                        WHERE e.enrollment_at >= :startDate
                                    AND e.enrollment_at <= :endDate    
                                    AND l.member_id = :memberId
                        GROUP BY e.lecture_id, l.price
                        ORDER BY lecture_id
            """, nativeQuery = true)
    List<MonthlyStatusItem> findMonthlyStatusByMemberId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("memberId") Long memberId
    );

    @Query(value = """
                        SELECT DATE(e.enrollment_at) AS day,
                            l.title,
                            COUNT(*) * l.price AS profit
                        FROM enrollment e
                        JOIN lecture l ON e.lecture_id = l.id
                        WHERE e.enrollment_at >= :startDate
                                    AND e.enrollment_at <= :endDate    
                                    AND l.member_id = :memberId
                        GROUP BY DATE(e.enrollment_at), l.title , l.price
                        ORDER BY day, lecture_id
            """, nativeQuery = true)
    List<ProfitTransfer> findProfitByDateAndLectureId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("memberId") Long memberId
    );

    @Query(value = """
            SELECT t.count AS lecture_count, COUNT(*) as student_count
            FROM (
                     SELECT e.student_id, COUNT(*) as count
                     FROM enrollment e
                     JOIN lecture l ON e.lecture_id = l.id
                     WHERE l.member_id = :memberId
                     GROUP BY e.student_id
                 ) t
            GROUP BY lecture_count
            ORDER BY lecture_count
            """, nativeQuery = true)
    List<LectureCountPerStudentResponse> countEnrollmentByMemberId(
            @Param("memberId") Long memberId
    );

    @Query(value = """
            SELECT t.day , SUM( profit ) AS profits
            FROM (
                     SELECT DATE(e.enrollment_at) AS day, COUNT(*) * l.price as profit
                     FROM enrollment e
                              JOIN lecture l ON e.lecture_id = l.id
                     WHERE l.member_id = :memberId
                                 AND e.enrollment_at >= :startDate
                                 AND e.enrollment_at <= :endDate
                     GROUP BY day, l.price
                 ) t
            GROUP BY day
            ORDER BY day
            """, nativeQuery = true)
    List<ProfitOverviewTransfer> findProfitOverviewByMemberId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("memberId") Long memberId
    );
}
