package com.dhgroup.beta.service;

import com.dhgroup.beta.aop.annotation.LogAspect;
import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.LikesRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.exception.NotFoundPostsException;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.exception.OverlapLikesException;
import com.dhgroup.beta.web.dto.LikesDto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    private final MemberRepository memberRepository;

    private final LikesRepository likesRepository;



    @Transactional
    public Long writePosts(PostsRequestDto postsRequestDto) {
        Member member = findMemberByMemberId(postsRequestDto.getMemberId());
        return postsRepository.save(postsRequestDto.toEntity(member)).getId(); //반환값 PostsRepository
    }

    @Transactional
    public void updatePosts(Long id, PostsUpdateDto postsUpdateDto) {

        Posts posts = findPostsByPostsId(id);
        posts.update(postsUpdateDto.getTitle(), postsUpdateDto.getContent());
    }


    @Transactional //삭제할 게시물이 없을 경우 예외처리해줘야함
    public void deletePosts(Long id) {
        postsRepository.delete(findPostsByPostsId(id));
    }


    public List<PostsResponseDto> viewPosts(Long memberId, Pageable pageable) {

        Page<Posts> findPosts = postsRepository.findAllByOrderByIdDesc(pageable);

        updateWhetherIsLiked(memberId, findPosts);
        List<PostsResponseDto> postsList = convertToDto(findPosts);

        postsListSizeCheck(postsList);

        return postsList;
    }

    public List<PostsResponseDto> viewPosts(Pageable pageable) {

        Page<Posts> findPosts = postsRepository.findAllByOrderByIdDesc(pageable);

        List<PostsResponseDto> postsList = convertToDto(findPosts);

        postsListSizeCheck(postsList);

        return postsList;
    }

    private static List<PostsResponseDto> convertToDto(Page<Posts> findPosts) {
        return findPosts
                .stream()
                .map(PostsResponseDto::createPostsResponseDto)
                .collect(Collectors.toList());
    }

    private static List<PostsResponseDto> convertToDto(List<Posts> findPosts) {
        return findPosts
                .stream()
                .map(PostsResponseDto::createPostsResponseDto)
                .collect(Collectors.toList());
    }

    void updateWhetherIsLiked(Long memberId, Page<Posts> findPosts) {
        findPosts
                .stream()
                .forEach(posts -> posts.updateIsLiked(likesRepository.existsByMemberIdAndPostsId(memberId, posts.getId())));
    }

    void updateWhetherIsLiked(Long memberId, List<Posts> findPosts) {
        findPosts
                .stream()
                .forEach(posts -> posts.updateIsLiked(likesRepository.existsByMemberIdAndPostsId(memberId, posts.getId())));
    }

    public boolean isWriter(Long postsId, Long memberId) {
        String nicknameByMobile = memberRepository.findById(memberId).get().getNickname();
        String nicknameByPosts = postsRepository.findById(postsId).get().getMember().getNickname();

        if(nicknameByMobile==nicknameByPosts)
            return true;
        else
            return false;
    }

    @Transactional
    public void likeIncrease(LikesRequestDto likesRequestDto) {
        Posts findPosts = findPostsByPostsId(likesRequestDto.getPostsId());
        Member findMember = findMemberByMemberId(likesRequestDto.getMemberId());

        try{
            Likes likes = likesRequestDto.toEntity(findPosts,findMember);
            likesRepository.save(likes);
            findPosts.like();
        } catch (DataIntegrityViolationException e) {
            throw new OverlapLikesException("하나의 게시글에 중복으로 좋아요를 누를 수 없습니다.");
        }
    }

    @Transactional
    public void likeRollback(LikesRequestDto likesRequestDto) {
        Posts findPosts = findPostsByPostsId(likesRequestDto.getPostsId());
        findPosts.likeRollback();
        likesRepository.deleteByMemberIdAndPostsId(likesRequestDto.getMemberId(), likesRequestDto.getPostsId());
    }

    private Posts findPostsByPostsId(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostsException("해당 게시글이 없습니다."));
    }

    private Member findMemberByMemberId(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    @LogAspect
    public List<PostsResponseDto> viewLikedPosts(Long memberId, Pageable pageable) {
        Page<Likes> findLikesList = likesRepository.findLikesByMemberIdOrderByDesc(memberId,pageable);
        List<Posts> findPostsList = convertToPosts(findLikesList);
        List<Long> findLikedPostsIds = convertToPostsIds(findPostsList);
        List<Posts> findLikedPosts = postsRepository.findLikedPostsByLatestOrder(findLikedPostsIds);

        updateWhetherIsLiked(memberId, findLikedPosts);
        updateLikedDate(memberId,findLikedPosts);
        List<PostsResponseDto> likedPostsDtos = convertToDto(findLikedPosts);

        postsListSizeCheck(likedPostsDtos);
        return likedPostsDtos;
    }

    void updateLikedDate(Long memberId,List<Posts> findLikedPosts) {
        findLikedPosts.stream().forEach(posts -> posts.updateLikedDate(likesRepository.findByPostsIdAndMemberId(posts.getId(),memberId).get().getLikedDate()));
    }

    private static List<Long> convertToPostsIds(List<Posts> findPostsList) {
        return findPostsList.stream().map(Posts::getId).collect(Collectors.toList());
    }

    private static List<Posts> convertToPosts(Page<Likes> findLikesList) {
        return findLikesList.stream()
                .map(Likes::getPosts).collect(Collectors.toList());
    }

    private static void postsListSizeCheck(List<PostsResponseDto> postsList) {
        if(postsList.size()==0)
            throw new NotFoundPostsException("더 이상 불러들일 게시글이 없습니다.");
    }
}
