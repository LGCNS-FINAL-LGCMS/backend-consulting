package com.lgcms.consulting.service.ai.tools;

import com.lgcms.consulting.dto.projection.ReportDTO.*;
import com.lgcms.consulting.repository.QuestionRepository;
import com.lgcms.consulting.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataTools {
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;

    @Tool(name = "fetchReviewData", description = """
            Retrieves course review data including star ratings, difficulty feedback, usefulness scores, and textual comments for a specific instructor.
            """)
    public List<ReviewData> fetchReviewData(
            ToolContext toolContext
    ) {
        return reviewRepository.findByLecturerId((Long) toolContext.getContext().get("memberId"));
    }

    @Tool(name = "fetchQuestionData", description = """
            Retrieves student question data for a specific instructor.
            """)
    public List<String> fetchQuestionData(
            ToolContext toolContext
    ) {
        return questionRepository.findByLecturerId((Long) toolContext.getContext().get("memberId"));
    }
}
