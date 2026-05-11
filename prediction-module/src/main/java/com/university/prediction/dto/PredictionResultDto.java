package com.university.prediction.dto;

import jakarta.validation.constraints.NotBlank;

public record PredictionResultDto(
        @NotBlank(message = "message is required")
        String message
) {}
