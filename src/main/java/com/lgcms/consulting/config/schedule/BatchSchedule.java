package com.lgcms.consulting.config.schedule;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
public class BatchSchedule{

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;


    public BatchSchedule(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Scheduled(cron = "0 0 0 1/1 * ?", zone = "Asia/Seoul")
    public void dailySchedule() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("datetime", now)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("dailyDataUpdateJob"), jobParameters);
    }
}
