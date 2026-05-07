package com.university.auth.dto;

public record JwtTokenDto(String accessToken, String refreshToken, long expiresIn) {}
