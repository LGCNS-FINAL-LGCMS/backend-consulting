package com.lgcms.consulting.dto.response.dashboard;

import java.util.List;
import java.util.Map;

public class DashBoardResponse {
    public record DashBoardDataResponse(
            MonthlyStatusResponse monthlyStatusResponse,
            ProfitDistributionResponse profitDistributionResponse,
            ProfitOverviewResponse profitOverviewResponse,
            CompleteProgressResponse completeProgressResponse,
            ProgressGroupResponse progressGroupResponse,
            StudentLectureCountResponse studentLectureCountResponse
    ) {
    }

    public record MonthlyStatusResponse(
            Long total,
            List<PieChartData> monthlyStatus
    ) {
    }

    public record ProfitDistributionResponse(
            String index,
            List<String> keyList,
            List<Map<String, String>> dataList
    ) {
    }

    public record LectureProfitItem(
            String title,
            Long profit
    ) {
    }

    public record ProfitOverviewResponse(
            String id,
            List<LineChartData> data
    ) {
    }

    public record CompleteProgressResponse(
            String index,
            List<String> keyList,
            List<CompleteProgressData> dataList
    ) {
    }

    public record CompleteProgressData(
            String title,
            String completeProgress
    ) {
    }

    public record ProgressGroupResponse(
            String index,
            List<String> keyList,
            List<ProgressGroupData> dataList
    ) {
    }

    public record ProgressGroupData(
            String rateGroup,
            String studentCount
    ) {
    }

    public record StudentLectureCountResponse(
            List<PieChartData> data
    ) {
    }

    public record PieChartData(
            String id,
            Long value
    ) {
    }

    public record LineChartData(
            String x,
            String y
    ) {
    }

}
