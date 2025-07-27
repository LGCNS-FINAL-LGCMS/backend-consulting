package com.lgcms.consulting.service.vectorDB;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VectorDBService {
    private final CustomJsonReader customJsonReader;
    private final VectorStore vectorStore;

    public Integer embeddingJson() {
        List<Document> tempdocuments = customJsonReader.loadJsonAsDocuments();
        List<Document> documents = new ArrayList<>();
        Map<String, Object> documentMetadata = new HashMap<>();
        documentMetadata.put("data_type","review");
        documentMetadata.put("lecture_id","1");
        for (Document document : tempdocuments) {
            documents.add(new Document(document.getText(), documentMetadata));
        }
        vectorStore.add(documents);
        return documents.size();
    }

    public List<Document> getDocuments(String text) {
        return vectorStore.similaritySearch(SearchRequest.builder()
                        .topK(20).query(text).similarityThreshold(0.0).build());
    }
}
