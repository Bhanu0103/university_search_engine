package com.university;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {
    @Test
    void applicationClassIsSpringBootApplication() {
        assertThat(Application.class.getAnnotation(SpringBootApplication.class)).isNotNull();
    }
}
