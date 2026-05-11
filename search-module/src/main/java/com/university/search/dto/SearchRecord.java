package com.university.search.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SearchRecord(
        @NotBlank(message = "query is required")
        String query,

        @Min(value = 0, message = "page must be zero or greater")
        int page
) {}
