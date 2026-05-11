package com.university.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotificationEventDto(
        @NotBlank(message = "targetUserId is required")
        String targetUserId,

        @NotBlank(message = "eventType is required")
        String eventType,

        @NotBlank(message = "message is required")
        @Size(max = 500, message = "message must be at most 500 characters")
        String message
) {}
