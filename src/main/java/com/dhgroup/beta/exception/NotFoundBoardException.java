package com.dhgroup.beta.exception;

public class NotFoundBoardException extends RuntimeException{
    public NotFoundBoardException() {
    }

    public NotFoundBoardException(String message) {
        super(message);
    }
}
