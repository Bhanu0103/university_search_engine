package com.university.ingest.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import com.university.ingest.dto.IngestRequestRecord;
import com.university.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngestServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    private final IngestService service = new IngestService(documentRepository, notificationService);

    @Test
    void ingestSavesDocumentAndSendsNotification() {
        IngestRequestRecord record = record(null);

        String result = service.ingest(record);

        assertThat(result).isEqualTo("Successfully ingested document for university: Test University");
        verify(documentRepository).save(any(DocumentEntity.class));
        verify(notificationService).sendAlert("New university ingested: Test University");
    }

    @Test
    void indexResearchPapersAddsDefaultDepartment() {
        service.indexResearchPapers(record(""));

        verify(documentRepository).save(org.mockito.ArgumentMatchers.argThat(document ->
                "Research".equals(document.getDepartment())));
    }

    @Test
    void updateIndexAppliesJsonChangesAndFlushes() throws Exception {
        DocumentEntity document = new DocumentEntity();
        document.setId(5L);
        document.setTitle("Old");
        when(documentRepository.findById(5L)).thenReturn(Optional.of(document));
        when(documentRepository.saveAndFlush(document)).thenReturn(document);

        String result = service.updateIndex("5", "{\"title\":\"New\",\"ranking\":2.0}");

        assertThat(document.getTitle()).isEqualTo("New");
        assertThat(document.getRanking()).isEqualTo(2.0);
        assertThat(result).contains("Successfully updated document");
    }

    @Test
    void deleteFromIndexThrowsWhenDocumentMissing() {
        when(documentRepository.existsById(7L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteFromIndex("7"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Document not found");
    }

    private IngestRequestRecord record(String department) {
        return new IngestRequestRecord("Title", "Test University", "Content", "City", 3.0, department);
    }
}
