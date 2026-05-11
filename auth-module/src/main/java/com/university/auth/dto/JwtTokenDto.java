package com.university.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record JwtTokenDto(
        @NotBlank(message = "accessToken is required")
        String accessToken,

        String refreshToken,

        @Positive(message = "expiresIn must be positive")
        long expiresIn
) {}
