package com.university.personalization.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRecord(
        @NotBlank(message = "userId is required")
        String userId
) {}
