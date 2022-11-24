package ru.bk.artv.vkrattach.exceptions;

public class ResourceNotSavedException extends RuntimeException {
    public ResourceNotSavedException(String message) {
        super(message);
    }

    public ResourceNotSavedException(String message, Throwable cause) {
        super(message, cause);
    }
}
