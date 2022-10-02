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
    @ExceptionHandler(value = NotFoundPostsException.class)
    public Map<String, String> handleException(NotFoundPostsException e) {
        Map<String, String> map = putErrorMessage("LIST_ERR");
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Map<String, String> handleException(IllegalArgumentException e) {
        Map<String, String> map = putErrorMessage("POST_ERR");
        return map;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Map<String, String> handleException(Exception e) {
        Map<String, String> map = putErrorMessage("NULL_ERR");
        return map;
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @ExceptionHandler(value = NotExistMemberException.class)
    public Map<String, String> handleException(NotExistMemberException e) {
        Map<String, String> map = putErrorMessage("REDIRECT_TO_SIGNUP");
        return map;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = ExistNicknameException.class)
    public Map<String, String> handleException(ExistNicknameException e) {
        Map<String, String> map = putErrorMessage("DUPLICATED_ID");
        return map;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = MemberNotMatchException.class)
    public Map<String, String> handleException(MemberNotMatchException e) {
        Map<String, String> map = putErrorMessage("NOT_ALLOW");
        return map;
    }

    private static Map<String, String> putErrorMessage(String err) {
        Map<String, String> map = new HashMap<>();
        map.put("msg", err);
        return map;
    }
}
