package com.LanguageDive.common.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String code, String message) {
        super(HttpStatus.CONFLICT, code, message);
    }
}
