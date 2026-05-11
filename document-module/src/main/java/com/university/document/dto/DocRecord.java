package com.university.document.dto;

import jakarta.validation.constraints.NotBlank;

public record DocRecord(
        @NotBlank(message = "id is required")
        String id,

        @NotBlank(message = "metadata is required")
        String metadata
) {}
