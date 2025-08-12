package com.lgcms.consulting.service.dashboard;

import com.lgcms.consulting.common.dto.exception.BaseException;
import com.lgcms.consulting.common.dto.exception.ConsultingError;
import com.lgcms.consulting.domain.*;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.*;
import com.lgcms.consulting.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashBoardService {
    private final MonthlyProfitStatusRepository monthlyProfitStatusRepository;
    private final StudentLectureCountRepository studentLectureCountRepository;
    private final DailyProfitRepository dailyProfitRepository;
    private final CompleteProgressRepository completeProgressRepository;
    private final ProgressGroupRepository progressGroupRepository;

    @Transactional(readOnly = true)
    public MonthlyStatusResponse getMonthlyStatus(Long memberId) {
        List<MonthlyProfitStatus> monthlyStatusItems = monthlyProfitStatusRepository.findByMemberId(memberId);
        if (monthlyStatusItems.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }

        Long total = monthlyStatusItems.stream().mapToLong(MonthlyProfitStatus::getMonthlyProfit).sum();

        return new MonthlyStatusResponse(total, monthlyStatusItems.stream().map(
                item-> new PieChartData(item.getTitle(), item.getMonthlyProfit()))
                .toList());
    }

    //TODO: 마지막 변환 로직 검토 필요
    @Transactional(readOnly = true)
    public ProfitDistributionResponse getProfitDistribution(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusWeeks(1);
        List<DailyProfit> dailyProfits = dailyProfitRepository.findTop5ByMemberIdAndDayBetween(memberId, startDate, now);
        if (dailyProfits.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }
        List<String> titles = dailyProfits.stream().map(DailyProfit::getTitle).distinct().toList();

        MultiValueMap<String, LectureProfitItem> profits = new LinkedMultiValueMap<>();
        for (DailyProfit item : dailyProfits) {
            profits.add(item.getDay().toLocalDate().toString(), new LectureProfitItem(item.getTitle(), item.getDailyProfit()));
        }

        return new ProfitDistributionResponse("day", titles, convert(profits));
    }

    @Transactional(readOnly = true)
    public ProfitOverviewResponse getProfitOverview(String title, String startDate, String endDate, Long memberId) {
        List<DailyProfit> dailyProfits = dailyProfitRepository.findByTitleAndMemberIdAndDayBetween(title, memberId, LocalDate.parse(startDate).atStartOfDay(), LocalDate.parse(endDate).atStartOfDay());
        if (dailyProfits.isEmpty()) {
            log.info(title + " - " + memberId + " - " + LocalDate.parse(startDate).atStartOfDay() + " - " + LocalDate.parse(endDate).atStartOfDay());
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }

        return new ProfitOverviewResponse("매출", dailyProfits.stream().map(item -> new LineChartData(item.getDay().toLocalDate().toString(), item.getDailyProfit().toString())).toList());
    }

    @Transactional(readOnly = true)
    public CompleteProgressResponse getCompleteProgress(Long memberId) {
        List<CompleteProgress> responses = completeProgressRepository.findTop5CompleteProgressByMemberIdOrderByCompleteProgressDesc(memberId);
        if (responses.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }

        return new CompleteProgressResponse("title", List.of("completeProgress"),
                responses.stream().map(
                        item ->
                                new CompleteProgressData(item.getTitle(), item.getCompleteProgress().toString())
                        )
                        .toList()
                );
    }

    @Transactional(readOnly = true)
    public ProgressGroupResponse getProgressGroup(Long memberId, String title) {
        List<ProgressGroup> responses = progressGroupRepository.findByMemberIdAndTitle(memberId, title);
        if (responses.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }
        return new ProgressGroupResponse("rateGroup", List.of("studentCount"),
                responses.stream().map(
                        item ->
                                new ProgressGroupData(item.getRateGroup(), item.getStudentCount().toString())
                ).toList()
        );
    }

    @Transactional(readOnly = true)
    public StudentLectureCountResponse getStudentLectureCount(Long memberId) {
        List<StudentLectureCount> studentLectureCount = studentLectureCountRepository.findByMemberId(memberId);
        if (studentLectureCount.isEmpty()) {
            throw new BaseException(ConsultingError.DASHBOARD_DATA_NOT_FOUND);
        }
        List<PieChartData> response = studentLectureCount.stream()
                .map(item ->
                        new PieChartData(item.getLectureCountGroup(), item.getStudentCount())
                )
                .toList();
        return new StudentLectureCountResponse(response);
    }

    List<Map<String, String>> convert(MultiValueMap<String, LectureProfitItem> profits) {
        List<Map<String, String>> results = new ArrayList<>();

        profits.forEach((k, v) -> {
            Map<String, String> resultItem = new HashMap<>();
            Long dailyTotal = v.stream().mapToLong(LectureProfitItem::profit).sum();
            for (LectureProfitItem item : v) {
                resultItem.put(item.title(), String.valueOf(Math.round((double) item.profit() / dailyTotal * 100)));
            }
            resultItem.put("day", k);
            results.add(resultItem);
        });

        return results;
    }
}
