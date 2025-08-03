package com.lgcms.consulting.service.dashboard;

import com.lgcms.consulting.common.dto.exception.BaseException;
import com.lgcms.consulting.common.dto.exception.ConsultingError;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.*;
import com.lgcms.consulting.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class DashBoardService {
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public MonthlyStatusResponse getMonthlyStatus(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<MonthlyStatusItem> monthlyStatusItems = enrollmentRepository.findMonthlyStatusByMemberId(startDate, endDate, memberId);
        if (monthlyStatusItems.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }

        Long total = monthlyStatusItems.stream().mapToLong(MonthlyStatusItem::value).sum();
        return new MonthlyStatusResponse(total, monthlyStatusItems);
    }
}
