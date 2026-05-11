package com.university.accesscontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PolicyRecord(
        @NotBlank(message = "role is required")
        String role,

        @NotEmpty(message = "allowedResources must not be empty")
        java.util.List<@NotBlank(message = "allowed resource must not be blank") String> allowedResources
) {}
