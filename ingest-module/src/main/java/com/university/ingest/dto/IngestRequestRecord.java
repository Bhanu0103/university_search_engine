package com.university.ingest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IngestRequestRecord(
    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title must be at most 255 characters")
    String title,

    @NotBlank(message = "universityName is required")
    @Size(max = 255, message = "universityName must be at most 255 characters")
    String universityName,

    @NotBlank(message = "content is required")
    String content,

    @NotBlank(message = "location is required")
    String location,

    @NotNull(message = "ranking is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "ranking must be greater than 0")
    Double ranking,

    @NotBlank(message = "department is required")
    String department
) {}
