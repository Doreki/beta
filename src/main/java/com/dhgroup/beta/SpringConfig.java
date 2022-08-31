package com.dhgroup.beta;

import com.dhgroup.beta.domain.BoardDto;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.repository.JpaBoardRepository;
import com.dhgroup.beta.service.BoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    BoardService boardService() {
        return new BoardService(boardRepository());
    }

    @Bean
    BoardRepository boardRepository() {
        return new JpaBoardRepository();
    }

    @Bean
    BoardDto boardDto() {
        return new BoardDto();
    }
}
