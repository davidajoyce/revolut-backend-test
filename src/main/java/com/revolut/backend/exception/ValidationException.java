package com.revolut.backend.exception;

public class ValidationException extends Throwable {
    private int code;
    private String message;
    public ValidationException() {
        this(500);
    }
    public ValidationException(int code) {
        this(code, "Error while processing the request", null);
    }
    public ValidationException(int code, String message) {
        this(code, message, null);
    }
    public ValidationException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public String getMessage(){ return message; };

}
