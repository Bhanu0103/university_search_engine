package com.university.analytics.dto;

import jakarta.validation.constraints.NotBlank;

public record StatRecord(
        @NotBlank(message = "metric is required")
        String metric
) {}
