package com.lgcms.consulting.service.ai;

import com.lgcms.consulting.common.annotation.DistributedLock;
import com.lgcms.consulting.domain.LecturerReport;
import com.lgcms.consulting.dto.response.report.ReportResponse;
import com.lgcms.consulting.repository.LecturerReportRepository;
import com.lgcms.consulting.service.ai.tools.AgentTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static com.lgcms.consulting.service.ai.Prompts.REPORT_SYSTEM_PROMPT;
import static com.lgcms.consulting.service.ai.Prompts.REPORT_USER_PROMPT;

@Service
@RequiredArgsConstructor
public class BedrockService implements AiService {
    private final ChatClient chatClient;
    private final AgentTools agentTools;
    private final LecturerReportRepository lecturerReportRepository;

    @Override
    @DistributedLock(lockKey = "'LecturerReport-' + #memberId", waitTime = 10, leaseTime = 40)
    @Transactional
    public ReportResponse getReport(Long memberId) {
        LocalDateTime now = LocalDateTime.now();

        LecturerReport report = lecturerReportRepository.findByMemberIdAndDate(memberId, now);
        if (report != null) {
            return ReportResponse.builder()
                    .reviewAnalysisResult(report.getReviewAnalysisResult())
                    .qnaAnalysisResult(report.getQnaAnalysisResult())
                    .overallAnalysisResult(report.getOverallAnalysisResult())
                    .build();
        }

        String systemPrompt = REPORT_SYSTEM_PROMPT.message;
        String userPrompt = REPORT_USER_PROMPT.message;

        LocalDateTime startDate = now.minusDays(30);

        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .tools(agentTools)
                .toolContext(Map.of(
                        "memberId", memberId,
                        "startDate", startDate,
                        "endDate", now
                ))
                .call()
                .content();

        ReportResponse structuredReport = getStructuredOutput(response);

        lecturerReportRepository.save(
                LecturerReport.builder()
                        .memberId(memberId)
                        .reviewAnalysisResult(structuredReport.getReviewAnalysisResult())
                        .qnaAnalysisResult(structuredReport.getQnaAnalysisResult())
                        .overallAnalysisResult(structuredReport.getOverallAnalysisResult())
                        .build()
        );

        return structuredReport;
    }

    ReportResponse getStructuredOutput(String response) {
        String prompt = """
                Format this:
                %s
                """.formatted(response);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(ReportResponse.class);
    }
}
