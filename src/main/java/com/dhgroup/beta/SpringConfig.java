package com.dhgroup.beta;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Autowired
    BoardRepository boardRepository;
    @Bean
    BoardService boardService() {
        return new BoardService(boardRepository);
    }

    @Bean
    Board board() {
        return new Board();
    }


}
