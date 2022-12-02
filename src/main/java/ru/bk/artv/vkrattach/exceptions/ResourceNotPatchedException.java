package ru.bk.artv.vkrattach.exceptions;

public class ResourceNotPatchedException extends RuntimeException {

    public ResourceNotPatchedException(String message) {
        super(message);
    }

    public ResourceNotPatchedException(String message, Throwable cause) {
        super(message, cause);
    }
}
