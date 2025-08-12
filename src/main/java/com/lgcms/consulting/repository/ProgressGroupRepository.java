package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.ProgressGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressGroupRepository extends JpaRepository<ProgressGroup, Long> {
    List<ProgressGroup> findByMemberIdAndTitle(Long memberId, String title);
}
