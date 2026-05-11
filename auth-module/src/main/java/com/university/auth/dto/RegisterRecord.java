package com.university.auth.dto;

import com.university.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRecord(
        @NotBlank(message = "username is required")
        @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password must contain at least 6 characters")
        String password,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotNull(message = "role is required")
        Role role
) {}
