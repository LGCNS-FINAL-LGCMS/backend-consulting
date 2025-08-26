package com.lgcms.consulting.api;

import com.lgcms.consulting.dto.request.dashboard.BatchRequest.JobRequest;
import com.lgcms.consulting.service.dashboard.BatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/consulting/batch")
@RestController
@RequiredArgsConstructor
public class BatchController {
    private final BatchJobService batchJobService;

    @PostMapping("/")
    public ResponseEntity<String> updateDashboard(@RequestBody JobRequest jobRequest) {
        return ResponseEntity.ok(batchJobService.launchJob(jobRequest.jobName()));
    }
}
