package com.franklin.chatapp.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException() {

    }

    public InvalidInputException(String msg) {
        super(msg);
    }
}
