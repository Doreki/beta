package com.dhgroup.beta.web;

import com.dhgroup.beta.service.BoardService;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/v1/board/{lastIndex}")
    public Map<String,Object> viewList(@PathVariable(required = false) Long lastIndex) {
            if(lastIndex == 0)
                lastIndex = boardService.findRecentBoardId().orElse(0L); //게시글이 하나도 없을때 예외를 던져주기위함

        Long total = boardService.boardCount();
        List<BoardResponseDto> boardResponseDtos = boardService.viewList(lastIndex);

        Map<String,Object> pageHandler = new HashMap<>();
        pageHandler.put("boardResponseDtos",boardResponseDtos);
        pageHandler.put("total",total);
        pageHandler.put("limit",boardResponseDtos.size());


        return pageHandler;
    }

    @PostMapping("/api/v1/board")
    public Long write(@RequestBody BoardPostDto boardPostDto) {
        return boardService.write(boardPostDto);
    }
}
