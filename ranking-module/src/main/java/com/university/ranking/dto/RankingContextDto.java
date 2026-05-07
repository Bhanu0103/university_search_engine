package com.university.ranking.dto;

public record RankingContextDto(String documentId, double tfIdfScore, double popularity, double recency) {}
