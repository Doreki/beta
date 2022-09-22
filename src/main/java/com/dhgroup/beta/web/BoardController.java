package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.User;
import com.dhgroup.beta.service.BoardService;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/api/v1/board/{lastIndex}")
    public Map<String,Object> viewList(@PathVariable(required = false) Long lastIndex) {
            if(lastIndex == 0)
                lastIndex = boardService.findRecentBoardId().orElse(0L); //게시글이 하나도 없을때 예외를 던져주기위함

        Long total = boardService.boardCount();
        List<BoardResponseDto> posts = boardService.viewList(lastIndex);
        lastIndex = posts.get(posts.size()-1).getId(); //List index가 0부터 시작하기때문

        Map<String,Object> pageHandler = new HashMap<>();
        pageHandler.put("posts",posts);
        pageHandler.put("total",total);
        pageHandler.put("lastIndex",lastIndex);
        pageHandler.put("limit",posts.size());

        return pageHandler;
    }

    @PostMapping("/api/v1/board")
    public Long write(@RequestBody BoardPostDto boardPostDto,HttpSession session) {
        User nickName = (User)session.getAttribute("nickName");
        boardPostDto.setUser(nickName);
        return boardService.write(boardPostDto);
    }

    @PatchMapping("/api/v1/board/{id}")
    public void update(@PathVariable Long id,@RequestBody BoardUpdateDto boardUpdateDto) {
//        String writer = (String)session.getAttribute("name");
        boardService.update(id,boardUpdateDto);
    }

    @DeleteMapping("/api/v1/board/{id}")
    public void delete(@PathVariable Long id,HttpSession session) {
        String nickName = (String)session.getAttribute("nickName");
        boardService.delete(id,nickName);
    }

    @PatchMapping("/api/v1/board/like/{id}")
    public void like(@PathVariable Long id) {
        boardService.likeIncrease(id);
    }

    @PatchMapping("/api/v1/board/likeRollback/{id}")
    public void likeRollback(@PathVariable Long id) {
        boardService.likeRollback(id);
    }
}
