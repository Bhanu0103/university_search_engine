package com.university.queryoptimization.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryOptimizationServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final QueryOptimizationService service = new QueryOptimizationService(documentRepository);

    @Test
    void suggestQueryReturnsDistinctUniversityNamesStartingWithPrefix() {
        DocumentEntity first = document("Stanford University");
        DocumentEntity duplicate = document("Stanford University");
        when(documentRepository.findByUniversityNameStartingWithIgnoreCase("stan"))
                .thenReturn(List.of(first, duplicate));

        assertThat(service.getSuggestions("stan")).containsExactly("Stanford University");
    }

    @Test
    void correctSpellingFixesKnownMisspellings() {
        assertThat(service.correctSpelling("artifical inteligence for enginering"))
                .isEqualTo("artificial intelligence for engineering");
    }

    @Test
    void expandQueryReturnsAiSynonyms() {
        assertThat(service.expandQuery("AI"))
                .containsExactly("artificial intelligence", "machine learning", "deep learning", "neural networks");
    }

    private DocumentEntity document(String university) {
        DocumentEntity document = new DocumentEntity();
        document.setUniversityName(university);
        return document;
    }
}
