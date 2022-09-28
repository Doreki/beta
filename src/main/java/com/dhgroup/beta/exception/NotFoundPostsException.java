package com.dhgroup.beta.exception;

public class NotFoundPostsException extends RuntimeException{
    public NotFoundPostsException() {
    }

    public NotFoundPostsException(String message) {
        super(message);
    }
}
