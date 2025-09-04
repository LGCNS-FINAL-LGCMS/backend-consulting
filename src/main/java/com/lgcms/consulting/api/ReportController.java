package com.lgcms.consulting.api;

import com.lgcms.consulting.common.dto.BaseResponse;
import com.lgcms.consulting.dto.response.report.ReportResponse;
import com.lgcms.consulting.service.ai.BedrockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lecturer/consulting/report")
@RestController
@RequiredArgsConstructor
public class ReportController {
    private final BedrockService bedrockService;

    @PostMapping
    public ResponseEntity<BaseResponse<ReportResponse>> getReport(@RequestHeader("X-USER-ID") Long id) {
        return ResponseEntity.ok(BaseResponse.ok(bedrockService.getReport(id)));
    }
}
