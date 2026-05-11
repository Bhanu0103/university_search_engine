package com.university.search.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record SearchQueryDto(
        @NotBlank(message = "query is required")
        String query,

        List<@NotBlank(message = "field must not be blank") String> fields,

        @Min(value = 0, message = "page must be zero or greater")
        int page,

        @Min(value = 1, message = "size must be at least 1")
        @Max(value = 100, message = "size must be at most 100")
        int size
) {}
