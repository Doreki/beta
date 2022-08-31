package com.dhgroup.beta.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class BoardDto {
    private Long bno;
    private String title;
    private String content;
    private String writer;
    private Integer view_cnt;
    private Integer comment_cnt;
    private Date reg_date;
}
