package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.MonthlyProfitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyProfitStatusRepository extends JpaRepository<MonthlyProfitStatus, Long> {
    List<MonthlyProfitStatus> findByMemberId(Long memberId);
}
