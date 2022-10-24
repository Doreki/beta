package com.dhgroup.beta.web.controller.api.admin;

import com.dhgroup.beta.domain.member.Role;
import com.dhgroup.beta.exception.MemberMismatchException;
import com.dhgroup.beta.security.PrincipalDetails;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.CMResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminApi {

    private final PostsService postsService;

    @DeleteMapping("/{postsId}")
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
