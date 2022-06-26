package com.zhmenko.yandexrestservice.controllers;

import com.zhmenko.yandexrestservice.model.Error;
import com.zhmenko.yandexrestservice.model.exceptions.BadRequestException;
import com.zhmenko.yandexrestservice.model.exceptions.NotImplementedException;
import com.zhmenko.yandexrestservice.model.exceptions.UnitNotFoundException;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(UnitNotFoundException.class)
    public ResponseEntity<Error> handleException(UnitNotFoundException e) {
        return new ResponseEntity<>(new Error(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Error> handleException(BadRequestException e) {
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class,HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleException(Exception e) {
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(),"Validation Failed"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<Error> handleException() {
        return new ResponseEntity<>(new Error(HttpStatus.NOT_IMPLEMENTED.value(), "Not Implemented"), HttpStatus.NOT_IMPLEMENTED);
    }
}