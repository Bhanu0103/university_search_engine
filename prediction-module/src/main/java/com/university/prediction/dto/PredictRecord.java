package com.university.prediction.dto;

import jakarta.validation.constraints.NotBlank;

public record PredictRecord(
        @NotBlank(message = "userId is required")
        String userId,

        @NotBlank(message = "data is required")
        String data
) {}
