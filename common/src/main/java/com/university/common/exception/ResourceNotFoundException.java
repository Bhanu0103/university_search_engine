package com.university.common.exception;

public class ResourceNotFoundException extends UniversitySearchException {
    public ResourceNotFoundException(String resourceName, String id) {
        super(resourceName + " not found: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
