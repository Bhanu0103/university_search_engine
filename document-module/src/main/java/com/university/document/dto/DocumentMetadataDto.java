package com.university.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record DocumentMetadataDto(
        @NotBlank(message = "id is required")
        String id,

        @NotBlank(message = "category is required")
        String category,

        @NotEmpty(message = "tags must not be empty")
        List<@NotBlank(message = "tag must not be blank") @Size(max = 50, message = "tag must be at most 50 characters") String> tags
) {}
