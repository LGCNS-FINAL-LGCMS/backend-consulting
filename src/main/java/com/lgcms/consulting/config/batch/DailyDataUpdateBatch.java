package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.domain.Enrollment;
import com.lgcms.consulting.domain.Lecture;
import com.lgcms.consulting.domain.Question;
import com.lgcms.consulting.domain.Review;
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
    private final CustomProcessor customProcessor;
    private final CustomWriter customWriter;

    @Bean
    public Job dailyDataUpdateJob() {
        return new JobBuilder("dailyDataUpdateJob", jobRepository)
                .start(lectureUpdateStep())
                .next(questionUpdateStep())
                .next(enrollmentUpdateStep())
                .next(reviewUpdateStep())
                .build();
    }

    @Bean
    public Step lectureUpdateStep() {
        return new StepBuilder("lectureUpdateStep", jobRepository)
                .<LectureMetaResponse, Lecture>chunk(100, transactionManager)
                .reader(customReader.lectureReader())
                .processor(customProcessor.lectureProcessor())
                .writer(customWriter.lectureWriter())
                .build();
    }

    @Bean
    public Step questionUpdateStep() {
        return new StepBuilder("questionUpdateStep", jobRepository)
                .<LectureQuestionsResponse, Question>chunk(100, transactionManager)
                .reader(customReader.questionReader())
                .processor(customProcessor.questionProcessor())
                .writer(customWriter.questionWriter())
                .build();
    }

    @Bean
    public Step enrollmentUpdateStep() {
        return new StepBuilder("enrollmentUpdateStep", jobRepository)
                .<LectureEnrollmentsResponse, Enrollment>chunk(100, transactionManager)
                .reader(customReader.enrollmentReader())
                .processor(customProcessor.enrollmentProcessor())
                .writer(customWriter.enrollmentWriter())
                .build();
    }


    @Bean
    public Step reviewUpdateStep() {
        return new StepBuilder("reviewUpdateStep", jobRepository)
                .<LectureReviewsResponse, Review>chunk(100, transactionManager)
                .reader(customReader.reviewReader())
                .processor(customProcessor.reviewProcessor())
                .writer(customWriter.reviewWriter())
                .build();
    }
}
