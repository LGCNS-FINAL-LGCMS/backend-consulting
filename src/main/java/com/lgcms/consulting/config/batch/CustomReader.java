package com.lgcms.consulting.config.batch;

import com.lgcms.consulting.remote.lesson.RemoteLectureService;
import com.lgcms.consulting.remote.lesson.dto.RemoteLectureResponse.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CustomReader {
    private final RemoteLectureService remoteLectureService;

    public ItemReader<LectureMetaResponse> lectureReader() {
        return new CustomItemReader<>(() -> remoteLectureService.getDataFromLecture().getBody());
    }

    public ItemReader<LectureEnrollmentsResponse> enrollmentReader() {
        return new CustomItemReader<>(() -> remoteLectureService.getEnrollmentsFromLecture().getBody());
    }

    public ItemReader<LectureQuestionsResponse> questionReader() {
        return new CustomItemReader<>(() -> remoteLectureService.getQuestionsFromLecture().getBody());
    }

    public ItemReader<LectureReviewsResponse> reviewReader() {
        return new CustomItemReader<>(() -> remoteLectureService.getReviewsFromLecture().getBody());
    }

    public class CustomItemReader<T> implements ItemReader<T> {
        private Integer count = 0;
        private List<T> items = null;
        private Supplier<List<T>> fetchSupplier;

        public CustomItemReader(Supplier<List<T>> fetchSupplier) {
            this.fetchSupplier = fetchSupplier;
        }

        @Override
        public T read() throws Exception {
            if (items == null) {
                items = fetchSupplier.get();
            }
            if (count < items.size()) {
                return items.get(count++);
            }
            else {
                return null;
            }

        }
    }
}
