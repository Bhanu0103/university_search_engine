package com.university.accesscontrol.dto;

import java.util.List;

public record AccessPolicyDto(String role, List<String> allowedResources) {}
