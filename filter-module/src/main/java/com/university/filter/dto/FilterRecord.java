package com.university.filter.dto;

import jakarta.validation.constraints.NotBlank;

public record FilterRecord(
        @NotBlank(message = "field is required")
        String field,

        @NotBlank(message = "value is required")
        String value
) {}
