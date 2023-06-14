package com.bci.desafio.controller;

import com.bci.desafio.dto.Error;
import com.bci.desafio.exceptions.ConstraintsException;
import com.bci.desafio.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ConstraintsException.class)
    public ResponseEntity<Error> handleConstraintsException(ConstraintsException ex) {
        Error error = Error.builder()
                .timestamp(new Timestamp(new Date().getTime()))
                .detail(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            Error error = Error.builder()
                            .timestamp(new Timestamp(new Date().getTime()))
                            .detail(e.getField() + " : " + e.getDefaultMessage())
                            .code(HttpStatus.BAD_REQUEST.value())
                            .build();
            errors.add(error);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Error> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        Error error = new Error(new Timestamp(new Date().getTime()),HttpStatus.BAD_REQUEST.value() , ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
