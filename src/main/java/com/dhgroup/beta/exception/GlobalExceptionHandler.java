package com.dhgroup.beta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundBoardException.class)
    public Map<String, String> handleException(NotFoundBoardException e) {
        Map<String, String> map = new HashMap<>();
        map.put("msg","LIST_ERR");
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Map<String, String> handleException(IllegalArgumentException e) {
        Map<String, String> map = new HashMap<>();
        map.put("msg","POST_ERR");
        return map;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Map<String, String> handleException(Exception e) {
        Map<String, String> map = new HashMap<>();
        map.put("msg","NULL_ERR");
        return map;
    }
}
