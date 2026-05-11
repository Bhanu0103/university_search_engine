package com.university.ranking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record RankingContextDto(
        @NotBlank(message = "documentId is required")
        String documentId,

        @PositiveOrZero(message = "tfIdfScore must be zero or greater")
        double tfIdfScore,

        @PositiveOrZero(message = "popularity must be zero or greater")
        double popularity,

        @PositiveOrZero(message = "recency must be zero or greater")
        double recency
) {}
