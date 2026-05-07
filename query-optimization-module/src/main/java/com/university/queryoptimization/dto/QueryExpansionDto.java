package com.university.queryoptimization.dto;

import java.util.List;

public record QueryExpansionDto(String original, List<String> expandedTerms) {}
