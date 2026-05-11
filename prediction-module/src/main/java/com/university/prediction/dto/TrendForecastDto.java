package com.university.prediction.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record TrendForecastDto(
        @NotBlank(message = "topic is required")
        String topic,

        @DecimalMin(value = "0.0", message = "probability must be at least 0")
        @DecimalMax(value = "1.0", message = "probability must be at most 1")
        double probability,

        @NotBlank(message = "timeframe is required")
        String timeframe
) {}
