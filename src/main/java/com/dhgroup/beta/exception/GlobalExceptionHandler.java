package com.dhgroup.beta.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundBoardException.class)
    public Map<String, String> handleException(NotFoundBoardException e) {
        Map<String, String> map = new HashMap<>();
        map.put("msg","LIST_ERR");
        return map;
    }

}
