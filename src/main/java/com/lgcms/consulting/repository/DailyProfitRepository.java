package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.DailyProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyProfitRepository extends JpaRepository<DailyProfit, Long> {
    //TODO: 나중에 강의 개수 많아지면..로직을 좀 바꿔야할듯
    @Query(value = """
            SELECT *
            FROM daily_profit AS dp
            WHERE dp.member_id = :memberId
            AND dp.title != 'all'
            AND dp.day BETWEEN :startDate AND :endDate
            """,
    nativeQuery = true)
    List<DailyProfit> findByMemberIdAndDayBetween(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<DailyProfit> findByTitleAndMemberIdAndDayBetween(String title, Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
