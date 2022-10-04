package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.exception.MemberNotMatchException;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.LikesRequestDto;
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
@RequestMapping("/api/v1/posts")
public class PostsController {

    private final PostsService postsService;

    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;



    @GetMapping("/")
    public List<PostsResponseDto> viewPosts(Pageable pageable) {

        return postsService.viewPosts(pageable);
    }

    @PostMapping("/")
    public Long write(@RequestBody PostsRequestDto postsRequestDto) {
        return postsService.write(postsRequestDto);
    }

    @PatchMapping("/{postsId}")
    public void update(@PathVariable Long postsId,
                       @RequestBody PostsUpdateDto postsUpdateDto) {

        if(postsService.authorCheck(postsId, postsUpdateDto.getGoogleId()))
            postsService.update(postsId, postsUpdateDto);
        else
            throw new MemberNotMatchException("권한이 없습니다.");
    }

    @DeleteMapping("/{postsId}")
    public void delete(@PathVariable Long postsId,
                       @RequestBody String googleId) {

        if(postsService.authorCheck(postsId, googleId))
            postsService.delete(postsId);
        else
            throw new MemberNotMatchException("권한이 없습니다.");
    }

    @PostMapping("/like/")
    public void like(@RequestBody LikesRequestDto likesRequestDto) {
        postsService.likeIncrease(likesRequestDto);
    }

    @PatchMapping("/likeRollback/{id}")
    public void likeRollback(@PathVariable Long id) {
        postsService.likeRollback(id);
    }

}
