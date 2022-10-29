package com.dhgroup.beta.web.controller.api.admin;

import com.dhgroup.beta.domain.member.Role;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.exception.MemberMismatchException;
import com.dhgroup.beta.security.PrincipalDetails;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.CMResponseDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminApi {

    private final PostsService postsService;
    private final PostsRepository postsRepository;


    @GetMapping("/posts/list")
    public ResponseEntity<?> viewPosts(Pageable pageable) {
        Map<String,Object> map = new HashMap<String,Object>();

        List<PostsResponseDto> postsResponseDtos = postsService.viewPosts(pageable);
        long totalCount = postsRepository.count();
        map.put("postsResponseDtos", postsResponseDtos);
        map.put("totalCount", totalCount);

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1, "게시글 목록이 성공적으로 불러와졌습니다.", map));
    }

    @DeleteMapping("/posts/{postsId}")
    public ResponseEntity<?> deletePosts(@PathVariable Long postsId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(isAdmin(principalDetails))
            postsService.deletePosts(postsId);
        else
            throw new MemberMismatchException("권한이 없습니다.");

        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1,"게시글이 삭제되었습니다.",null));
    }
    private static boolean isAdmin(PrincipalDetails principalDetails) {
        return principalDetails.getRole() == Role.ADMIN;
    }
}
