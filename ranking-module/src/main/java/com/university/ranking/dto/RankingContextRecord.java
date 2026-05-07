package com.university.ranking.dto;

public record RankingContextRecord(
    double tfIdf,
    double popularity,
    double recency
) {}
