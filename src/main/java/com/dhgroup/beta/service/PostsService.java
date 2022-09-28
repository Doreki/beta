package com.dhgroup.beta.service;

import com.dhgroup.beta.exception.NotFoundPostsException;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.web.dto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public Posts findById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        //시간 차로 인해 게시글이 없을 수도 있기 때문에 예외 던져줌
        //DB에서 해당 게시글을 찾아오고 없으면 예외 던짐
        //자주 사용되는 메서드이기 때문에 메서드로 분리
    }

    @Transactional
    public Long write(PostsRequestDto postsRequestDto) {
            return postsRepository.save(postsRequestDto.toEntity()).getId(); //반환값 PostsRepository
    }

    @Transactional
    public void update(Long id, PostsUpdateDto postsUpdateDto) {

        Posts posts = findById(id); //영속성 컨테스트에 올린다.
        posts.update(postsUpdateDto.getTitle(), postsUpdateDto.getContent());
        //게시글이 있으면 글 내용을 수정
    }


    @Transactional //삭제할 게시물이 없을 경우 예외처리해줘야함
    public void delete(Long id) {
        Posts posts = findById(id);
        postsRepository.delete(posts);
    }



    @Transactional
    public void likeIncrease(Long id) {
        Posts posts = findById(id);
        posts.likeIncrease();
    }

    //프론트 단에서 기능 구현해야함
    @Transactional
    public void likeRollback(Long id) {
        Posts posts = findById(id);
        posts.likeCancle();
    }

    public List<PostsResponseDto> viewList(Long startId) {
        if(startId<=0) {
            throw new NotFoundPostsException("더 이상 불러들일 게시글이 없습니다.");
        }

        List<PostsResponseDto> postsList = postsRepository.findFirst10ByIdLessThanEqualOrderByIdDesc(startId)
                .stream().map(PostsResponseDto::new).collect(Collectors.toList());
        //Posts형태의 데이터를 PostsResponseDto로 변환시켜서 List에 담아서 보냄

        if(postsList.size()==0)
            throw new NotFoundPostsException("더 이상 불러들일 게시글이 없습니다.");

        return postsList;
    }

    public Optional<Long> findRecentPostsId() {
        Optional<Posts> opt = Optional.ofNullable(postsRepository.findTopByOrderByIdDesc());
                opt.orElseThrow(() -> new NotFoundPostsException("마지막 게시글 입니다."));
        return Optional.of(opt.get().getId()); //null값 반환될 수 있기 때문
    }

    public Long postsCount() {
        return postsRepository.count();
    }

    public PostsResponseDto read(Long id) {
        Posts posts = findById(id);
        //Entity의 내용을 Dto에 담는다
        return new PostsResponseDto(posts);
    }
}
