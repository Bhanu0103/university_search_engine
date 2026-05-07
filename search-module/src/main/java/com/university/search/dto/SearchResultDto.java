package com.university.search.dto;

public record SearchResultDto(String documentId, double relevanceScore, String snippet) {}
