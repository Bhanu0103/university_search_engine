package com.university.filter.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilterService {
    private final DocumentRepository documentRepository;

    public FilterService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<DocumentEntity> filterByDepartment(String department) {
        return documentRepository.findByDepartmentContainingIgnoreCase(department);
    }

    public List<DocumentEntity> filterByLocation(String location) {
        return documentRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<DocumentEntity> applyFilters(String query, String department, String location) {
        String normalizedQuery = normalize(query);
        String normalizedDepartment = normalize(department);
        String normalizedLocation = normalize(location);

        return documentRepository.findAll().stream()
                .filter(document -> normalizedQuery.isBlank()
                        || contains(document.getTitle(), normalizedQuery)
                        || contains(document.getUniversityName(), normalizedQuery)
                        || contains(document.getContent(), normalizedQuery))
                .filter(document -> normalizedDepartment.isBlank() || contains(document.getDepartment(), normalizedDepartment))
                .filter(document -> normalizedLocation.isBlank() || contains(document.getLocation(), normalizedLocation))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getFacetCounts(String query, List<String> facetFields) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        String normalizedQuery = normalize(query);
        List<DocumentEntity> documents = documentRepository.findAll().stream()
                .filter(document -> normalizedQuery.isBlank()
                        || contains(document.getTitle(), normalizedQuery)
                        || contains(document.getUniversityName(), normalizedQuery)
                        || contains(document.getContent(), normalizedQuery)
                        || contains(document.getDepartment(), normalizedQuery))
                .collect(Collectors.toList());

        for (String field : facetFields) {
            String normalizedField = normalize(field);
            for (DocumentEntity document : documents) {
                String value = facetValue(document, normalizedField);
                if (value != null && !value.isBlank()) {
                    counts.merge(normalizedField + ":" + value, 1, Integer::sum);
                }
            }
        }

        return counts;
    }

    public List<DocumentEntity> refineSearch(String query, String refineToken) {
        String normalizedQuery = normalize(query);
        String normalizedToken = normalize(refineToken);

        return documentRepository.findAll().stream()
                .filter(document -> normalizedQuery.isBlank()
                        || contains(document.getTitle(), normalizedQuery)
                        || contains(document.getUniversityName(), normalizedQuery)
                        || contains(document.getContent(), normalizedQuery))
                .filter(document -> normalizedToken.isBlank()
                        || contains(document.getDepartment(), normalizedToken)
                        || contains(document.getLocation(), normalizedToken)
                        || contains(document.getUniversityName(), normalizedToken))
                .collect(Collectors.toList());
    }

    public List<DocumentEntity> combineFilters(List<String> filterIds) {
        if (filterIds == null || filterIds.isEmpty()) {
            return List.of();
        }

        return documentRepository.findAll().stream()
                .filter(document -> filterIds.stream().anyMatch(filter ->
                        contains(document.getDepartment(), normalize(filter))
                                || contains(document.getLocation(), normalize(filter))
                                || contains(document.getUniversityName(), normalize(filter))))
                .collect(Collectors.toList());
    }

    private String facetValue(DocumentEntity document, String field) {
        return switch (field) {
            case "department" -> document.getDepartment();
            case "location" -> document.getLocation();
            case "university", "universityname" -> document.getUniversityName();
            default -> null;
        };
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
