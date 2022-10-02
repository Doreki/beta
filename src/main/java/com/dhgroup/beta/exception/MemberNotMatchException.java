package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotMatchException extends RuntimeException {

    public MemberNotMatchException(String message) {
        super(message);
    }

    public MemberNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotMatchException(Throwable cause) {
        super(cause);
    }

    public MemberNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
