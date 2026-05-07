package com.university.personalization.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import com.university.common.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

@Service
public class PersonalizationService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public PersonalizationService(DocumentRepository documentRepository, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    public List<DocumentEntity> getPersonalizedResults(String role) {
        System.out.println("DEBUG: Personalizing results for ROLE: " + role);
        
        if ("STUDENT".equalsIgnoreCase(role)) {
            // Students get top ranked universities
            return documentRepository.findAll().stream()
                    .filter(d -> d.getRanking() != null && d.getRanking() <= 10.0)
                    .toList();
        } else if ("RESEARCHER".equalsIgnoreCase(role)) {
            // Researchers get content related to research
            return documentRepository.findByUniversityNameContainingIgnoreCaseOrContentContainingIgnoreCase("Research", "Research");
        } else {
            // Default/Admin: Return everything
            return documentRepository.findAll();
        }
    }

    public String trackUserSearch(String userId, String eventJson) {
        validateUser(userId);
        String clickedDocumentId = readString(eventJson, "clickedDocumentId");
        if (clickedDocumentId != null && !clickedDocumentId.isBlank()) {
            findDocument(clickedDocumentId);
        }
        System.out.println("PERSONALIZATION EVENT user=" + userId + " event=" + eventJson);
        return "Tracked search event for user: " + userId;
    }

    public String buildUserProfile(String userId) {
        validateUser(userId);
        return "Built profile for user: " + userId;
    }

    public List<DocumentEntity> recommendContent(String userId) {
        var user = validateUser(userId);
        return getPersonalizedResults(user.getRole().name());
    }

    public List<DocumentEntity> getPersonalizedResults(String userId, String query) {
        var user = validateUser(userId);
        String normalizedQuery = query == null ? "" : query.toLowerCase(Locale.ROOT);
        return getPersonalizedResults(user.getRole().name()).stream()
                .filter(document -> normalizedQuery.isBlank()
                        || contains(document.getTitle(), normalizedQuery)
                        || contains(document.getUniversityName(), normalizedQuery)
                        || contains(document.getContent(), normalizedQuery))
                .toList();
    }

    private com.university.common.entity.UserEntity validateUser(String userId) {
        try {
            return userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user id: " + userId);
        }
    }

    private DocumentEntity findDocument(String documentId) {
        try {
            return documentRepository.findById(Long.parseLong(documentId))
                    .orElseThrow(() -> new IllegalArgumentException("Document not found: " + documentId));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid document id: " + documentId);
        }
    }

    private String readString(String json, String field) {
        if (json == null) {
            return "";
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\"" + java.util.regex.Pattern.quote(field) + "\"\\s*:\\s*\"([^\"]*)\"")
                .matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }
}
