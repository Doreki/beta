package com.dhgroup.beta;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.service.BoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

//    @Bean
//    BoardService boardService() {
//        return new BoardService(boardRepository());
//    }

    @Bean
    Board board() {
        return new Board();
    }


}
