package com.university.search.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import com.university.ranking.service.RankingService;
import com.university.notification.service.NotificationService;
import com.university.abusedetection.service.AbuseDetectionService;
import com.university.analytics.service.AnalyticsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final DocumentRepository documentRepository;
    private final RankingService rankingService;
    private final NotificationService notificationService;
    private final AbuseDetectionService abuseDetectionService;
    private final AnalyticsService analyticsService;

    public SearchService(DocumentRepository documentRepository, 
                         RankingService rankingService,
                         NotificationService notificationService,
                         AbuseDetectionService abuseDetectionService,
                         AnalyticsService analyticsService) {
        this.documentRepository = documentRepository;
        this.rankingService = rankingService;
        this.notificationService = notificationService;
        this.abuseDetectionService = abuseDetectionService;
        this.analyticsService = analyticsService;
    }

    public List<DocumentEntity> search(String query) {
        // 1. Check for Abuse FIRST
        if (abuseDetectionService.isAbusive(query)) {
            notificationService.sendAlert("⚠️ SECURITY ALERT: Abusive search attempt blocked!");
            return new ArrayList<>();
        }

        // 2. Notify & Analytics
        notificationService.sendAlert("Search performed for query: " + query);
        analyticsService.recordQuery(query); // RECORD FOR TRENDS

        // 3. Get raw results from database
        List<DocumentEntity> rawResults = documentRepository
                .findByUniversityNameContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
        
        // 4. Pass them to the Ranking module to be sorted
        return rankingService.rankResults(rawResults);
    }

    public List<DocumentEntity> advancedSearch(String query, List<String> fields) {
        List<DocumentEntity> candidates = documentRepository.findAll();
        String normalizedQuery = normalize(query);
        Set<String> normalizedFields = fields == null
                ? Set.of()
                : fields.stream().map(this::normalize).collect(Collectors.toSet());

        List<DocumentEntity> matches = candidates.stream()
                .filter(document -> matchesAnyRequestedField(document, normalizedQuery, normalizedFields))
                .collect(Collectors.toList());

        return rankingService.rankResults(matches);
    }

    public List<DocumentEntity> filterSearchResults(String query, String department, String location) {
        String normalizedQuery = normalize(query);
        String normalizedDepartment = normalize(department);
        String normalizedLocation = normalize(location);

        List<DocumentEntity> matches = documentRepository.findAll().stream()
                .filter(document -> normalizedQuery.isBlank()
                        || contains(document.getTitle(), normalizedQuery)
                        || contains(document.getUniversityName(), normalizedQuery)
                        || contains(document.getContent(), normalizedQuery))
                .filter(document -> normalizedDepartment.isBlank() || contains(document.getDepartment(), normalizedDepartment))
                .filter(document -> normalizedLocation.isBlank() || contains(document.getLocation(), normalizedLocation))
                .collect(Collectors.toList());

        return rankingService.rankResults(matches);
    }

    public List<DocumentEntity> paginateResults(String query, int page, int size) {
        List<DocumentEntity> results = search(query);
        int safeSize = size <= 0 ? 10 : size;
        int safePage = Math.max(page, 0);
        int fromIndex = safePage * safeSize;

        if (fromIndex >= results.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + safeSize, results.size());
        return results.subList(fromIndex, toIndex);
    }

    public Optional<DocumentEntity> getSearchById(String id) {
        try {
            return documentRepository.findById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private boolean matchesAnyRequestedField(DocumentEntity document, String query, Set<String> fields) {
        if (query.isBlank()) {
            return true;
        }

        if (fields.isEmpty()) {
            return contains(document.getTitle(), query)
                    || contains(document.getUniversityName(), query)
                    || contains(document.getContent(), query)
                    || contains(document.getLocation(), query)
                    || contains(document.getDepartment(), query);
        }

        return (fields.contains("title") && contains(document.getTitle(), query))
                || (fields.contains("university") && contains(document.getUniversityName(), query))
                || (fields.contains("universityname") && contains(document.getUniversityName(), query))
                || (fields.contains("content") && contains(document.getContent(), query))
                || (fields.contains("location") && contains(document.getLocation(), query))
                || (fields.contains("department") && contains(document.getDepartment(), query));
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
