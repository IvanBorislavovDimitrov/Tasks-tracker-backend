package com.tracker.taskstracker.exception;

public class TRException extends RuntimeException {

    public TRException(String message) {
        super(message);
    }

    public TRException(String message, Exception e) {
        super(message, e);
    }

    public TRException(Exception e) {
        super(e);
    }
}
