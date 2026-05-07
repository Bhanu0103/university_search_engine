package com.university.search.dto;

import java.util.List;

public record SearchQueryDto(String query, List<String> fields, int page, int size) {}
