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
}
