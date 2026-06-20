package com.splitManager.splitmanager.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends BaseException{
    public AuthException(String message, HttpStatus status) {
        super(message, status);
    }
}
