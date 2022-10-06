package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotMatchException extends RuntimeException {
    public MemberNotMatchException(String message) {
        super(message);
    }

}
