package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotExistMemberException extends RuntimeException{
    public NotExistMemberException(String message) {
        super(message);
    }
}
