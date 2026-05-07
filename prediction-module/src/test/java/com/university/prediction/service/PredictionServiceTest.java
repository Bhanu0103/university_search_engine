package com.university.prediction.service;

import com.university.accesscontrol.service.AccesscontrolService;
import com.university.analytics.entity.AnalyticsEntity;
import com.university.analytics.repository.AnalyticsRepository;
import com.university.common.entity.UserEntity;
import com.university.common.enums.Role;
import com.university.common.repository.UserRepository;
import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import com.university.prediction.dto.PredictRecord;
import com.university.prediction.repository.PredictionRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PredictionServiceTest {
    private final PredictionRepository predictionRepository = mock(PredictionRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AccesscontrolService accessControlService = mock(AccesscontrolService.class);
    private final AnalyticsRepository analyticsRepository = mock(AnalyticsRepository.class);
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final PredictionService service = new PredictionService(
            predictionRepository, userRepository, accessControlService, analyticsRepository, documentRepository);

    @Test
    void forecastSearchTrendsReturnsTrendJsonFromAnalytics() {
        AnalyticsEntity entity = new AnalyticsEntity();
        entity.setQueryText("machine");
        entity.setCount(10);
        when(analyticsRepository.findTopAnalytics()).thenReturn(List.of(entity));

        assertThat(service.forecastSearchTrends("next_30_days"))
                .contains("\"timeRange\":\"next_30_days\"")
                .contains("\"forecast\":\"high\"");
    }

    @Test
    void recommendContentOptimizationReportsMissingMetadata() {
        DocumentEntity document = new DocumentEntity();
        document.setTitle("Short Doc");
        document.setRanking(1.0);
        document.setContent("short");
        when(documentRepository.findById(6L)).thenReturn(Optional.of(document));

        assertThat(service.recommendContentOptimization("6"))
                .contains("Add more descriptive content");
    }

    @Test
    void predictRejectsNonAdminUsers() {
        UserEntity user = new UserEntity();
        user.setRole(Role.STUDENT);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.predict(new PredictRecord("1", "data")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Access denied");
    }
}
