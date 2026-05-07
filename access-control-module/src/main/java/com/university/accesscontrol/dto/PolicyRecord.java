package com.university.accesscontrol.dto;

public record PolicyRecord(String role, java.util.List<String> allowedResources) {}
