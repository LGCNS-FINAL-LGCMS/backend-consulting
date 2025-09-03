package com.lgcms.consulting.dto.response.lecture;

import com.lgcms.consulting.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public class RemoteLectureResponse {
    public record LectureAllDataResponse(
            List<LectureMetaResponse> lectures,
            List<LectureQuestionsResponse> questions,
            List<LectureEnrollmentsResponse> enrollments,
            List<LectureReviewsResponse> reviews
    ) {
    }

    public record LectureMetaResponse(
            String id,
            Long memberId,
            String title,
            String level,
            Long price,
            Long reviewCount,
            Long totalAmount
    ) {
        public Lecture toEntity() {
            return Lecture.builder()
                    .id(id)
                    .memberId(memberId)
                    .title(title)
                    .level(level)
                    .price(price)
                    .reviewCount(reviewCount)
                    .totalAmount(totalAmount)
                    .build();
        }
    }

    public record LectureProgressResponse(
        Long id,
        Long memberId,
        String lectureId,
        Integer progressRate
    ) {
        public Progress toEntity() {
            return Progress.builder()
                    .id(id)
                    .memberId(memberId)
                    .lectureId(lectureId)
                    .progressRate(progressRate)
                    .build();
        }
    }

    public record LectureQuestionsResponse(
            Long id,
            Long memberId,
            String lectureId,
            String title,
            String content,
            LocalDateTime createdAt
    ) {
        public Question toEntity() {
            return Question.builder()
                    .id(id)
                    .memberId(memberId)
                    .lectureId(lectureId)
                    .title(title)
                    .content(content)
                    .createdAt(createdAt)
                    .build();
        }
    }

    public record LectureEnrollmentsResponse(
            Long id,
            Long studentId,
            String lectureId,
            LocalDateTime enrollmentAt
    ) {
        public Enrollment toEntity() {
            return Enrollment.builder()
                    .id(id)
                    .studentId(studentId)
                    .lectureId(lectureId)
                    .enrollmentAt(enrollmentAt)
                    .build();
        }
    }

    public record LectureReviewsResponse(
            Long id,
            Long memberId,
            String lectureId,
            String suggestion,
            Integer star,
            String nickname,
            Integer difficulty,
            Integer usefulness,
            LocalDateTime createdAt
    ) {
        public Review toEntity() {
            return Review.builder()
                    .id(id)
                    .memberId(memberId)
                    .lectureId(lectureId)
                    .suggestion(suggestion)
                    .star(star)
                    .nickname(nickname)
                    .difficulty(difficulty)
                    .usefulness(usefulness)
                    .createdAt(createdAt)
                    .build();
        }
    }
}
