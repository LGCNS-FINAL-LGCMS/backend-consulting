package com.lgcms.consulting.api;

import com.lgcms.consulting.service.ai.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    //채팅 테스트용
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody Map<String, String> body) {
        return openAiService.chat(body.get("text"));
    }


    //레포트 작성
    @PostMapping("/report")
    public String report() {
        return openAiService.getReport();
    }

}
