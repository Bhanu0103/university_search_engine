package com.university.accesscontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AccessPolicyDto(
        @NotBlank(message = "role is required")
        String role,

        @NotEmpty(message = "allowedResources must not be empty")
        List<@NotBlank(message = "allowed resource must not be blank") String> allowedResources
) {}
