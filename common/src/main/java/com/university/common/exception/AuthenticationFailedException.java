package com.university.common.exception;

public class AuthenticationFailedException extends UniversitySearchException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
