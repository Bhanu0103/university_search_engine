package com.university.ranking.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RankingServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final RankingService service = new RankingService(documentRepository);

    @Test
    void rankResultsSortsLowestRankingFirstAndNullLast() {
        DocumentEntity top = document(1L, 1.0);
        DocumentEntity lower = document(2L, 5.0);
        DocumentEntity missing = document(3L, null);

        assertThat(service.rankResults(List.of(lower, missing, top))).containsExactly(top, lower, missing);
    }

    @Test
    void boostResultsMultipliesDocumentScoreByFactor() {
        when(documentRepository.findById(3L)).thenReturn(Optional.of(document(3L, 10.0)));

        Map<String, Double> scores = service.boostResults(List.of("3"), "1.25");

        assertThat(scores).containsEntry("3", 12.5);
    }

    @Test
    void applyRankingModelUsesResearchMultiplierForAllDocuments() {
        when(documentRepository.findAll()).thenReturn(List.of(document(1L, 10.0)));

        assertThat(service.applyRankingModel("research-priority-model")).containsEntry("1", 12.0);
    }

    private DocumentEntity document(Long id, Double ranking) {
        DocumentEntity document = new DocumentEntity();
        document.setId(id);
        document.setRanking(ranking);
        return document;
    }
}
