package ru.bk.artv.vkrattach.exceptions;

public class OrderAcceptedException extends RuntimeException {

    public OrderAcceptedException(String message) {
        super(message);
    }

    public OrderAcceptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
