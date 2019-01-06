package com.todo.app.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class AppException extends RuntimeException {

    private HttpStatus errorCode;
    private List<String> errorMessages;

    public AppException(HttpStatus errorCode, List<String> errorMessages) {
        this.errorCode = errorCode;
        this.errorMessages = errorMessages;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
