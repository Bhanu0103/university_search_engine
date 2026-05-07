package com.university.analytics.service;

import com.university.analytics.entity.AnalyticsEntity;
import com.university.analytics.repository.AnalyticsRepository;
import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnalyticsServiceTest {
    private final AnalyticsRepository repository = mock(AnalyticsRepository.class);
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final AnalyticsService service = new AnalyticsService(repository, documentRepository);

    @Test
    void recordQueryCreatesLowercaseAnalyticsEntry() {
        when(repository.findByQueryText("machine")).thenReturn(Optional.empty());

        service.recordQuery("Machine");

        verify(repository).save(org.mockito.ArgumentMatchers.argThat(entity ->
                "machine".equals(entity.getQueryText()) && entity.getCount() == 1));
    }

    @Test
    void getSearchAnalyticsReturnsJsonMetrics() {
        AnalyticsEntity entity = analytics("machine", 3);
        when(repository.findTopAnalytics()).thenReturn(List.of(entity));

        assertThat(service.getSearchAnalytics("last_7_days"))
                .contains("\"timeRange\":\"last_7_days\"")
                .contains("\"totalSearches\":3")
                .contains("\"query\":\"machine\"");
    }

    @Test
    void getContentPerformanceReportsFoundDocument() {
        DocumentEntity document = new DocumentEntity();
        document.setTitle("AI Course");
        document.setRanking(2.0);
        document.setDepartment("CSE");
        document.setLocation("Chennai");
        when(documentRepository.findById(6L)).thenReturn(Optional.of(document));

        assertThat(service.getContentPerformance("6"))
                .contains("\"status\":\"FOUND\"")
                .contains("\"title\":\"AI Course\"");
    }

    private AnalyticsEntity analytics(String query, int count) {
        AnalyticsEntity entity = new AnalyticsEntity();
        entity.setQueryText(query);
        entity.setCount(count);
        return entity;
    }
}
