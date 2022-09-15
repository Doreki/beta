//package com.dhgroup.beta.service;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.BDDMockito.*;
//
//import com.dhgroup.beta.domain.exception.NotFoundBoardException;
//import com.dhgroup.beta.repository.Board;
//import com.dhgroup.beta.repository.BoardRepository;
//import com.dhgroup.beta.web.dto.BoardPostDto;
//import com.dhgroup.beta.web.dto.BoardResponseDto;
//import com.dhgroup.beta.web.dto.BoardUpdateDto;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//
//@ExtendWith(MockitoExtension.class)
//public class BoardServiceUnitTest {
//
//    @Mock
//    private BoardRepository boardRepository;
//
//    @InjectMocks
//    BoardService boardService;
//
//    @Test
//    public void 글작성() {
//        BoardPostDto boardPostDto = boardPostDto();
//
//        doReturn(new Board()).when(boardRepository).save(any(Board.class));
//
//        Long id = boardService.write(boardPostDto);
//
//        assertThat(id).isEqualTo(1);
//    }
//
//    @Test
//    public void 게시글목록() {
//        List<Board> boardList = boardList();
//        Long id = boardList.get(9).getId();
//        doReturn(boardList()).when(boardRepository).findFirst10ByIdLessThanEqualOrderByIdDesc(id);
//    }
//    private BoardPostDto boardPostDto() {
//        return BoardPostDto.builder()
//                .title("글제목")
//                .content("글내용")
//                .build();
//    }
//
//    private List<Board> boardList() {
//        List<Board> boardList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            boardList.add(new Board("title","content","writer"));
//        }
//        return boardList;
//    }
//}