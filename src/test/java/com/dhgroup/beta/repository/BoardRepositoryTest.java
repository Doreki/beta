package com.dhgroup.beta.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Before
    public void cleanup() {
        boardRepository.deleteAll();
    }

    @Test
    public void save() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String writer = "테스트 작가";
        boardRepository.save(Board.builder()
                .title(title)
                .content(content)
                .writer(writer).build());
        //when
        List<Board> boardList = boardRepository.findAll();

        Board board = boardList.get(0);
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getWriter()).isEqualTo(writer);
    }

    @Test
    public void BaseTimeEntity_등록() {
        LocalDateTime now = LocalDateTime.of(2022,9,6,0,0,0);
             boardRepository.save(Board.builder()
                        .title("글 제목")
                        .content("글 내용")
                        .writer("글 작성자")
                        .build());

             List<Board> boardList = boardRepository.findAll();

             Board board = boardList.get(0);

        System.out.println(">>>>>>>>>>> createDate=" +
                board.getCreatedDate() +", modifiedDate="+
                board.getModifiedDate());

        assertThat(board.getCreatedDate()).isAfter(now);
        assertThat(board.getModifiedDate()).isAfter(now);
    }
}