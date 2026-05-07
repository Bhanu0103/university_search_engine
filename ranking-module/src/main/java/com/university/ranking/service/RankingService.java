package com.university.ranking.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankingService {
    private final DocumentRepository documentRepository;

    public RankingService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<DocumentEntity> rankResults(List<DocumentEntity> results) {
        // Sort by ranking (ascending: 1.0 is better than 10.0)
        // Handle nulls by putting them at the end
        return results.stream()
                .sorted(Comparator.comparing(DocumentEntity::getRanking, 
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public double score(Object record) {
        return 1.0; // Default score
    }

    public Map<String, Double> calculateScores(List<String> documentIds) {
        Map<String, Double> scores = new LinkedHashMap<>();
        for (String documentId : documentIds) {
            findDocument(documentId).ifPresent(document -> scores.put(documentId, scoreDocument(document)));
        }
        return scores;
    }

    public Map<String, Double> boostResults(List<String> documentIds, String boostFactor) {
        double factor = parseBoostFactor(boostFactor);
        Map<String, Double> scores = new LinkedHashMap<>();
        for (String documentId : documentIds) {
            findDocument(documentId).ifPresent(document -> scores.put(documentId, scoreDocument(document) * factor));
        }
        return scores;
    }

    public Map<String, Double> applyRankingModel(String modelName) {
        double modelMultiplier = switch (modelName == null ? "" : modelName.toLowerCase()) {
            case "research-priority-model" -> 1.20;
            case "student-priority-model" -> 1.10;
            default -> 1.0;
        };

        return documentRepository.findAll().stream()
                .sorted(Comparator.comparing(DocumentEntity::getRanking, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toMap(
                        document -> String.valueOf(document.getId()),
                        document -> scoreDocument(document) * modelMultiplier,
                        (left, right) -> left,
                        LinkedHashMap::new));
    }

    public Map<String, Double> personalizeRanking(String userId, List<String> documentIds) {
        double userWeight = Math.max(1.0, Math.min(1.5, (userId == null ? 0 : userId.length()) / 10.0 + 1.0));
        Map<String, Double> scores = new LinkedHashMap<>();
        for (String documentId : documentIds) {
            findDocument(documentId).ifPresent(document -> scores.put(documentId, scoreDocument(document) * userWeight));
        }
        return scores;
    }

    private java.util.Optional<DocumentEntity> findDocument(String documentId) {
        try {
            return documentRepository.findById(Long.parseLong(documentId));
        } catch (NumberFormatException e) {
            return java.util.Optional.empty();
        }
    }

    private double scoreDocument(DocumentEntity document) {
        double ranking = document.getRanking() == null || document.getRanking() <= 0 ? 100.0 : document.getRanking();
        return Math.max(0.01, 100.0 / ranking);
    }

    private double parseBoostFactor(String boostFactor) {
        try {
            return Double.parseDouble(boostFactor);
        } catch (Exception e) {
            return 1.0;
        }
    }
}
