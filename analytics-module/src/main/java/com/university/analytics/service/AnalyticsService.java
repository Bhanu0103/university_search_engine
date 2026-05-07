package com.university.analytics.service;

import com.university.analytics.entity.AnalyticsEntity;
import com.university.analytics.repository.AnalyticsRepository;
import com.university.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final AnalyticsRepository repository;
    private final DocumentRepository documentRepository;

    public AnalyticsService(AnalyticsRepository repository, DocumentRepository documentRepository) {
        this.repository = repository;
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void recordQuery(String query) {
        if (query == null || query.isBlank()) return;
        
        AnalyticsEntity entity = repository.findByQueryText(query.toLowerCase())
                .orElse(new AnalyticsEntity());
        
        if (entity.getQueryText() == null) {
            entity.setQueryText(query.toLowerCase());
            entity.setCount(1);
        } else {
            entity.incrementCount();
        }
        
        repository.save(entity);
    }

    public List<String> getTopTrending(int limit) {
        return repository.findTopQueries().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public String getSearchAnalytics(String timeRange) {
        List<AnalyticsEntity> analytics = repository.findTopAnalytics();
        int totalSearches = analytics.stream()
                .map(AnalyticsEntity::getCount)
                .filter(count -> count != null)
                .mapToInt(Integer::intValue)
                .sum();

        String topQueriesJson = analytics.stream()
                .limit(10)
                .map(entity -> "{\"query\":\"" + escape(entity.getQueryText()) + "\",\"count\":" + safeCount(entity) + "}")
                .collect(Collectors.joining(","));

        return "{\"timeRange\":\"" + escape(defaultText(timeRange, "all")) + "\","
                + "\"totalSearches\":" + totalSearches + ","
                + "\"uniqueQueries\":" + analytics.size() + ","
                + "\"topQueries\":[" + topQueriesJson + "]}";
    }

    public String getUserEngagement(String userId) {
        int totalSearches = repository.findTopAnalytics().stream()
                .map(AnalyticsEntity::getCount)
                .filter(count -> count != null)
                .mapToInt(Integer::intValue)
                .sum();
        String level = totalSearches == 0 ? "NO_ACTIVITY" : totalSearches < 5 ? "LOW" : totalSearches < 20 ? "MEDIUM" : "HIGH";

        return "{\"userId\":\"" + escape(defaultText(userId, "unknown")) + "\","
                + "\"engagementLevel\":\"" + level + "\","
                + "\"systemSearchEvents\":" + totalSearches + "}";
    }

    public String getContentPerformance(String documentId) {
        try {
            Long id = Long.parseLong(documentId);
            return documentRepository.findById(id)
                    .map(document -> "{\"documentId\":\"" + escape(documentId) + "\","
                            + "\"title\":\"" + escape(document.getTitle()) + "\","
                            + "\"ranking\":" + (document.getRanking() == null ? 0.0 : document.getRanking()) + ","
                            + "\"department\":\"" + escape(document.getDepartment()) + "\","
                            + "\"location\":\"" + escape(document.getLocation()) + "\","
                            + "\"status\":\"FOUND\"}")
                    .orElse("{\"documentId\":\"" + escape(documentId) + "\",\"status\":\"NOT_FOUND\"}");
        } catch (NumberFormatException e) {
            return "{\"documentId\":\"" + escape(documentId) + "\",\"status\":\"INVALID_ID\"}";
        }
    }

    private int safeCount(AnalyticsEntity entity) {
        return entity.getCount() == null ? 0 : entity.getCount();
    }

    private String defaultText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
