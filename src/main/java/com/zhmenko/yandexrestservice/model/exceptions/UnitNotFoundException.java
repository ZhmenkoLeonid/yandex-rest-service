package com.zhmenko.yandexrestservice.model.exceptions;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException() {
        super("Item not found");
    }
}
