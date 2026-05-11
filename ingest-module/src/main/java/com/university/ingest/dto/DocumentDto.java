package com.university.ingest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DocumentDto(
        @NotBlank(message = "title is required")
        @Size(max = 255, message = "title must be at most 255 characters")
        String title,

        @NotBlank(message = "content is required")
        String content,

        @Size(max = 100, message = "author must be at most 100 characters")
        String author,

        @NotBlank(message = "type is required")
        String type
) {}
