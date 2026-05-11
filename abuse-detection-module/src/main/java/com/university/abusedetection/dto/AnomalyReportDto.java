package com.university.abusedetection.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AnomalyReportDto(
        @NotBlank(message = "ipAddress is required")
        @Pattern(regexp = "^[0-9a-fA-F:.]+$", message = "ipAddress must look like an IPv4 or IPv6 address")
        String ipAddress,

        @NotBlank(message = "userId is required")
        String userId,

        @Min(value = 0, message = "requestRate must be zero or greater")
        int requestRate,

        boolean isBot
) {}
