package com.lgcms.consulting.api;

import com.lgcms.consulting.domain.vectorDB.service.VectorDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmbeddingController {

    private final VectorDBService vectorDBService;

    @PostMapping("/embedding")
    public Integer embedding() {
        return vectorDBService.embeddingJson();
    }
}
