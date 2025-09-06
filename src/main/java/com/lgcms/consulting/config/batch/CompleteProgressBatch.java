package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.config.batch.utils.BatchConfig;
import com.lgcms.consulting.domain.CompleteProgress;
import com.lgcms.consulting.repository.CompleteProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class CompleteProgressBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final CompleteProgressRepository completeProgressRepository;
    private final BatchConfig batchConfig;

    @Bean
    public Job completeProgressJob() {
        return new JobBuilder("completeProgressJob", jobRepository)
                .start(completeProgressStep())
                .build();
    }

    @Bean
    public Step completeProgressStep() {
        return new StepBuilder("completeProgressStep", jobRepository)
                .<CompleteProgressDTO, CompleteProgress>chunk(1000, transactionManager)
                .reader(completeProgressItemReader())
                .processor(completeProgressItemProcessor())
                .writer(completeProgressItemWriter())
                .faultTolerant()
                .retryPolicy(batchConfig.retryPolicy())
                .backOffPolicy(batchConfig.backOffPolicy())
                .taskExecutor(batchConfig.taskExecutor())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<CompleteProgressDTO> completeProgressItemReader() {
        return new JdbcCursorItemReaderBuilder<CompleteProgressDTO>()
                .name("completeProgressItemReader")
                .dataSource(dataSource)
                .sql("""
                        SELECT l.title, l.member_id,
                               ROUND(COUNT(CASE WHEN p.progress_rate > 90 THEN 1 END) * 100.0 / COUNT(*))::bigint AS complete_progress
                        FROM progress p
                                 JOIN Lecture l ON p.lecture_id = l.id
                        GROUP BY l.title, l.member_id;
                        """)
                .fetchSize(1000)
                .dataRowMapper(CompleteProgressDTO.class)
                .build();
    }

    @Bean
    public ItemProcessor<CompleteProgressDTO, CompleteProgress> completeProgressItemProcessor() {
        return item -> CompleteProgress.builder()
                .title(item.title)
                .memberId(item.memberId)
                .completeProgress(item.completeProgress)
                .build();
    }

    @Bean
    public RepositoryItemWriter<CompleteProgress> completeProgressItemWriter() {
        return new RepositoryItemWriterBuilder<CompleteProgress>()
                .repository(completeProgressRepository)
                .build();
    }

    public record CompleteProgressDTO(
            String title,
            Long memberId,
            Long completeProgress
    ) {
    }
}
