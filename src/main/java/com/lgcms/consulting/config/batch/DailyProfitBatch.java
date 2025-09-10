package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.config.batch.utils.BatchConfig;
import com.lgcms.consulting.domain.DailyProfit;
import com.lgcms.consulting.repository.DailyProfitRepository;
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
import java.sql.Timestamp;
import java.time.LocalDate;


@Configuration
@RequiredArgsConstructor
public class DailyProfitBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final DailyProfitRepository dailyProfitRepository;
    private final BatchConfig batchConfig;

    @Bean
    public Job dailyProfitJob() {
        return new JobBuilder("dailyProfitJob", jobRepository)
                .start(dailyProfitStep())
                .build();
    }

    @Bean
    public Step dailyProfitStep() {
        return new StepBuilder("dailyProfitStep", jobRepository)
                .<DailyProfitDTO, DailyProfit>chunk(1000, transactionManager)
                .reader(dailyProfitItemReader(null))
                .processor(dailyProfitItemProcessor())
                .writer(dailyProfitItemWriter())
                .faultTolerant()
                .retryPolicy(batchConfig.retryPolicy())
                .backOffPolicy(batchConfig.backOffPolicy())
                .taskExecutor(batchConfig.taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<DailyProfitDTO> dailyProfitItemReader(
            @Value("#{jobParameters['date']}") LocalDate today
    ) {
        JdbcCursorItemReader<DailyProfitDTO> cursorItemReader =
                new JdbcCursorItemReaderBuilder<DailyProfitDTO>()
                        .name("dailyProfitItemReader")
                        .dataSource(dataSource)
                        .sql("""
                                SELECT
                                    l.member_id,
                                    DATE(e.enrollment_at) AS day,
                                    COALESCE(l.title, 'all') AS title,
                                    SUM(l.price) AS profit
                                FROM enrollment e
                                         JOIN lecture l ON e.lecture_id = l.id
                                GROUP BY DATE(e.enrollment_at), l.member_id, ROLLUP(l.title)
                                ORDER BY day, l.member_id, title;
                                """)
//                .queryArguments(List.of(today.toLocalDate()))
                        .fetchSize(1000)
                        .dataRowMapper(DailyProfitDTO.class)
                        .build();

        return new SynchronizedItemStreamReaderBuilder<DailyProfitDTO>()
                .delegate(cursorItemReader)
                .build();
    }

    @Bean
    public ItemProcessor<DailyProfitDTO, DailyProfit> dailyProfitItemProcessor() {
        return item -> DailyProfit.builder()
                .day(item.day.toLocalDateTime())
                .title(item.title)
                .memberId(item.memberId)
                .dailyProfit(item.profit)
                .build();
    }

    @Bean
    public RepositoryItemWriter<DailyProfit> dailyProfitItemWriter() {
        return new RepositoryItemWriterBuilder<DailyProfit>()
                .repository(dailyProfitRepository)
                .build();
    }

    public record DailyProfitDTO(
            Timestamp day,
            String title,
            Long profit,
            Long memberId
    ) {
    }

}
