package com.university.ingest.dto;

public record IngestRequestRecord(
    String title,
    String universityName,
    String content,
    String location,
    Double ranking,
    String department
) {}
