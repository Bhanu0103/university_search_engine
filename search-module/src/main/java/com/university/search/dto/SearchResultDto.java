package com.university.search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record SearchResultDto(
        @NotBlank(message = "documentId is required")
        String documentId,

        @PositiveOrZero(message = "relevanceScore must be zero or greater")
        double relevanceScore,

        String snippet
) {}
