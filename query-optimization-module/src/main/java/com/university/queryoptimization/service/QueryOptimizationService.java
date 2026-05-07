package com.university.queryoptimization.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueryOptimizationService {
    private final DocumentRepository documentRepository;

    public QueryOptimizationService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<String> getSuggestions(String prefix) {
        // Find universities that start with the given prefix
        List<DocumentEntity> entities = documentRepository.findByUniversityNameStartingWithIgnoreCase(prefix);
        
        // Return only the names
        return entities.stream()
                .map(DocumentEntity::getUniversityName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> autoComplete(String prefix) {
        return getSuggestions(prefix);
    }

    public String correctSpelling(String wrongQuery) {
        if (wrongQuery == null || wrongQuery.isBlank()) {
            return "";
        }

        Map<String, String> corrections = Map.of(
                "artifical", "artificial",
                "inteligence", "intelligence",
                "compter", "computer",
                "universty", "university",
                "sciense", "science",
                "enginering", "engineering"
        );

        String corrected = wrongQuery;
        for (Map.Entry<String, String> entry : corrections.entrySet()) {
            corrected = corrected.replaceAll("(?i)\\b" + entry.getKey() + "\\b", entry.getValue());
        }
        return corrected;
    }

    public List<String> expandQuery(String baseQuery) {
        if (baseQuery == null || baseQuery.isBlank()) {
            return List.of();
        }
        String normalized = baseQuery.trim();
        if ("AI".equalsIgnoreCase(normalized)) {
            return List.of("artificial intelligence", "machine learning", "deep learning", "neural networks");
        }
        if (normalized.toLowerCase().contains("cs") || normalized.toLowerCase().contains("computer")) {
            return List.of(normalized, "computer science", "software engineering", "data structures");
        }
        return List.of(normalized, normalized + " universities", normalized + " courses", normalized + " research");
    }
}
