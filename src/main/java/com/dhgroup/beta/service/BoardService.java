package com.dhgroup.beta.service;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        //시간 차로 인해 게시글이 없을 수도 있기 때문에 예외 던져줌
        //DB에서 해당 게시글을 찾아오고 없으면 예외 던짐
        //자주 사용되는 메서드이기 때문에 메서드로 분리
    }

//    @Autowired


    @Transactional
    public Long write(BoardPostDto boardPostDto) {
            return boardRepository.save(boardPostDto.toEntity()).getId(); //반환값 BoardRepository
    }

    @Transactional
    public Long update(Long id, BoardUpdateDto boardUpdateDto) {

        Board board = findById(id);
        board.update(boardUpdateDto.getTitle(),boardUpdateDto.getContent());
        //게시글이 있으면 글 내용을 수정
        return id;
    }


    @Transactional
    public Long delete(Long id,String writer) {
        Board board = findById(id);
        if(board.getWriter() == writer) //게시글 작성자와 session의 작성자가 똑같다면
        boardRepository.delete(board);
        return id;
    }

    @Transactional
    public BoardResponseDto read(Long id) {
        Board board = findById(id);
        //Entity의 내용을 Dto에 담는다
        return new BoardResponseDto(board);
    }

    @Transactional
    public Integer likeIncrease(Long id) {
        Board board = findById(id);
        board.likeIncrease();

        return board.getLikeCnt();
    }

    //프론트 단에서 기능 구현해야함
    @Transactional
    public Integer likeRollback(Long id) {
        Board board = findById(id);
        board.likeCancle();

        return board.getLikeCnt();
    }

    @Transactional
    public List<BoardResponseDto> viewList(Integer scroll) {

        Board board = boardRepository.findTopByOrderByIdDesc();  //3,2,1 -> 3
        Long startId = board.getId()-scroll*10;
        List<BoardResponseDto> boardList = boardRepository.findFirst10ByIdLessThanEqualOrderByIdDesc(startId)
                .stream().map(BoardResponseDto::new).collect(Collectors.toList());
        //Board형태의 데이터를 BoardResponseDto로 변환시켜서 List에 담아서 보냄
        if(boardList.size()==0)
            throw new IllegalStateException("마지막 게시글 입니다.");
        return boardList;
    }
}
