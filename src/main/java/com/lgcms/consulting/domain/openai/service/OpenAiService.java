package com.lgcms.consulting.domain.openai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAiChatModel openAiChatModel;
    private final VectorStore vectorStore;

    public ChatResponse chat(String text){
        ChatClient chatClient = ChatClient.create(openAiChatModel);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(0.7)
                .build();

        return chatClient.prompt()
                .options(options)
                .user(text)
                .call()
                .chatResponse();
    }

    public String getReport() {
        ChatClient chatClient = ChatClient.create(openAiChatModel);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(0.7)
                .maxCompletionTokens(1000)
                .build();

        FilterExpressionBuilder b = new FilterExpressionBuilder();


        Advisor reviewAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .filterExpression(b.eq("data_type", "review").build())
                        .topK(20)
                        .build())
                .build();

        Advisor questionAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .filterExpression(b.eq("data_type", "question").build())
                        .topK(20)
                        .build())
                .build();

        Prompt prompt = new Prompt
                ("""
                       당신은 데이터 분석가 입니다.
                       수강생들의 수강평을 분석해서 난이도의 적절성, 강의 내용의 유익함, 건의사항을 정리해서 보여주세요.
                       그리고 가장 많이 나온 질문은 무엇인지 분석해주세요.
                       """
                );

        return chatClient.prompt(prompt)
                .advisors(reviewAdvisor, questionAdvisor)
                .options(options)
                .call()
                .content();
    }
}
