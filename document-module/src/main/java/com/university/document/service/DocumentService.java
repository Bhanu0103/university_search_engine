package com.university.document.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void saveDocument(DocumentEntity document) {
        documentRepository.save(document);
    }

    public List<DocumentEntity> searchByKeyword(String keyword) {
        return documentRepository.findByContentContainingIgnoreCase(keyword);
    }

    @Transactional
    public String uploadDocument(String content, String metadataJson) {
        DocumentEntity document = new DocumentEntity();
        document.setContent(content);
        applyMetadata(document, metadataJson);

        if (isBlank(document.getTitle())) {
            document.setTitle("Untitled Document");
        }
        if (isBlank(document.getUniversityName())) {
            document.setUniversityName("Unknown University");
        }

        documentRepository.save(document);
        return "Document uploaded with id: " + document.getId();
    }

    @Transactional
    public String updateMetadata(String documentId, String metadataJson) {
        DocumentEntity document = findExisting(documentId);
        applyMetadata(document, metadataJson);
        documentRepository.save(document);
        return "Document metadata updated: " + documentId;
    }

    @Transactional
    public String tagDocument(String documentId, List<String> tags) {
        DocumentEntity document = findExisting(documentId);
        String joinedTags = String.join(", ", tags);
        String content = document.getContent() == null ? "" : document.getContent();
        document.setContent(content + "\nTags: " + joinedTags);
        documentRepository.save(document);
        return "Document tagged: " + documentId;
    }

    @Transactional
    public String classifyDocument(String documentId) {
        DocumentEntity document = findExisting(documentId);
        if (isBlank(document.getDepartment())) {
            String content = document.getContent() == null ? "" : document.getContent().toLowerCase();
            if (content.contains("research") || content.contains("paper")) {
                document.setDepartment("Research");
            } else if (content.contains("course") || content.contains("syllabus")) {
                document.setDepartment("Courses");
            } else {
                document.setDepartment("General");
            }
            documentRepository.save(document);
        }
        return "Document classified as: " + document.getDepartment();
    }
    
    public com.university.document.dto.DocRecord getDocumentDetails(Long id) {
        var opt = documentRepository.findById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("Document not found: " + id);
        }
        var entity = opt.get();
        // For demo, we return the id and title as metadata string
        return new com.university.document.dto.DocRecord(String.valueOf(entity.getId()), entity.getTitle());
    }

    private DocumentEntity findExisting(String documentId) {
        try {
            return documentRepository.findById(Long.parseLong(documentId))
                    .orElseThrow(() -> new IllegalArgumentException("Document not found: " + documentId));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid document id: " + documentId);
        }
    }

    private void applyMetadata(DocumentEntity document, String metadataJson) {
        String title = readString(metadataJson, "title");
        String university = firstNonBlank(readString(metadataJson, "university"), readString(metadataJson, "universityName"));
        String location = readString(metadataJson, "location");
        String department = readString(metadataJson, "department");
        String ranking = readNumber(metadataJson, "ranking");

        if (!isBlank(title)) {
            document.setTitle(title);
        }
        if (!isBlank(university)) {
            document.setUniversityName(university);
        }
        if (!isBlank(location)) {
            document.setLocation(location);
        }
        if (!isBlank(department)) {
            document.setDepartment(department);
        }
        if (!isBlank(ranking)) {
            document.setRanking(Double.parseDouble(ranking));
        }
    }

    private String readString(String json, String field) {
        if (json == null) {
            return "";
        }
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(field) + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String readNumber(String json, String field) {
        if (json == null) {
            return "";
        }
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(field) + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)").matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String firstNonBlank(String first, String second) {
        return isBlank(first) ? second : first;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
