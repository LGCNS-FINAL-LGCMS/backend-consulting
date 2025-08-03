package com.lgcms.consulting.dto.response.dashboard;

import java.util.List;

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
}
