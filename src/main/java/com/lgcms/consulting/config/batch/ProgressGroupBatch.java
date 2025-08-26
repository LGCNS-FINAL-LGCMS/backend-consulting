package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.config.batch.utils.BatchRetryPolicy;
import com.lgcms.consulting.domain.ProgressGroup;
import com.lgcms.consulting.repository.ProgressGroupRepository;
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
public class ProgressGroupBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final ProgressGroupRepository progressGroupRepository;
    private final BatchRetryPolicy batchRetryPolicy;

    @Bean
    public Job progressGroupJob() {
        return new JobBuilder("progressGroupJob", jobRepository)
                .start(progressGroupStep())
                .build();
    }

    @Bean
    public Step progressGroupStep() {
        return new StepBuilder("progressGroupStep", jobRepository)
                .<ProgressGroupDTO, ProgressGroup>chunk(1000, transactionManager)
                .reader(progressGroupItemReader())
                .processor(progressGroupItemProcessor())
                .writer(progressGroupItemWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<ProgressGroupDTO> progressGroupItemReader() {
        return new JdbcCursorItemReaderBuilder<ProgressGroupDTO>()
                .name("progressGroupItemReader")
                .dataSource(dataSource)
                .sql("""
                        WITH base AS (
                            SELECT
                                CASE
                                    WHEN p.progress_rate < 10 THEN '0~9%'
                                    WHEN p.progress_rate < 20 THEN '10~19%'
                                    WHEN p.progress_rate < 30 THEN '20~29%'
                                    WHEN p.progress_rate < 40 THEN '30~39%'
                                    WHEN p.progress_rate < 50 THEN '40~49%'
                                    WHEN p.progress_rate < 60 THEN '50~59%'
                                    WHEN p.progress_rate < 70 THEN '60~69%'
                                    WHEN p.progress_rate < 80 THEN '70~79%'
                                    WHEN p.progress_rate < 90 THEN '80~89%'
                                    ELSE '90~100%'
                                    END AS rate_group,
                                l.title,
                                l.member_id
                            FROM progress p
                                     JOIN lecture l ON p.lecture_id = l.id
                        )
                        SELECT
                            rate_group,
                            CASE WHEN GROUPING(title) = 1 THEN 'all' ELSE title END AS title,
                            member_id,
                            COUNT(*) AS student_count
                        FROM base
                        GROUP BY rate_group, member_id, ROLLUP (title)
                        ORDER BY rate_group, title;
                        """)
                .fetchSize(1000)
                .dataRowMapper(ProgressGroupDTO.class)
                .build();
    }

    @Bean
    public ItemProcessor<ProgressGroupDTO, ProgressGroup> progressGroupItemProcessor() {
        return item -> ProgressGroup.builder()
                .rateGroup(item.rateGroup)
                .title(item.title)
                .memberId(item.memberId)
                .studentCount(item.studentCount)
                .build();
    }

    @Bean
    public RepositoryItemWriter<ProgressGroup> progressGroupItemWriter() {
        return new RepositoryItemWriterBuilder<ProgressGroup>()
                .repository(progressGroupRepository)
                .build();
    }

    public record ProgressGroupDTO(
            String rateGroup,
            String title,
            Long memberId,
            Long studentCount
    ) {
    }
}
