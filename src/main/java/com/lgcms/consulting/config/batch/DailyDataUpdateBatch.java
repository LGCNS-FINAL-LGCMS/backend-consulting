package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.config.batch.utils.BatchRetryPolicy;
import com.lgcms.consulting.config.batch.utils.CustomReader;
import com.lgcms.consulting.config.batch.utils.CustomWriter;
import com.lgcms.consulting.domain.*;
import com.lgcms.consulting.dto.response.lecture.RemoteLectureResponse.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyDataUpdateBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CustomReader customReader;
    private final CustomWriter customWriter;
    private final BatchRetryPolicy batchRetryPolicy;

    @Bean
    public Job dailyDataUpdateJob() {
        return new JobBuilder("dailyDataUpdateJob", jobRepository)
                .start(lectureUpdateStep())
                .next(questionUpdateStep())
                .next(enrollmentUpdateStep())
                .next(reviewUpdateStep())
                .next(progressUpdateStep())
                .build();
    }

    @Bean
    public Step lectureUpdateStep() {
        return new StepBuilder("lectureUpdateStep", jobRepository)
                .<LectureMetaResponse, Lecture>chunk(1000, transactionManager)
                .reader(customReader.lectureReader())
                .processor(LectureMetaResponse::toEntity)
                .writer(customWriter.lectureWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }

    @Bean
    public Step questionUpdateStep() {
        return new StepBuilder("questionUpdateStep", jobRepository)
                .<LectureQuestionsResponse, Question>chunk(1000, transactionManager)
                .reader(customReader.questionReader())
                .processor(LectureQuestionsResponse::toEntity)
                .writer(customWriter.questionWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }

    @Bean
    public Step enrollmentUpdateStep() {
        return new StepBuilder("enrollmentUpdateStep", jobRepository)
                .<LectureEnrollmentsResponse, Enrollment>chunk(10000, transactionManager)
                .reader(customReader.enrollmentReader())
                .processor(LectureEnrollmentsResponse::toEntity)
                .writer(customWriter.enrollmentWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }

    @Bean
    public Step reviewUpdateStep() {
        return new StepBuilder("reviewUpdateStep", jobRepository)
                .<LectureReviewsResponse, Review>chunk(1000, transactionManager)
                .reader(customReader.reviewReader())
                .processor(LectureReviewsResponse::toEntity)
                .writer(customWriter.reviewWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }

    @Bean
    public Step progressUpdateStep() {
        return new StepBuilder("progressUpdateStep", jobRepository)
                .<LectureProgressResponse, Progress>chunk(1000, transactionManager)
                .reader(customReader.progressReader())
                .processor(LectureProgressResponse::toEntity)
                .writer(customWriter.progressWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }
}
