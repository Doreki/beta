package com.dhgroup.beta.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class NotValidBindingException extends RuntimeException {
    private Map<String, String > errorMap;
    public NotValidBindingException(String message, Map<String, String > errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
