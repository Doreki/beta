package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.LikesRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.exception.NotFoundPostsException;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.web.dto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.web.dto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    private final MemberRepository memberRepository;

    private final LikesRepository likesRepository;

    private Posts findPostsById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostsException("해당 게시글이 없습니다."));
        //시간 차로 인해 게시글이 없을 수도 있기 때문에 예외 던져줌
        //DB에서 해당 게시글을 찾아오고 없으면 예외 던짐
        //자주 사용되는 메서드이기 때문에 메서드로 분리
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    @Transactional
    public Long write(PostsRequestDto postsRequestDto) {
        Long memberId = postsRequestDto.getMemberId();
        Member member = memberRepository.findById(memberId).get();
        return postsRepository.save(postsRequestDto.toEntity(member)).getId(); //반환값 PostsRepository
    }

    @Transactional
    public void update(Long id, PostsUpdateDto postsUpdateDto) {

        Posts posts = findPostsById(id); //영속성 컨테스트에 올린다.
        posts.update(postsUpdateDto.getTitle(), postsUpdateDto.getContent());
        //게시글이 있으면 글 내용을 수정
    }


    @Transactional //삭제할 게시물이 없을 경우 예외처리해줘야함
    public void delete(Long id) {
        postsRepository.delete(findPostsById(id));
    }



    @Transactional
    public void likeIncrease(LikesRequestDto likesRequestDto) {
        Member member = findMemberById(likesRequestDto.getMemberId());
        Posts posts = findPostsById(likesRequestDto.getPostsId());
        Likes likes = likesRequestDto.toEntity(member, posts);
        likesRepository.save(likes);
    }

    //프론트 단에서 기능 구현해야함
    @Transactional
    public void likeRollback(Long id) {
        Posts posts = findPostsById(id);
        posts.likeCancle();
    }

    public List<PostsResponseDto> viewPosts(Pageable pageable) {

        List<PostsResponseDto> postsList = postsRepository.findAllByOrderByIdDesc(pageable)
                .stream().map(PostsResponseDto::new).collect(Collectors.toList());
        //Posts형태의 데이터를 PostsResponseDto로 변환시켜서 List에 담아서 보냄

        if(postsList.size()==0)
            throw new NotFoundPostsException("더 이상 불러들일 게시글이 없습니다.");

        return postsList;
    }

    public boolean authorCheck(Long postsId, String googleId) {
        String nicknameByMobile = memberRepository.findByGoogleId(googleId).get().getNickname();
        String nicknameByPosts = postsRepository.findById(postsId).get().getMember().getNickname();
        if(nicknameByMobile==nicknameByPosts) return true;
        else return false;
    }
}
