package com.dhgroup.beta.web;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.service.BoardService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public List<Board> viewList() {
        return boardService.viewList();
    }

}
