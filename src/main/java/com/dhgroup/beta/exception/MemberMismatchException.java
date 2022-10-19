package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberMismatchException extends RuntimeException {
    public MemberMismatchException(String message) {
        super(message);
    }

}
