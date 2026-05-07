package com.university.search.service;

import com.university.abusedetection.service.AbuseDetectionService;
import com.university.analytics.service.AnalyticsService;
import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import com.university.notification.service.NotificationService;
import com.university.ranking.service.RankingService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SearchServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final RankingService rankingService = mock(RankingService.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    private final AbuseDetectionService abuseDetectionService = mock(AbuseDetectionService.class);
    private final AnalyticsService analyticsService = mock(AnalyticsService.class);
    private final SearchService service = new SearchService(
            documentRepository, rankingService, notificationService, abuseDetectionService, analyticsService);

    @Test
    void searchRecordsAnalyticsAndRanksResults() {
        DocumentEntity document = new DocumentEntity();
        when(abuseDetectionService.isAbusive("engineering")).thenReturn(false);
        when(documentRepository.findByUniversityNameContainingIgnoreCaseOrContentContainingIgnoreCase("engineering", "engineering"))
                .thenReturn(List.of(document));
        when(rankingService.rankResults(List.of(document))).thenReturn(List.of(document));

        assertThat(service.search("engineering")).containsExactly(document);
        verify(analyticsService).recordQuery("engineering");
        verify(notificationService).sendAlert("Search performed for query: engineering");
    }

    @Test
    void searchBlocksAbusiveQueriesBeforeRepositoryLookup() {
        when(abuseDetectionService.isAbusive("x".repeat(60))).thenReturn(true);

        assertThat(service.search("x".repeat(60))).isEmpty();
        verifyNoInteractions(documentRepository, analyticsService);
    }
}
