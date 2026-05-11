package com.university.accesscontrol.dto;

import jakarta.validation.constraints.NotBlank;

public record AccessRequestDto(
        @NotBlank(message = "userId is required")
        String userId,

        String role,

        String resourceId
) {}
