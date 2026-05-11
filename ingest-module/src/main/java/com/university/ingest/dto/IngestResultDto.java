package com.university.ingest.dto;

import jakarta.validation.constraints.NotBlank;

public record IngestResultDto(
        boolean success,

        String documentId,

        @NotBlank(message = "message is required")
        String message
) {}
