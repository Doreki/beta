package com.dhgroup.beta.service;

import com.dhgroup.beta.exception.NotFoundBoardException;
import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;

@SpringBootTest
public class BoardServiceTest {

    private final static Integer LIMIT=10; //
    private final static Long START_ID=11L;
//    private final static Long START_ID=INITIAL_VALUE+1;
    private final static Long LAST_VALUE=START_ID-LIMIT+1;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardService boardService;

    Long id;
    @AfterEach
    public void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    public void 글작성() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        assertThat(boardRepository.findById(id).get().getId()).isEqualTo(id);
    }

    @Test
    public void 글_수정() {
        Long id= boardWrite("글제목","글내용","글쓴이");
                LocalDateTime now = LocalDateTime.now(); //글 수정전

                String writer="글쓴이"; //세션으로부터 얻어온 이름

                BoardUpdateDto updateDto = BoardUpdateDto
                        .builder()
                        .title("글제목 수정")
                        .content("글내용 수정")
                        .build();
        //when - 실행
                boardService.update(id, updateDto); //repositroy 내용 수정
            Board board = boardService.findById(id); //DB에서 수정된 시간을 가져옴
        //then - 검증
        assertThat(boardRepository.findById(id).get().getTitle()).isEqualTo("글제목 수정");
        assertThat(boardRepository.findById(id).get().getContent()).isEqualTo("글내용 수정");
        assertThat(boardRepository.findById(id).get().getWriter()).isEqualTo("글쓴이");
        assertThat(board.getModifiedDate()).isAfter(now); //글 수정 후에 수정시간이 바꼈는지 확인
        System.out.println("now = " + now);
        System.out.println("board.getModifiedDate() = " + board.getModifiedDate());
    }

    @Test
    public void 글수정_실패() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        Long wrongId = id+1;
        String writer="글쓴이"; //세션으로부터 얻어온 이름
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> boardService.update(wrongId, BoardUpdateDto
                .builder()
                .title("글제목 수정")
                .content("글내용 수정")
                .build()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }


    @Test
    public void 글삭제() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        //given
        String writer = "글쓴이"; //session으로 부터 얻어온 이름
        //when
        boardService.delete(id);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,() -> boardService.findById(id));
        //글을 찾을때 글이 없으면 삭제가 성공한 것
        //then
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

//    @Test
//    public void 글삭제_실패() {
//        Long id= boardWrite("글제목","글내용","글쓴이");
//        //given - 상황
//        String writer = "작성자 아님";
//        //when - 실행
//        boardService.delete(id);
//        //then - 검증, 글이 존재한다면 삭제 실패한 것
//        Board board = boardRepository.findById(id).get();
//        assertThat(board.getWriter()).isEqualTo("글쓴이");
//    }

    @Test
    public void 글조회() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        LocalDateTime now = LocalDateTime.now();
        BoardResponseDto responseDto = boardService.read(id);
        assertThat(responseDto.getTitle()).isEqualTo("글제목");
        assertThat(responseDto.getCreatedDate()).isBefore(now); //Dto에 생성시간 잘 들어갔는지 확인,생성이 먼저이기 때문에 생성 시간이 지금보다 더 전이어야함
    }

    @Test
    public void 좋아요버튼() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        //given
        boardService.likeIncrease(id);
        boardService.likeIncrease(id);
        //when
        Board board = boardRepository.findById(id).get();
        //then
        assertThat(board.getLikeCnt()).isEqualTo(2);
    }

    @Test
    public void 좋아요취소() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        //given
        boardService.likeIncrease(id);
        boardService.likeIncrease(id);
        boardService.likeRollback(id);
        boardService.likeRollback(id);

        Board board = boardRepository.findById(id).get();
        //when
        //then
        assertThat(board.getLikeCnt()).isEqualTo(0);
    }

    @Test
    public void 좋아요취소실패() {
        Long id= boardWrite("글제목","글내용","글쓴이");
        boardService.likeRollback(id);

        Board board = boardRepository.findById(id).get();

        assertThat(board.getLikeCnt()).isEqualTo(0);
    }

    @Test
    public void 글목록불러오기() {
        //given 총 글 11개추가
        for(int i=1;i<=11;i++) {
        boardWrite("글제목"+i,"글내용"+i,"글쓴이");
        }

        //when
        List<BoardResponseDto> boardList = boardService.viewList(START_ID);
        BoardResponseDto firstBoard = boardList.get(0);
        BoardResponseDto lastBoard = boardList.get(9);
        //then
        assertThat(boardList.size()).isEqualTo(LIMIT);
        assertThat(firstBoard.getTitle()).isEqualTo("글제목"+START_ID);
        assertThat(lastBoard.getTitle()).isEqualTo("글제목"+(START_ID-9));

        //then
        boardList = boardService.viewList(lastBoard.getId()-1);
        assertThat(boardList.size()).isEqualTo(START_ID-10);
        assertThat(boardList.get(0).getTitle()).isEqualTo("글제목"+(START_ID-10));
    }

    @Test
    public void 글목록불러오기_마지막_페이지() {
        //given
        //게시글이 아무 것도 없을때
        System.out.println("boardRepository.findTopByOrderByIdDesc() = " + boardRepository.findTopByOrderByIdDesc());
        NotFoundBoardException e = assertThrows(NotFoundBoardException.class,() ->boardService.viewList(START_ID));
        assertThat(e.getMessage()).isEqualTo("더 이상 불러들일 게시글이 없습니다.");

        for(int i=1;i<=START_ID;i++) {
            boardWrite("글제목"+i,"글내용"+i,"글쓴이");
        }

        //when 마지막 게시글을 불러올때
        e = assertThrows(NotFoundBoardException.class,() ->boardService.viewList(1L));

        //then
        assertThat(e.getMessage()).isEqualTo("더 이상 불러들일 게시글이 없습니다.");
    }

    public Long boardWrite(String title,String content,String writer) {
        BoardPostDto boardPostDto = BoardPostDto
                .builder()
                .title(title)
                .content(content)
                .build();
        boardPostDto.setWriter(writer);

        return boardService.write(boardPostDto);
    }
}