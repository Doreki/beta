package com.dhgroup.beta;

import com.dhgroup.beta.domain.BoardDto;
import com.dhgroup.beta.service.BoardService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        BoardService service = context.getBean("boardService", BoardService.class);
        BoardDto boardDto = new BoardDto();
        service.write(boardDto);
    }
}
