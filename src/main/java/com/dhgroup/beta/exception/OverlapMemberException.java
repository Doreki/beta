package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OverlapMemberException extends RuntimeException{
    public OverlapMemberException(String message) {
        super(message);
    }
}
