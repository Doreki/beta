package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundPostsException extends RuntimeException{
    public NotFoundPostsException(String message) {
        super(message);
    }

}
