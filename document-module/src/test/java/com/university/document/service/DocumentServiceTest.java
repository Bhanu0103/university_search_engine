package com.university.document.service;

import com.university.common.exception.ResourceNotFoundException;
import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocumentServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final DocumentService service = new DocumentService(documentRepository);

    @Test
    void uploadDocumentAppliesMetadataAndDefaultsMissingValues() {
        when(documentRepository.save(any(DocumentEntity.class))).thenAnswer(invocation -> {
            DocumentEntity document = invocation.getArgument(0);
            document.setId(15L);
            return document;
        });

        String result = service.uploadDocument("course syllabus", "{\"ranking\":4.5}");

        assertThat(result).isEqualTo("Document uploaded with id: 15");
        verify(documentRepository).save(any(DocumentEntity.class));
    }

    @Test
    void classifyDocumentSetsResearchDepartmentFromContent() {
        DocumentEntity document = new DocumentEntity();
        document.setContent("Published research paper");
        when(documentRepository.findById(2L)).thenReturn(Optional.of(document));

        String result = service.classifyDocument("2");

        assertThat(result).isEqualTo("Document classified as: Research");
        assertThat(document.getDepartment()).isEqualTo("Research");
        verify(documentRepository).save(document);
    }

    @Test
    void tagDocumentAppendsTagsToExistingContent() {
        DocumentEntity document = new DocumentEntity();
        document.setContent("Original");
        when(documentRepository.findById(3L)).thenReturn(Optional.of(document));

        service.tagDocument("3", List.of("AI", "ML"));

        assertThat(document.getContent()).contains("Original").contains("Tags: AI, ML");
        verify(documentRepository).save(document);
    }

    @Test
    void getDocumentDetailsThrowsWhenMissing() {
        when(documentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDocumentDetails(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Document not found");
    }
}
