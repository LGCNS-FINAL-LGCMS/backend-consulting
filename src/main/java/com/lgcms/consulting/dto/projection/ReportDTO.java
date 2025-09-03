package com.lgcms.consulting.dto.projection;

public class ReportDTO {
    public record ReviewData(
            String suggestion,
            Integer star,
            Integer difficulty,
            Integer usefulness,
            String title
    ) {
    }
    public record QuestionData(
            String title,
            String content
    ){}
}