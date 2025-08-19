package com.lgcms.consulting.service.ai;

import com.lgcms.consulting.dto.response.report.ReportResponse;

public interface AiService {
    ReportResponse getReport(Long memberId);
}
