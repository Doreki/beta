package com.dhgroup.beta.web.controller;

import com.dhgroup.beta.exception.*;
import com.dhgroup.beta.web.dto.CMResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<?> internalServerCatcher(Exception e) {
//        Map<String, String> map = putErrorMessage("알 수 없는 오류입니다.");
//        return ResponseEntity
//        .internalServerError()
//        .body(CMResponseDto.createCMResponseDto(-1, "INTERNAL_ERR",map));
//    }

    @ExceptionHandler(value = NotFoundPostsException.class)
    public ResponseEntity<?> notFoundPostsCatcher(NotFoundPostsException e) {
        Map<String, String> map = putErrorMessage(e.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(CMResponseDto.createCMResponseDto(-1, "NOT_POSTS",map));
    }


    @ExceptionHandler(value = NotExistMemberException.class)
    public ResponseEntity<?> notExistMemberCatcher(NotExistMemberException e) {
        Map<String, String> map = putErrorMessage(e.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(CMResponseDto.createCMResponseDto(-1, "NOT_MEMBER",map));
    }

    @ExceptionHandler(value = OverlapMemberException.class)
    public ResponseEntity<?> overlapMemberCatcher(OverlapMemberException e) {
        Map<String, String> map = putErrorMessage(e.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(CMResponseDto.createCMResponseDto(-1, "DUPLICATED_ID",map));
    }

    @ExceptionHandler(value = MemberNotMatchException.class)
    public ResponseEntity<?> memberNotMatchCatcher(MemberNotMatchException e) {
        Map<String, String> map = putErrorMessage(e.getMessage());
        return ResponseEntity
                .status(FORBIDDEN)
                .body(CMResponseDto.createCMResponseDto(-1,"NOT_ALLOW", map));
    }

    @ExceptionHandler(value = NotValidBindingException.class)
    public ResponseEntity<?> notValidBindingCatcher(NotValidBindingException e) {
        return ResponseEntity
                .badRequest()
                .body(CMResponseDto.createCMResponseDto(-1, e.getMessage(), e.getErrorMap()));
    }

    @ExceptionHandler(value = OverlapLikesException.class)
    public ResponseEntity<?> overlapLikesCatcher(OverlapLikesException e) {
        Map<String, String> map = putErrorMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(CMResponseDto.createCMResponseDto(-1, "DUPLICATED_LIKES", map));
    }

    private Map<String, String> putErrorMessage(String message) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("errMsg", message);
        return map;
    }
}
