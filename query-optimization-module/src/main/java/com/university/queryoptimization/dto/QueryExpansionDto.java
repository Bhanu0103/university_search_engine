package com.university.queryoptimization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record QueryExpansionDto(
        @NotBlank(message = "original query is required")
        String original,

        @NotEmpty(message = "expandedTerms must not be empty")
        List<@NotBlank(message = "expanded term must not be blank") String> expandedTerms
) {}
