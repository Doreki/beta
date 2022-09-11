package com.dhgroup.beta.web;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.service.BoardService;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = {"/api/v1/board/{scroll}", "api/v1/board"})
    public List<BoardResponseDto> viewList(@PathVariable(required = false) Integer scroll) {
            if(scroll==null)
                scroll=0;
        return boardService.viewList(scroll);

    }

    @PostMapping("/api/v1/board")
    public Long write(@RequestBody BoardPostDto boardPostDto) {
        return boardService.write(boardPostDto);
    }
}
