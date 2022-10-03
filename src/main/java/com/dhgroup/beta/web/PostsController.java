package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.exception.MemberNotMatchException;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;

    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;



    @GetMapping("/api/v1/posts")
    public List<PostsResponseDto> viewPostsList(Pageable pageable) {

        return postsService.viewPostsList(pageable);
    }

    @PostMapping("/api/v1/posts")
    public Long write(@RequestBody PostsRequestDto postsRequestDto) {
        return postsService.write(postsRequestDto);
    }

    @PatchMapping("/api/v1/posts/{postsId}")
    public void update(@PathVariable Long postsId,
                       @RequestBody PostsUpdateDto postsUpdateDto
                       ) {
        if(postsService.authorCheck(postsId, postsUpdateDto.getGoogleId()))
            postsService.update(postsId, postsUpdateDto);
        else
            throw new MemberNotMatchException("권한이 없습니다.");
    }

    @DeleteMapping("/api/v1/posts/{postsId}")
    public void delete(@PathVariable Long postsId) {
        postsService.delete(postsId);
    }

    @PatchMapping("/api/v1/posts/like/{postsId}")
    public void like(@PathVariable Long postsId) {
        postsService.likeIncrease(postsId);
    }

    @PatchMapping("/api/v1/posts/likeRollback/{id}")
    public void likeRollback(@PathVariable Long id) {
        postsService.likeRollback(id);
    }

}
