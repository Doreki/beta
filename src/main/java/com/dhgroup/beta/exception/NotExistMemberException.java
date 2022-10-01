package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotExistMemberException extends RuntimeException{


    public NotExistMemberException(String message) {
        super(message);
    }

    public NotExistMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistMemberException(Throwable cause) {
        super(cause);
    }

    public NotExistMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
