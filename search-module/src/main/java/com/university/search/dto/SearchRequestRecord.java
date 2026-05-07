package com.university.search.dto;

import java.util.List;

public record SearchRequestRecord(
    String query,
    List<String> filters,
    int page,
    int size
) {}
