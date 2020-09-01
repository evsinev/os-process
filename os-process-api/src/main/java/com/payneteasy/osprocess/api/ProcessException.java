package com.payneteasy.osprocess.api;

public class ProcessException extends Exception {

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
