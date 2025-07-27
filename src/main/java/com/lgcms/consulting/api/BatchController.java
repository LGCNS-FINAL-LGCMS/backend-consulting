package com.lgcms.consulting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    //TODO: 대시보드 시각화 추가시 업데이트 로직 필요
    @PostMapping("/batch")
    public ResponseEntity<String> updateDashboard() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("datetime", now)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("dailyDataUpdateJob"), jobParameters);

        return ResponseEntity.ok("Batch test");
    }
}
