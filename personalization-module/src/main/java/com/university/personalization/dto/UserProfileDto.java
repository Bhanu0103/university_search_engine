package com.university.personalization.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserProfileDto(
        @NotBlank(message = "userId is required")
        String userId,

        @NotBlank(message = "role is required")
        String role,

        List<@NotBlank(message = "search history item must not be blank") String> searchHistory
) {}
