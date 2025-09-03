package com.lgcms.consulting.config.batch.utils;

import com.lgcms.consulting.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class CustomWriter {
    private final DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<Lecture> lectureWriter() {
        String sql = """
                INSERT INTO lecture (id, member_id, title, level, price, review_count, total_amount)
                VALUES (:id, :memberId, :title, :level, :price, :reviewCount, :totalAmount)
                ON CONFLICT (id)
                DO UPDATE SET
                    member_id = EXCLUDED.member_id,
                    title = EXCLUDED.title,
                    level = EXCLUDED.level,
                    price = EXCLUDED.price,
                    review_count = EXCLUDED.review_count,
                    total_amount = EXCLUDED.total_amount
                """;

        return new JdbcBatchItemWriterBuilder<Lecture>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Enrollment> enrollmentWriter() {
        String sql = """
                INSERT INTO enrollment (id, student_id, lecture_id, enrollment_at)
                VALUES (:id, :studentId, :lectureId, :enrollmentAt)
                ON CONFLICT (id)
                DO UPDATE SET
                    student_id = EXCLUDED.student_id,
                    lecture_id = EXCLUDED.lecture_id,
                    enrollment_at = EXCLUDED.enrollment_at
                """;

        return new JdbcBatchItemWriterBuilder<Enrollment>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Question> questionWriter() {
        String sql = """
                INSERT INTO question (id, member_id, lecture_id, title, created_at)
                VALUES (:id, :memberId, :lectureId, :title, :createdAt)
                ON CONFLICT (id)
                DO UPDATE SET
                    member_id = EXCLUDED.member_id,
                    lecture_id = EXCLUDED.lecture_id,
                    title = EXCLUDED.title,
                    created_at = EXCLUDED.created_at
                """;

        return new JdbcBatchItemWriterBuilder<Question>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Review> reviewWriter() {
        String sql = """
                INSERT INTO review (id, member_id, lecture_id, suggestion, star, nickname, difficulty, usefulness, created_at)
                VALUES (:id, :memberId, :lectureId, :suggestion, :star, :nickname, :difficulty, :usefulness, :createdAt)
                ON CONFLICT (id)
                DO UPDATE SET
                    member_id = EXCLUDED.member_id,
                    lecture_id = EXCLUDED.lecture_id,
                    suggestion = EXCLUDED.suggestion,
                    star = EXCLUDED.star,
                    nickname = EXCLUDED.nickname,
                    difficulty = EXCLUDED.difficulty,
                    usefulness = EXCLUDED.usefulness,
                    created_at = EXCLUDED.created_at
                """;

        return new JdbcBatchItemWriterBuilder<Review>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Progress> progressWriter() {
        String sql = """
                INSERT INTO progress (id, member_id, lecture_id, progress_rate)
                VALUES (:id, :memberId, :lectureId, :progressRate)
                ON CONFLICT (id)
                DO UPDATE SET
                    member_id = EXCLUDED.member_id,
                    lecture_id = EXCLUDED.lecture_id,
                    progress_rate = EXCLUDED.progress_rate
                """;

        return new JdbcBatchItemWriterBuilder<Progress>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
