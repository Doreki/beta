package com.dhgroup.beta.web;

import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/api/v1/posts/{lastIndex}")
    public Map<String,Object> viewList(@PathVariable(required = false) Long lastIndex) {
            if(lastIndex == 0)
                lastIndex = postsService.findRecentPostsId().orElse(0L); //게시글이 하나도 없을때 예외를 던져주기위함

        List<PostsResponseDto> posts = postsService.viewList(lastIndex);
        Long total = postsService.postsCount();
        lastIndex = posts.get(posts.size()-1).getId(); //List index가 0부터 시작하기때문

        Map<String,Object> pageHandler = new HashMap<>();
        pageHandler.put("posts",posts);
        pageHandler.put("total",total);
        pageHandler.put("lastIndex",lastIndex);
        pageHandler.put("limit",posts.size());

        return pageHandler;
    }

    @PostMapping("/api/v1/posts")
    public Long write(@RequestBody PostsRequestDto postsRequestDto) {
        return postsService.write(postsRequestDto);
    }

    @PatchMapping("/api/v1/posts/{id}")
    public void update(@PathVariable Long id,@RequestBody PostsUpdateDto postsUpdateDto) {
        postsService.update(id, postsUpdateDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public void delete(@PathVariable Long id) {
        postsService.delete(id);
    }

    @PatchMapping("/api/v1/posts/like/{id}")
    public void like(@PathVariable Long id) {
        postsService.likeIncrease(id);
    }

    @PatchMapping("/api/v1/posts/likeRollback/{id}")
    public void likeRollback(@PathVariable Long id) {
        postsService.likeRollback(id);
    }
}
