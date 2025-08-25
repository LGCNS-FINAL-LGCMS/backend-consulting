package com.lgcms.consulting.config.batch.utils;

import com.lgcms.consulting.domain.*;
import com.lgcms.consulting.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomWriter {
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final QuestionRepository questionRepository;
    private final ReviewRepository reviewRepository;
    private final ProgressRepository progressRepository;

    public RepositoryItemWriter<Lecture> lectureWriter() {
        return new RepositoryItemWriterBuilder<Lecture>()
                .repository(lectureRepository)
                .build();
    }

    public RepositoryItemWriter<Enrollment> enrollmentWriter() {
        return new RepositoryItemWriterBuilder<Enrollment>()
                .repository(enrollmentRepository)
                .build();
    }

    public RepositoryItemWriter<Question> questionWriter() {
        return new RepositoryItemWriterBuilder<Question>()
                .repository(questionRepository)
                .build();
    }

    public RepositoryItemWriter<Review> reviewWriter() {
        return new RepositoryItemWriterBuilder<Review>()
                .repository(reviewRepository)
                .build();
    }

    public RepositoryItemWriter<Progress> progressWriter() {
        return new RepositoryItemWriterBuilder<Progress>()
                .repository(progressRepository)
                .build();
    }
}
