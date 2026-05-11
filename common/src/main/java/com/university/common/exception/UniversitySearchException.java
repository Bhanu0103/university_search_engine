package com.university.common.exception;

public abstract class UniversitySearchException extends RuntimeException {
    protected UniversitySearchException(String message) {
        super(message);
    }

    protected UniversitySearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
