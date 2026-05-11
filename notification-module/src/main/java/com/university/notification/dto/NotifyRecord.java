package com.university.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotifyRecord(
        @NotBlank(message = "msg is required")
        @Size(max = 500, message = "msg must be at most 500 characters")
        String msg
) {}
