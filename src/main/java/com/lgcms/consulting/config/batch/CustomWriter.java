package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.domain.Enrollment;
import com.lgcms.consulting.domain.Lecture;
import com.lgcms.consulting.domain.Question;
import com.lgcms.consulting.domain.Review;
import com.lgcms.consulting.repository.EnrollmentRepository;
import com.lgcms.consulting.repository.LectureRepository;
import com.lgcms.consulting.repository.QuestionRepository;
import com.lgcms.consulting.repository.ReviewRepository;
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

    public RepositoryItemWriter<Lecture> lectureWriter(){
        return new RepositoryItemWriterBuilder<Lecture>()
                .repository(lectureRepository)
                .methodName("save")
                .build();
    }

    public RepositoryItemWriter<Enrollment> enrollmentWriter(){
        return new RepositoryItemWriterBuilder<Enrollment>()
                .repository(enrollmentRepository)
                .methodName("save")
                .build();
    }

    public RepositoryItemWriter<Question> questionWriter(){
        return new RepositoryItemWriterBuilder<Question>()
                .repository(questionRepository)
                .methodName("save")
                .build();
    }

    public RepositoryItemWriter<Review> reviewWriter(){
        return new RepositoryItemWriterBuilder<Review>()
                .repository(reviewRepository)
                .methodName("save")
                .build();
    }
}
