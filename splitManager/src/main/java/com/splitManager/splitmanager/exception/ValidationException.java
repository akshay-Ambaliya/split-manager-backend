package com.splitManager.splitmanager.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationException extends BaseException {

    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message, HttpStatus.BAD_REQUEST);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}