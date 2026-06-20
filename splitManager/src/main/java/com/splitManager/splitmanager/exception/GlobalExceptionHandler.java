package com.splitManager.splitmanager.exception;

import com.splitManager.splitmanager.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle custom business exceptions
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseException(BaseException ex) {

        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .status(ex.getStatus())
                .data(null)
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    // 2. Handle validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Validation Failed")
                .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                .data(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // 3. Handle all other exceptions (CRASH SAFETY NET)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {

        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Internal Server Error")
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .data(null)
                .build();

        ex.printStackTrace(); // in real system use logger

        return ResponseEntity.internalServerError().body(response);
    }
}