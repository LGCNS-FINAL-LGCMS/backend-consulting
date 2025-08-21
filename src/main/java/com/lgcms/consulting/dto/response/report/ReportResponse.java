package com.lgcms.consulting.dto.response.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {
    private String reviewAnalysisResult;

    private String qnaAnalysisResult;

    private String overallAnalysisResult;
}
