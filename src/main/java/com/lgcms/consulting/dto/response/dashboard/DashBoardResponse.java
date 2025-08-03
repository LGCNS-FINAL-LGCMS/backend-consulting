package com.lgcms.consulting.dto.response.dashboard;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DashBoardResponse {
    public record MonthlyStatusResponse(
            Long total,
            List<MonthlyStatusItem> monthlyStatus
    ) {
    }

    public record MonthlyStatusItem(
            Long id,
            Long value
    ) {
    }

    public record ProfitDistributionResponse(
            List<String> titles,
            List<Map<String, Object>> profits
    ) {
    }

    public record LectureProfitItem(
            String title,
            Long profit
    ) {
    }

    public record ProfitTransfer(
            Date day,
            String title,
            Long profit
    ) {
    }

    public record ProfitOverviewResponse(
            String id,
            List<ProfitOverviewTransfer> data
    ) {
    }

    public record ProfitOverviewTransfer(
            Date x,
            BigDecimal y
    ) {
    }
}
