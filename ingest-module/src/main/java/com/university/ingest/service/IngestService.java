package com.university.ingest.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import com.university.ingest.dto.IngestRequestRecord;
import com.university.notification.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngestService {
    private final DocumentRepository documentRepository;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IngestService(DocumentRepository documentRepository, NotificationService notificationService) {
        this.documentRepository = documentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public String ingest(IngestRequestRecord record) {
        DocumentEntity document = new DocumentEntity();
        document.setTitle(record.title());
        document.setUniversityName(record.universityName());
        document.setContent(record.content());
        document.setLocation(record.location());
        document.setRanking(record.ranking());
        document.setDepartment(record.department());

        documentRepository.save(document);
        
        // Trigger notification
        notificationService.sendAlert("New university ingested: " + record.universityName());
        
        return "Successfully ingested document for university: " + record.universityName();
    }

    public String indexCourses(IngestRequestRecord record) {
        return ingest(withDefaultDepartment(record, "Courses"));
    }

    public String indexFacultyProfiles(IngestRequestRecord record) {
        return ingest(withDefaultDepartment(record, "Faculty"));
    }

    public String indexResearchPapers(IngestRequestRecord record) {
        return ingest(withDefaultDepartment(record, "Research"));
    }

    @Transactional
    public String updateIndex(String id, String updatedJson) throws Exception {
        if (updatedJson == null || updatedJson.isBlank()) {
            throw new IllegalArgumentException("updatedJson is required");
        }

        DocumentEntity document = documentRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));

        JsonNode json = objectMapper.readTree(updatedJson);
        if (json.hasNonNull("title")) {
            document.setTitle(json.get("title").asText());
        }
        if (json.hasNonNull("university") || json.hasNonNull("universityName")) {
            document.setUniversityName(json.hasNonNull("university")
                    ? json.get("university").asText()
                    : json.get("universityName").asText());
        }
        if (json.hasNonNull("content")) {
            document.setContent(json.get("content").asText());
        }
        if (json.hasNonNull("location")) {
            document.setLocation(json.get("location").asText());
        }
        if (json.hasNonNull("ranking")) {
            document.setRanking(json.get("ranking").asDouble());
        }
        if (json.hasNonNull("department")) {
            document.setDepartment(json.get("department").asText());
        }

        DocumentEntity saved = documentRepository.saveAndFlush(document);
        notificationService.sendAlert("Updated search index document: " + id + " -> " + describe(saved));
        return "Successfully updated document in university_documents: " + describe(saved);
    }

    @Transactional
    public String deleteFromIndex(String id) {
        Long documentId = Long.parseLong(id);
        if (!documentRepository.existsById(documentId)) {
            throw new IllegalArgumentException("Document not found: " + id);
        }

        documentRepository.deleteById(documentId);
        notificationService.sendAlert("Deleted search index document: " + id);
        return "Successfully deleted document: " + id;
    }

    private IngestRequestRecord withDefaultDepartment(IngestRequestRecord record, String department) {
        return new IngestRequestRecord(
                record.title(),
                record.universityName(),
                record.content(),
                record.location(),
                record.ranking(),
                record.department() == null || record.department().isBlank() ? department : record.department()
        );
    }

    private String describe(DocumentEntity document) {
        return "{id=" + document.getId()
                + ", title='" + document.getTitle() + "'"
                + ", universityName='" + document.getUniversityName() + "'"
                + ", location='" + document.getLocation() + "'"
                + ", ranking=" + document.getRanking()
                + ", department='" + document.getDepartment() + "'}";
    }
}
