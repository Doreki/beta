package com.dhgroup.beta.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CMResponseDto<T> {
    private int status;
    private String msg;
    private T data;

    public static CMResponseDto createCMResponseDto(Integer status, String msg, Object data) {
        return new CMResponseDto(status, msg, data);
    }
}
