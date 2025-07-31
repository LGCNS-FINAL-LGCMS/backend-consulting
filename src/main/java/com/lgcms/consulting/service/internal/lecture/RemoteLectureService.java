package com.lgcms.consulting.service.internal.lecture;

import com.lgcms.consulting.dto.response.lecture.RemoteLectureResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "RemoteLectureService",
        path = "/internal/lecture"
)
public interface RemoteLectureService {
    @GetMapping("/data")
    public ResponseEntity<List<RemoteLectureResponse.LectureMetaResponse>> getDataFromLecture();

    @GetMapping("/questions")
    public ResponseEntity<List<RemoteLectureResponse.LectureQuestionsResponse>> getQuestionsFromLecture();

    @GetMapping("/enrollments")
    public ResponseEntity<List<RemoteLectureResponse.LectureEnrollmentsResponse>> getEnrollmentsFromLecture();

    @GetMapping("/reviews")
    public ResponseEntity<List<RemoteLectureResponse.LectureReviewsResponse>> getReviewsFromLecture();
}
