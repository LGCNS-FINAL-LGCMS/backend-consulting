package com.lgcms.consulting.remote.lesson.dto;

import com.lgcms.consulting.domain.Enrollment;
import com.lgcms.consulting.domain.Lecture;
import com.lgcms.consulting.domain.Question;
import com.lgcms.consulting.domain.Review;

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
            Long id,
            Long memberId,
            String title,
            String level,
            Long price,
            Long avgRating,
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
                    .avgRating(avgRating)
                    .reviewCount(reviewCount)
                    .totalAmount(totalAmount)
                    .build();
        }
    }

    public record LectureStatusResponse(

    ) {
    }

    public record LectureQuestionsResponse(
            Long id,
            Long memberId,
            Long lectureId,
            String title,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public Question toEntity() {
            return Question.builder()
                    .id(id)
                    .memberId(memberId)
                    .lectureId(lectureId)
                    .title(title)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }
    }

    public record LectureEnrollmentsResponse(
            Long id,
            Long studentId,
            Long lectureId,
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
            Long lectureId,
            String content,
            String star,
            String nickname,
            String details,
            String etc,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public Review toEntity() {
            return Review.builder()
                    .id(id)
                    .memberId(memberId)
                    .lectureId(lectureId)
                    .content(content)
                    .star(star)
                    .nickname(nickname)
                    .details(details)
                    .etc(etc)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }
    }
}
