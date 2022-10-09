package com.dhgroup.beta.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OverlapLikesException extends RuntimeException {
    public OverlapLikesException(String message) {
        super(message);
    }
}
