package com.LanguageDive.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String key, Object value) {
        super(String.format("%s not found with %s: %s", resource, key, value));
    }

    public ResourceNotFoundException(String resource, Object value) {
        super(String.format("%s not found with id: %s", resource, value));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
