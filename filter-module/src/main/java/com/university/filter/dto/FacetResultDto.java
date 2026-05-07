package com.university.filter.dto;

import java.util.Map;

public record FacetResultDto(String field, Map<String, Integer> counts) {}
