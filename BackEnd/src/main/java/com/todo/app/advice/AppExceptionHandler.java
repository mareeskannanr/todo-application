package com.todo.app.advice;

import com.todo.app.exception.AppException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = exception.getBindingResult().getFieldErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity handleAppException(AppException appException) {
        return ResponseEntity.status(appException.getErrorCode()).body(appException.getErrorMessages());
    }

}
