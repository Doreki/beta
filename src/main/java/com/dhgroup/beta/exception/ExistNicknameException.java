package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExistNicknameException extends RuntimeException{


    public ExistNicknameException(String message) {
        super(message);
    }

    public ExistNicknameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistNicknameException(Throwable cause) {
        super(cause);
    }

    public ExistNicknameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
