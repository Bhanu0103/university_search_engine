package com.university.filter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Map;

public record FacetResultDto(
        @NotBlank(message = "field is required")
        String field,

        @NotEmpty(message = "counts must not be empty")
        Map<@NotBlank(message = "facet value must not be blank") String, @PositiveOrZero(message = "count must be zero or greater") Integer> counts
) {}
