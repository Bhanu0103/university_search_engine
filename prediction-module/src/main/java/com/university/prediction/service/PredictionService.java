package com.university.prediction.service;

import com.university.analytics.entity.AnalyticsEntity;
import com.university.analytics.repository.AnalyticsRepository;
import com.university.document.repository.DocumentRepository;
import com.university.prediction.repository.PredictionRepository;
import com.university.prediction.dto.PredictRecord;
import com.university.prediction.dto.PredictionResultDto;
import com.university.common.repository.UserRepository;
import com.university.accesscontrol.service.AccesscontrolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictionService {
    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);
    private final PredictionRepository predictionRepository;
    private final UserRepository userRepository;
    private final AccesscontrolService accessControlService;
    private final AnalyticsRepository analyticsRepository;
    private final DocumentRepository documentRepository;

    public PredictionService(PredictionRepository predictionRepository,
                             UserRepository userRepository,
                             AccesscontrolService accessControlService,
                             AnalyticsRepository analyticsRepository,
                             DocumentRepository documentRepository) {
        this.predictionRepository = predictionRepository;
        this.userRepository = userRepository;
        this.accessControlService = accessControlService;
        this.analyticsRepository = analyticsRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * Simple prediction stub – only users with ADMIN role are allowed.
     */
    public PredictionResultDto predict(PredictRecord record) {
        var userOpt = userRepository.findById(Long.valueOf(record.userId()));
        if (userOpt.isEmpty() || !userOpt.get().getRole().name().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can invoke predictions");
        }
        // In a real system you would run a model here. We'll just echo back the data.
        logger.info("Predicting for user {}", record.userId());
        return new PredictionResultDto("Prediction successful for " + record.data());
    }

    public String forecastSearchTrends(String timeRange) {
        List<AnalyticsEntity> trends = analyticsRepository.findTopAnalytics();
        String trendJson = trends.stream()
                .limit(5)
                .map(entity -> "{\"query\":\"" + escape(entity.getQueryText()) + "\","
                        + "\"currentCount\":" + safeCount(entity) + ","
                        + "\"forecast\":\"" + forecastLabel(safeCount(entity)) + "\"}")
                .collect(Collectors.joining(","));

        return "{\"timeRange\":\"" + escape(defaultText(timeRange, "all")) + "\","
                + "\"trendSource\":\"search_analytics\","
                + "\"trends\":[" + trendJson + "]}";
    }

    public String recommendContentOptimization(String documentId) {
        try {
            Long id = Long.parseLong(documentId);
            return documentRepository.findById(id)
                    .map(document -> {
                        String suggestion;
                        if (document.getRanking() == null || document.getRanking() <= 0) {
                            suggestion = "Add a valid ranking value for better scoring.";
                        } else if (document.getContent() == null || document.getContent().length() < 80) {
                            suggestion = "Add more descriptive content so search and filtering can match this document.";
                        } else if (document.getDepartment() == null || document.getDepartment().isBlank()) {
                            suggestion = "Add department metadata to improve filtering and facets.";
                        } else if (document.getLocation() == null || document.getLocation().isBlank()) {
                            suggestion = "Add location metadata to improve local search relevance.";
                        } else {
                            suggestion = "Content metadata looks complete. Monitor search analytics for keyword gaps.";
                        }

                        return "{\"documentId\":\"" + escape(documentId) + "\","
                                + "\"title\":\"" + escape(document.getTitle()) + "\","
                                + "\"suggestion\":\"" + escape(suggestion) + "\"}";
                    })
                    .orElse("{\"documentId\":\"" + escape(documentId) + "\",\"suggestion\":\"Document not found.\"}");
        } catch (NumberFormatException e) {
            return "{\"documentId\":\"" + escape(documentId) + "\",\"suggestion\":\"Invalid document id.\"}";
        }
    }

    public String analyzeUsagePatterns(String userId) {
        int totalSearches = analyticsRepository.findTopAnalytics().stream()
                .map(AnalyticsEntity::getCount)
                .filter(count -> count != null)
                .mapToInt(Integer::intValue)
                .sum();
        long documentCount = documentRepository.count();
        String insight = totalSearches == 0
                ? "No search activity has been recorded yet. Run SearchService/ExecuteSearch first to build usage data."
                : "Search activity is available. Popular query trends can guide recommendations and content updates.";

        return "{\"userId\":\"" + escape(defaultText(userId, "unknown")) + "\","
                + "\"totalSearchEvents\":" + totalSearches + ","
                + "\"indexedDocuments\":" + documentCount + ","
                + "\"insight\":\"" + escape(insight) + "\"}";
    }

    private int safeCount(AnalyticsEntity entity) {
        return entity.getCount() == null ? 0 : entity.getCount();
    }

    private String forecastLabel(int count) {
        if (count >= 10) {
            return "high";
        }
        if (count >= 3) {
            return "medium";
        }
        return "low";
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

//import com.university.prediction.repository.PredictionRepository;
//import com.university.prediction.entity.PredictionEntity;
//import com.university.prediction.dto.*;
//import org.springframework.stereotype.Service;
//@Service
//public class PredictionService {
//    private final PredictionRepository repository;
//    public PredictionService(PredictionRepository repository) { this.repository = repository; }
//        public void predict(PredictRecord record) { }
//}
