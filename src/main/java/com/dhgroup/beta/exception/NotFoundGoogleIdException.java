package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundGoogleIdException extends RuntimeException{

    public NotFoundGoogleIdException(String message) {
        super(message);
    }
}
