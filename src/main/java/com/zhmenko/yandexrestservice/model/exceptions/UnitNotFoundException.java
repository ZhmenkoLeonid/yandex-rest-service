package com.zhmenko.yandexrestservice.model.exceptions;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException(String message) {
        super(message);
    }
}
