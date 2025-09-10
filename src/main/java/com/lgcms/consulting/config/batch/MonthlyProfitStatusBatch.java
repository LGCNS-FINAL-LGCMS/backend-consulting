package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.config.batch.utils.BatchConfig;
import com.lgcms.consulting.domain.MonthlyProfitStatus;
import com.lgcms.consulting.repository.MonthlyProfitStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MonthlyProfitStatusBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final MonthlyProfitStatusRepository monthlyProfitStatusRepository;
    private final BatchConfig batchConfig;

    @Bean
    public Job monthlyProfitStatusJob() {
        return new JobBuilder("monthlyProfitStatusJob", jobRepository)
                .start(monthlyProfitStatusStep())
                .build();
    }

    @Bean
    public Step monthlyProfitStatusStep() {
        return new StepBuilder("monthlyProfitStatusStep", jobRepository)
                .<MonthlyProfitStatusDTO, MonthlyProfitStatus>chunk(1000, transactionManager)
                .reader(monthlyProfitStatusItemReader(null))
                .processor(monthlyProfitStatusItemProcessor())
                .writer(monthlyProfitStatusItemWriter())
                .faultTolerant()
                .retryPolicy(batchConfig.retryPolicy())
                .backOffPolicy(batchConfig.backOffPolicy())
                .taskExecutor(batchConfig.taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<MonthlyProfitStatusDTO> monthlyProfitStatusItemReader(
            @Value("#{jobParameters['date']}") LocalDate today
    ) {
        JdbcCursorItemReader<MonthlyProfitStatusDTO> cursorItemReader =
                new JdbcCursorItemReaderBuilder<MonthlyProfitStatusDTO>()
                        .name("monthlyProfitStatusItemReader")
                        .dataSource(dataSource)
                        .sql("""
                                SELECT l.title, SUM(l.price) as profit, l.member_id
                                FROM enrollment e JOIN lecture l ON e.lecture_id = l.id
                                WHERE e.enrollment_at BETWEEN ? AND ?
                                GROUP BY l.title, l.member_id
                                ORDER BY l.title
                                """)
                        .queryArguments(List.of(today.withDayOfMonth(1), today))
                        .fetchSize(1000)
                        .dataRowMapper(MonthlyProfitStatusDTO.class)
                        .build();

        return new SynchronizedItemStreamReaderBuilder<MonthlyProfitStatusDTO>()
                .delegate(cursorItemReader)
                .build();
    }

    @Bean
    public ItemProcessor<MonthlyProfitStatusDTO, MonthlyProfitStatus> monthlyProfitStatusItemProcessor() {
        return item -> MonthlyProfitStatus.builder()
                .title(item.title())
                .monthlyProfit(item.profit())
                .memberId(item.memberId())
                .build();
    }

    @Bean
    public RepositoryItemWriter<MonthlyProfitStatus> monthlyProfitStatusItemWriter() {
        return new RepositoryItemWriterBuilder<MonthlyProfitStatus>()
                .repository(monthlyProfitStatusRepository)
                .build();
    }

    public record MonthlyProfitStatusDTO(
            String title,
            Long profit,
            Long memberId
    ) {
    }
}
