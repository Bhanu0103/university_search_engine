package com.university.filter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

public record FilterCriteriaDto(
        @NotEmpty(message = "dynamicFilters must not be empty")
        Map<@NotBlank(message = "filter key must not be blank") String, @NotBlank(message = "filter value must not be blank") String> dynamicFilters
) {}
