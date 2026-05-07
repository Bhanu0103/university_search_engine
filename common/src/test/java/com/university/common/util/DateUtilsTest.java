package com.university.common.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class DateUtilsTest {
    @Test
    void formatAndParseRoundTripIsoDateTime() {
        LocalDateTime value = LocalDateTime.of(2026, 5, 7, 10, 15, 30);

        String formatted = DateUtils.format(value);

        assertThat(formatted).isEqualTo("2026-05-07T10:15:30");
        assertThat(DateUtils.parse(formatted)).isEqualTo(value);
    }

    @Test
    void nullInputsReturnNull() {
        assertThat(DateUtils.format(null)).isNull();
        assertThat(DateUtils.parse(null)).isNull();
    }
}
