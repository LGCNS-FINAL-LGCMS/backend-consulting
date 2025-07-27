package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.domain.Enrollment;
import com.lgcms.consulting.domain.Lecture;
import com.lgcms.consulting.domain.Question;
import com.lgcms.consulting.domain.Review;
import com.lgcms.consulting.remote.lesson.dto.RemoteLectureResponse.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomProcessor {
    public ItemProcessor<LectureMetaResponse, Lecture> lectureProcessor() {
        return new ItemProcessor<LectureMetaResponse, Lecture>() {
            @Override
            public Lecture process(LectureMetaResponse item) throws Exception {
                return item.toEntity();
            }
        };
    }

    public ItemProcessor<LectureEnrollmentsResponse, Enrollment> enrollmentProcessor() {
        return new ItemProcessor<LectureEnrollmentsResponse, Enrollment>() {
            @Override
            public Enrollment process(LectureEnrollmentsResponse item) throws Exception {
                return item.toEntity();
            }
        };
    }

    public ItemProcessor<LectureQuestionsResponse, Question> questionProcessor() {
        return new ItemProcessor<LectureQuestionsResponse, Question>() {
            @Override
            public Question process(LectureQuestionsResponse item) throws Exception {
                return item.toEntity();
            }
        };
    }

    public ItemProcessor<LectureReviewsResponse, Review> reviewProcessor() {
        return new ItemProcessor<LectureReviewsResponse, Review>() {
            @Override
            public Review process(LectureReviewsResponse item) throws Exception {
                return item.toEntity();
            }
        };
    }
}