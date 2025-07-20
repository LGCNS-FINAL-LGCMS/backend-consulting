package com.lgcms.consulting.ai.openai.service;

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
                        **Prompt:**
                        
                        Analyze lecture reviews, ratings, and frequently asked questions from a learning platform and provide concise feedback for the instructor using the JSON structure below.
                        
                        Requirements:
                        - For "advantage", summarize suitability of difficulty, usefulness of content, and overall star ratings into up to 3 single-sentence bullet points.
                        - For "weakness", analyze complaints or requests from reviews, summarizing up to 3 distinct points (leave empty if not present).
                        - For "question", summarize the most commonly asked question(s).
                        - For “summary”, write an overall evaluation of the lecture in up to 3 sentences as one paragraph.
                        - Respond in Korean.
                        
                        **JSON Structure:**
                        ```
                        {
                          "advantage": [],
                          "weakness": [],
                          "question": [],
                          "summary": ""
                        }
                        ```
                        
                        Ensure your feedback is based on the data and is concise, objective, and informative for the instructor.
                       """
                );

        return chatClient.prompt(prompt)
                .advisors(reviewAdvisor, questionAdvisor)
                .options(options)
                .call()
                .content();
    }
}
