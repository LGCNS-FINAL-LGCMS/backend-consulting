package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.DailyProfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyProfitRepository extends JpaRepository<DailyProfit, Long> {
    List<DailyProfit> findTop5ByMemberIdAndDayBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<DailyProfit> findByTitleAndMemberIdAndDayBetween(String title, Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
