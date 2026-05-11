package com.university.ranking.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record RankingContextRecord(
    @PositiveOrZero(message = "tfIdf must be zero or greater")
    double tfIdf,

    @PositiveOrZero(message = "popularity must be zero or greater")
    double popularity,

    @PositiveOrZero(message = "recency must be zero or greater")
    double recency
) {}
