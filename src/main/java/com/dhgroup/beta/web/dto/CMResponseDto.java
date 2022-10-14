package com.dhgroup.beta.web.dto;

import lombok.*;

@Builder
@Data
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
