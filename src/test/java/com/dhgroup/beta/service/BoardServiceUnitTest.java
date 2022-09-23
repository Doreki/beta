package com.dhgroup.beta.service;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;

import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.User;
import com.dhgroup.beta.domain.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {

    @Mock
    private BoardRepository boardRepository;


    @InjectMocks
    BoardService boardService;

//    @Test
//    public void 글작성() {
//
//        User user = User.builder().nickName("글쓴이").build();
//        given().willReturn(user);
//        Board board = Board.builder()
//                .title("글제목")
//                .content("글내용")
//                .user()
//                .build();
//
//        given(boardRepository.save(boardPostDto.toEntity())).willReturn(1L);
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

    @Test
     public void 글목록_불러오기() throws Exception{
        //given

        Long startId = 5L;
        List<Board> boards = new ArrayList<>();
        User user = createUser("글쓴이", "1");

        //게시글 생성
        for (int i = 0; i < 5; i++) {
            Board board = createBoard(user, "글제목", "글내용");
            boards.add(board);
        }
        given(boardRepository.findFirst10ByIdLessThanEqualOrderByIdDesc(startId)).willReturn(boards);
        //when

        List<BoardResponseDto> boardResponseDtos = boardService.viewList(startId);
        //then
        verify(boardRepository).findFirst10ByIdLessThanEqualOrderByIdDesc(startId);
        assertThat(boardResponseDtos.size()).isEqualTo(5);
    }


    private static Board createBoard(User user, String title, String content) {
        return Board.builder().title(title).content(content).user(user).build();
    }

    private static User createUser(String nickName, String googleId) {
        return User.builder().nickName(nickName).googleId(googleId).build();
    }
}