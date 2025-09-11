package com.lgcms.consulting.service.dashboard;

import com.lgcms.consulting.common.dto.exception.BaseException;
import com.lgcms.consulting.common.dto.exception.ConsultingError;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchJobService {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public String launchJob(String jobName) {
        LocalDateTime now = LocalDateTime.now();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("datetime", now)
                .toJobParameters();

        try {
            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(jobName), jobParameters);
            if (jobExecution.getStatus() == BatchStatus.FAILED) {
                throw new BaseException(ConsultingError.BATCH_JOB_FAILED);
            }
        } catch (NoSuchJobException e) {
            throw new BaseException(ConsultingError.NO_SUCH_JOB);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new BaseException(ConsultingError.ALREADY_COMPLETE_JOB);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new BaseException(ConsultingError.ALREADY_RUNNING_JOB);
        } catch (Exception e) {
            throw new BaseException(ConsultingError.BATCH_JOB_FAILED);
        }


        return jobName + " Success";
    }
}
