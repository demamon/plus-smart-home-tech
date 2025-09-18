package ru.yandex.practicum.order.exception;

public class NonAuthorizedUserException extends RuntimeException {
    public NonAuthorizedUserException(String message) {
        super(message);
    }
}
