package ru.yandex.practicum.warehouse.exception;

public class NoBookingFoundException extends RuntimeException {
    public NoBookingFoundException(String message) {
        super(message);
    }
}
