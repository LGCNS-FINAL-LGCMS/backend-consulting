package com.lgcms.consulting.service.dashboard;

import com.lgcms.consulting.common.dto.exception.BaseException;
import com.lgcms.consulting.common.dto.exception.ConsultingError;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.*;
import com.lgcms.consulting.repository.EnrollmentRepository;
import com.lgcms.consulting.repository.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class DashBoardService {
    private final EnrollmentRepository enrollmentRepository;
    private final ProgressRepository progressRepository;

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

    @Transactional
    public ProfitDistributionResponse getProfitDistribution(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusWeeks(1);
        List<ProfitTransfer> dailyProfits = enrollmentRepository.findProfitByDateAndLectureId(startDate, now, memberId);
        if (dailyProfits.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }

        MultiValueMap<String, LectureProfitItem> profits = new LinkedMultiValueMap<>();
        for (ProfitTransfer item : dailyProfits) {
            profits.add(item.day().toString(), new LectureProfitItem(item.title(), item.profit()));
        }

        return new ProfitDistributionResponse(profits.keySet().stream().toList(), convert(profits));
    }

    @Transactional
    public ProfitOverviewResponse getProfitOverview(String startDate, String endDate, Long memberId) {
        List<ProfitOverviewTransfer> responses = enrollmentRepository.findProfitOverviewByMemberId(LocalDate.parse(startDate), LocalDate.parse(endDate), memberId);
        if (responses.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }

        return new ProfitOverviewResponse("매출", responses);
    }

    @Transactional
    public List<CompleteProgressResponse> getCompleteProgress(Long memberId) {
        List<CompleteProgressTransfer> responses = progressRepository.findCompleteProgressByMemberId(memberId);
        if (responses.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }
        return responses.stream()
                .map(n -> CompleteProgressTransfer.toDTO(n.title(), n.completeProgress())).collect(Collectors.toList());
    }

    List<Map<String, Object>> convert(MultiValueMap<String, LectureProfitItem> profits) {
        List<Map<String, Object>> results = new ArrayList<>();

        profits.forEach((k, v) -> {
            Map<String, Object> resultItem = new HashMap<>();
            Long dailyTotal = v.stream().mapToLong(LectureProfitItem::profit).sum();
            for (LectureProfitItem item : v) {
                resultItem.put(item.title(), Math.round((double) item.profit() / dailyTotal * 100));
            }
            resultItem.put("day", k);
            results.add(resultItem);
        });

        return results;
    }
}
