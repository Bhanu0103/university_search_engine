package com.university.personalization.dto;

import java.util.List;

public record UserProfileDto(String userId, String role, List<String> searchHistory) {}
