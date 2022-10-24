package com.dhgroup.beta.web.controller.api;

import com.dhgroup.beta.annotation.LogAspect;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.exception.MemberMismatchException;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.CMResponseDto;
import com.dhgroup.beta.web.dto.LikesDto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.LikedPostsResponseDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsUpdateDto;
import com.dhgroup.beta.web.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostsApi {

    private final PostsService postsService;

    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/list/{memberId}")
    public ResponseEntity<?> viewPosts(@PathVariable Long memberId, Pageable pageable) {
        List<PostsResponseDto> postsResponseDtos = new ArrayList<>();
        if(memberId == 0L) //로그인 안한 상태라면
             postsResponseDtos = postsService.viewPosts(pageable);
        else
             postsResponseDtos = postsService.viewPosts(memberId,pageable); //좋아요 여부 넘기기

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1, "게시글 목록이 성공적으로 불러와졌습니다.", postsResponseDtos));
    }

    @PostMapping("/")
    public ResponseEntity<?> writePosts(@Validated(ValidationSequence.class) @RequestBody PostsRequestDto postsRequestDto, BindingResult bindingResult) {

        Long postsId = postsService.writePosts(postsRequestDto);
        return ResponseEntity
                .status(CREATED)
                .body(CMResponseDto.createCMResponseDto(1,"게시글이 등록되었습니다.",postsId));
    }

    @PatchMapping("/{postsId}")
    public ResponseEntity<?> update(@PathVariable Long postsId,
                                 @Validated(ValidationSequence.class)
                                 @RequestBody PostsUpdateDto postsUpdateDto) {

        if(postsService.isWriter(postsId, postsUpdateDto.getMemberId()))
            postsService.updatePosts(postsId, postsUpdateDto);
        else
            throw new MemberMismatchException("권한이 없습니다.");

        return ResponseEntity
                .status(CREATED)
                .body(CMResponseDto.createCMResponseDto(1,"게시글이 수정되었습니다.",null));
    }

    @DeleteMapping("/{postsId}")
    public ResponseEntity<?> delete(@PathVariable Long postsId,@RequestBody Long memberId) {

        if(postsService.isWriter(postsId, memberId))
            postsService.deletePosts(postsId);
        else
            throw new MemberMismatchException("권한이 없습니다.");

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1,"게시글이 삭제되었습니다.",null));
    }

    @LogAspect
    @PostMapping("/like")
    public ResponseEntity<?> likeIncrease(@RequestBody LikesRequestDto likesRequestDto) {
        postsService.likeIncrease(likesRequestDto);

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1,"게시글에 좋아요를 눌렀습니다.",null));
    }

    @DeleteMapping("/like")
    public ResponseEntity<?> likeRollback(@RequestBody LikesRequestDto likesRequestDto) {
        postsService.likeRollback(likesRequestDto);

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1,"게시글에 좋아요를 취소하셨습니다.",null));
    }

    @GetMapping("/like/list/{memberId}")
    public ResponseEntity<?> likeList(@PathVariable Long memberId, Pageable pageable){

        List<LikedPostsResponseDto> postsResponseDtos = postsService.viewLikedPosts(memberId,pageable); //좋아요 여부 넘기기

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1, "게시글 목록이 성공적으로 불러와졌습니다.", postsResponseDtos));
    }
}
