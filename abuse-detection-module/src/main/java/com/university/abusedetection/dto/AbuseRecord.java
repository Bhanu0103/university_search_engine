package com.university.abusedetection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AbuseRecord(
        @NotBlank(message = "ip is required")
        @Pattern(regexp = "^[0-9a-fA-F:.]+$", message = "ip must look like an IPv4 or IPv6 address")
        String ip
) {}
