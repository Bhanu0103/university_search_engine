package com.university.abusedetection.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AbuseDetectionServiceTest {
    private final AbuseDetectionService service = new AbuseDetectionService();

    @Test
    void flagsQueriesLongerThanFiftyCharacters() {
        assertThat(service.isAbusive("x".repeat(51))).isTrue();
    }

    @Test
    void allowsNormalAndNullQueries() {
        assertThat(service.isAbusive("engineering")).isFalse();
        assertThat(service.isAbusive(null)).isFalse();
    }

    @Test
    void detectSearchAbuseReturnsReasonForMissingIpAddress() {
        AbuseDetectionService.AbuseDecision decision = service.detectSearchAbuse("1", "");

        assertThat(decision.abusive()).isTrue();
        assertThat(decision.reason()).isEqualTo("Missing IP address");
    }

    @Test
    void flagSuspiciousUserAddsFraudReport() {
        String message = service.flagSuspiciousUser("9", "Too many requests");

        assertThat(message).isEqualTo("Flagged suspicious user 9: Too many requests");
        assertThat(service.getFraudReports("2026-05-01", "2026-05-08")).contains(message);
    }

    @Test
    void analyzeQueryPatternsReturnsSummaryJson() {
        assertThat(service.analyzeQueryPatterns("today"))
                .contains("\"timeRange\":\"today\"")
                .contains("\"summary\"");
    }
}
