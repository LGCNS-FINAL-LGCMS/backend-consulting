package com.lgcms.consulting.dto.projection;

public class ReportDTO {
    public record ReviewData(
            String suggestion,
            Long star,
            Integer difficulty,
            Integer usefulness,
            String title
    ) {
    }
}