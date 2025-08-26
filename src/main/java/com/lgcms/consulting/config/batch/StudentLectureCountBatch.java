package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.config.batch.utils.BatchRetryPolicy;
import com.lgcms.consulting.domain.StudentLectureCount;
import com.lgcms.consulting.repository.StudentLectureCountRepository;
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
public class StudentLectureCountBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final StudentLectureCountRepository studentLectureCountRepository;
    private final BatchRetryPolicy batchRetryPolicy;

    @Bean
    public Job studentLectureCountJob() {
        return new JobBuilder("studentLectureCountJob", jobRepository)
                .start(studentLectureCountStep())
                .build();
    }

    @Bean
    public Step studentLectureCountStep() {
        return new StepBuilder("studentLectureCountStep", jobRepository)
                .<StudentLectureCountDTO, StudentLectureCount>chunk(1000, transactionManager)
                .reader(studentLectureCountItemReader())
                .processor(studentLectureCountItemProcessor())
                .writer(studentLectureCountItemWriter())
                .faultTolerant()
                .retryPolicy(batchRetryPolicy.retryPolicy())
                .backOffPolicy(batchRetryPolicy.backOffPolicy())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<StudentLectureCountDTO> studentLectureCountItemReader() {
        return new JdbcCursorItemReaderBuilder<StudentLectureCountDTO>()
                .name("studentLectureCountItemReader")
                .dataSource(dataSource)
                .sql("""
                        WITH student_lecture_counts AS (SELECT l.member_id,
                                                               e.student_id,
                                                               COUNT(*) AS lecture_count
                                                        FROM enrollment e
                                                                 JOIN lecture l ON e.lecture_id = l.id
                                                        GROUP BY e.student_id, l.member_id),
                             buckets AS (SELECT *
                                         FROM (VALUES ('1', 1, 1),
                                                      ('2', 2, 2),
                                                      ('3', 3, 3),
                                                      ('4', 4, 4),
                                                      ('5', 5, NULL)) AS v(label, min_cnt, max_cnt))
                        SELECT b.label               AS lecture_count_group,
                               slc.member_id,
                               COUNT(slc.student_id) AS student_count
                        FROM buckets b
                                 JOIN student_lecture_counts slc
                                      ON (
                                          (b.max_cnt IS NOT NULL AND slc.lecture_count BETWEEN b.min_cnt AND b.max_cnt)
                                              OR (b.max_cnt IS NULL AND slc.lecture_count >= b.min_cnt)
                                          )
                        GROUP BY b.label, slc.member_id
                        """)
                .fetchSize(1000)
                .dataRowMapper(StudentLectureCountDTO.class)
                .build();
    }

    @Bean
    public ItemProcessor<StudentLectureCountDTO, StudentLectureCount> studentLectureCountItemProcessor() {
        return item -> StudentLectureCount.builder()
                .memberId(item.memberId)
                .lectureCountGroup(item.lectureCountGroup)
                .studentCount(item.studentCount)
                .build();
    }

    @Bean
    public RepositoryItemWriter<StudentLectureCount> studentLectureCountItemWriter() {
        return new RepositoryItemWriterBuilder<StudentLectureCount>()
                .repository(studentLectureCountRepository)
                .build();
    }

    public record StudentLectureCountDTO(
            String lectureCountGroup,
            Long memberId,
            Long studentCount
    ) {
    }
}
