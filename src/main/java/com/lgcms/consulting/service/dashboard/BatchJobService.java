package com.lgcms.consulting.service.dashboard;

import com.lgcms.consulting.common.dto.exception.BaseException;
import com.lgcms.consulting.common.dto.exception.ConsultingError;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BatchJobService {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public String launchJob(String jobName) {
        LocalDate now = LocalDate.now();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("date", now)
                .toJobParameters();

        try {
            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(jobName), jobParameters);
            if (jobExecution.getStatus() == BatchStatus.FAILED) {
                throw new BaseException(ConsultingError.BATCH_JOB_FAILED);
            }
        } catch (NoSuchJobException e) {
            throw new BaseException(ConsultingError.NO_SUCH_JOB);
        } catch (Exception e) {
            throw new BaseException(ConsultingError.BATCH_JOB_FAILED);
        }

        return jobName + " Success";
    }
}
