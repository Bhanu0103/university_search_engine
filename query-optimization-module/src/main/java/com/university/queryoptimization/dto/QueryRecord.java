package com.university.queryoptimization.dto;

import jakarta.validation.constraints.NotBlank;

public record QueryRecord(
        @NotBlank(message = "raw query is required")
        String raw
) {}
