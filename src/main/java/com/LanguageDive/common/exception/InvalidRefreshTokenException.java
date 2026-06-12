package com.LanguageDive.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends ApiException {
    public InvalidRefreshTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", message);
    }
}
