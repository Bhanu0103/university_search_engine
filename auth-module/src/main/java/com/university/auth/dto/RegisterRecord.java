package com.university.auth.dto;

import com.university.common.enums.Role;

public record RegisterRecord(String username, String password, String email, Role role) {}
