package com.university.notification.dto;

public record NotificationEventDto(String targetUserId, String eventType, String message) {}
