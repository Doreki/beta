package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.BoardDto;

public class JpaBoardRepository implements BoardRepository{
    @Override
    public BoardDto save(BoardDto boardDto) {
        System.out.println("DI 테스트");
        return null;
    }
}
