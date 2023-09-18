package ru.bk.artv.vkrattach.exceptions;

public class UploadResourceException  extends RuntimeException {

    public UploadResourceException(String message) {
        super(message);
    }

    public UploadResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
