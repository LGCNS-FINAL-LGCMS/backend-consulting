package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.CompleteProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompleteProgressRepository extends JpaRepository<CompleteProgress, Long> {
    List<CompleteProgress> findTop5CompleteProgressByMemberIdOrderByCompleteProgressDesc(Long memberId);
}
