package com.zhmenko.yandexrestservice.model.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException() {super("Validation Failed");}
}
