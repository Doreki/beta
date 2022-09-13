package com.dhgroup.beta.domain.exception;

public class NotFoundBoardException extends RuntimeException{
    public NotFoundBoardException() {
    }

    public NotFoundBoardException(String message) {
        super(message);
    }
}
