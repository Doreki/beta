package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OverlapMemberException extends RuntimeException{


    public OverlapMemberException(String message) {
        super(message);
    }

    public OverlapMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverlapMemberException(Throwable cause) {
        super(cause);
    }

    public OverlapMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
