package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.exception.MemberNotMatchException;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.LikesDto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsUpdateDto;
import com.dhgroup.beta.web.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> writePosts(@Validated(ValidationSequence.class)
                                       @RequestBody PostsRequestDto postsRequestDto) {

        Long postsId = postsService.writePosts(postsRequestDto);
        return ResponseEntity.ok(postsId);
    }

    @PatchMapping("/{postsId}")
    public ResponseEntity<?> update(@PathVariable Long postsId,
                                 @Validated(ValidationSequence.class)
                                 @RequestBody PostsUpdateDto postsUpdateDto) {

        if(postsService.isWriter(postsId, postsUpdateDto.getGoogleId()))
            postsService.updatePosts(postsId, postsUpdateDto);
        else
            throw new MemberNotMatchException("권한이 없습니다.");

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{postsId}")
    public void delete(@PathVariable Long postsId,
                       @RequestBody String googleId) {

        if(postsService.isWriter(postsId, googleId))
            postsService.deletePosts(postsId);
        else
            throw new MemberNotMatchException("권한이 없습니다.");
    }

    @PostMapping("/like")
    public void likeIncrease(@RequestBody LikesRequestDto likesRequestDto) {
        postsService.likeIncrease(likesRequestDto);
    }

    @DeleteMapping("/like")
    public void likeRollback(@RequestBody LikesRequestDto likesRequestDto) {
        postsService.likeRollback(likesRequestDto);
    }
}
