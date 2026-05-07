package com.university.document.dto;

import java.util.List;

public record DocumentMetadataDto(String id, String category, List<String> tags) {}
