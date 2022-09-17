package com.dhgroup.beta.service;

import com.dhgroup.beta.exception.NotFoundBoardException;
import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.domain.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private final BoardRepository boardRepository;

    @Transactional
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        //시간 차로 인해 게시글이 없을 수도 있기 때문에 예외 던져줌
        //DB에서 해당 게시글을 찾아오고 없으면 예외 던짐
        //자주 사용되는 메서드이기 때문에 메서드로 분리
    }

    @Transactional
    public Long write(BoardPostDto boardPostDto) {
            return boardRepository.save(boardPostDto.toEntity()).getId(); //반환값 BoardRepository
    }

    @Transactional
    public void update(Long id, BoardUpdateDto boardUpdateDto) {

        Board board = findById(id);
//        if(board.getWriter() == writer)
        board.update(boardUpdateDto.getTitle(),boardUpdateDto.getContent());
        //게시글이 있으면 글 내용을 수정
    }


    @Transactional //삭제할 게시물이 없을 경우 예외처리해줘야함
    public void delete(Long id) {
        Board board = findById(id);
//        if(board.getWriter() == writer) //게시글 작성자와 session의 작성자가 똑같다면
        boardRepository.delete(board);
    }

    @Transactional
    public BoardResponseDto read(Long id) {
        Board board = findById(id);
        //Entity의 내용을 Dto에 담는다
        return new BoardResponseDto(board);
    }

    @Transactional
    public void likeIncrease(Long id) {
        Board board = findById(id);
        board.likeIncrease();
    }

    //프론트 단에서 기능 구현해야함
    @Transactional
    public void likeRollback(Long id) {
        Board board = findById(id);
        board.likeCancle();
    }

    @Transactional
    public List<BoardResponseDto> viewList(Long startId) {
        if(startId<=0) {
            throw new NotFoundBoardException("더 이상 불러들일 게시글이 없습니다.");
        }

        List<BoardResponseDto> boardList = boardRepository.findFirst10ByIdLessThanEqualOrderByIdDesc(startId)
                .stream().map(BoardResponseDto::new).collect(Collectors.toList());
        //Board형태의 데이터를 BoardResponseDto로 변환시켜서 List에 담아서 보냄

        if(boardList.size()==0)
            throw new NotFoundBoardException("더 이상 불러들일 게시글이 없습니다.");

        return boardList;
    }

    @Transactional
    public Optional<Long> findRecentBoardId() {
        Optional<Board> opt = Optional.ofNullable(boardRepository.findTopByOrderByIdDesc());
                opt.orElseThrow(() -> new NotFoundBoardException("마지막 게시글 입니다."));
        return Optional.of(opt.get().getId()); //null값 반환될 수 있기 때문
    }

    @Transactional
    public Long boardCount() {
        return boardRepository.count();
    }
}
