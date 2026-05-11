package com.university.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record MetricDto(
        @NotBlank(message = "metricName is required")
        String metricName,

        @PositiveOrZero(message = "value must be zero or greater")
        double value,

        @NotBlank(message = "timestamp is required")
        String timestamp
) {}
