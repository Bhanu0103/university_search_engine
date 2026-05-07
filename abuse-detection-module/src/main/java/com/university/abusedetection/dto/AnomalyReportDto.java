package com.university.abusedetection.dto;

public record AnomalyReportDto(String ipAddress, String userId, int requestRate, boolean isBot) {}
