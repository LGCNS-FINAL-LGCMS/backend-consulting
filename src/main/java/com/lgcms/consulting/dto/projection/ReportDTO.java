package com.lgcms.consulting.dto.projection;

public class ReportDTO {
    public record ReviewData(
            String content,
            String star,
            String details,
            String etc,
            String title
    ) {
    }
}
