# 청년부 미니 SNS
## 스프링부트 , JPA를 활용한 객체지향적인 설계 추구
### 프로젝트 소개
위 프로젝트는 SNS 애플리케이션을 만들기 위한 프로젝트로 클라이언트 영역(안드로이드,ios)과 서버(자바,스프링) 영역을 분담하여 프로젝트를 진행하였습니다.
서버의 경우 Spring boot와 JPA를 중점적으로 개발하였으며 Spring Security,Validation 등을 활용하였습니다. 
모바일 어플리케이션의 특성상 주로 Api로 통신하였으며 Restful한 설계를 지향하였습니다.
현재 버전은 베타버전으로 앞으로 계속해서 기능을 발전시켜 나갈 예정입니다. 

* 제작 기간은 2달 정도 소요됐으며, 구성원은 다음과 같습니다.

<a href = "https://github.com/LDH0094"><img alt="GitHub" src ="https://img.shields.io/badge/GitHub-181717.svg?&style=for-the-badge&logo=GitHub&logoColor=white"/> 임 도현 (Api,Database 설계 & 구현, 관리자 페이지(웹)) </a>

<a href = "https://github.com/Doreki"><img alt="GitHub" src ="https://img.shields.io/badge/GitHub-181717.svg?&style=for-the-badge&logo=GitHub&logoColor=white"/> 이 덕현 (모바일 애플리케이션 담당)</a>

### 기능구현

게시판의 기본적인 CRUD기능을 구현하였습니다. 사용자 편의성에 맞춰 좋아요 기능을 구현하였습니다.
사용자에 따라 좋아요한 게시물 정보를 DB에 저장하고 좋아요한 게시물에 따라 하트 표시가 뜨도록 구현하였습니다.
또한 사용자가 자신이 좋아요를 누른 게시물을 볼 수 있도록 하였으며 게시글이 작성된 시간 순서가 아닌 사용자가 좋아요를 누른 시간 순서대로 게시물이 뜨도록 하였습니다. 

<img src="src/main/resources/static/images/readme/write_like.gif" width="45%"><img src="src/main/resources/static/images/readme/like_button_clicked.gif" width="45%">

글쓰기와 좋아요(좌)

좋아요 게시물(우)

``` java
@Query(value = "select l from Likes l" +
        " join fetch l.posts"+
        " where l.member.id = :id order by l.id Desc",
        countQuery = "select count(l) from Likes l")
public Page<Likes> findLikesByMemberIdOrderByDesc(@Param("id") Long memberId, Pageable pageable);

@Query("select l from Likes l" +
        " join fetch l.posts" +
        " where l.member.id = :memberId" +
        " and l.posts.id in :postsIds")
public List<Likes> findLikesByMemberIdAndPostsIds(@Param("memberId") Long memberId, @Param("postsIds")List<Long> postsIds);
```

좋아요를 누른 순서대로 게시글 목록을 가져오기 위하여서 Likes 엔티티를 시간 순서대로 List에 담아서 가져오고 Likes 엔티티에 담겨져 있는 postId를 가져와서 Posts객체로 변환시켜줬습니다.
위 과정에서 쿼리 최적화를 위해 in 절을 이용하여 파라미터를 List로 받아와서 n+1 문제를 해결하였습니다.

### TDD와 단위테스트

TDD를 중심으로 개발하며 더 나은 설계를 추구 하였습니다. 테스트 코드를 작성하므로써 더 많은 시간이 걸리는 것 같아보였습니다. 하지만 에러가 발생할 경우 미리 작성한 단위 테스트 코드로 인해 어디서 에러가 발생했는지 쉽게 알 수 있었습니다.
이는 책임의 분리가 잘 이루어졌기 때문에 가능했던 일이라 생각합니다. 테스트코드를 먼저 작성함으로써 실제 코드를 구현하기 전에 SRP에 입각한 설계가 머리 속에 그려졌고 또한 내 코드가 더 익숙하게 다가왔습니다.

``` java
    @Test
    @WithMockCustomMember(role = Role.ADMIN)
     public void 글삭제() throws Exception{
        //given

        Long postsId = 1L;
        //when
        mockMvc.perform(
                        delete("/api/v1/admin/{postsId}", postsId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        //then
        verify(postsService).deletePosts(postsId);
    }

    @Test
    @WithMockCustomMember(role = Role.MEMBER)
    public void 글삭제_실패() throws Exception{
        //given

        Long postsId = 1L;
        //when
        mockMvc.perform(
                        delete("/api/v1/admin/{postsId}", postsId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
```

가짜객체를 직접 정의하여서 스프링 시큐리티를 테스트하는 코드  

``` java
    @Test
     public void 좋아요_게시물_찾아오기() throws Exception{
        //given
        Member member = createMember("글쓴이", "1", 1L);

        List<Posts> postsList = new ArrayList();
        List<Likes> likesList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Posts posts = createPosts(member, "글제목", "글내용", 1L+i);
            postsList.add(posts);
            Likes likes = Likes.createLikes(posts, member);
            likesList.add(likes);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Likes> page = new PageImpl<>(likesList);

        given(likesRepository.findLikesByMemberIdOrderByDesc(member.getId(),pageRequest)).willReturn(page);
        //when
        List<LikedPostsResponseDto> postsResponseDtos = postsService.viewLikedPosts(member.getId(), pageRequest);
        //then
        verify(likesRepository).findLikesByMemberIdOrderByDesc(member.getId(),pageRequest);
        assertThat(postsResponseDtos.size()).isEqualTo(10);
    }
    
    @Test
     public void 좋아요_여부_업데이트() throws Exception{
        //given
        Member member = createMember("홍길동", "1", 1L);
        List<Posts> postsList = new ArrayList<>();
        List<Likes> likesList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Posts posts = createPosts(member, "글제목", "글내용", i + 0L);
            postsList.add(posts);
            likesList.add(Likes.createLikes(posts,member));
        }
        List<Long> postsIds = postsList.stream().map(Posts::getId).collect(Collectors.toList());
        List<PostsResponseDto> postsResponseDtos = toPostsDto(postsList);
        given(likesRepository.findLikesByMemberIdAndPostsIds(member.getId(), postsIds)).willReturn(likesList);
        //when
        postsService.updateWhetherIsLiked(likesList,postsResponseDtos);
        //then
        assertThat(postsResponseDtos.get(0).isLiked()).isEqualTo(true);
    }

    private static List<PostsResponseDto> toPostsDto(List<Posts> postsList) {
        return postsList.stream().map(PostsResponseDto::createPostsResponseDto).collect(Collectors.toList());
    }

    @Test
     public void 좋아요_시간_업데이트() throws Exception{
        //given
        Member member = createMember("홍길동", "1", 1L);
        List<Posts> postsList = new ArrayList<>();

        Posts posts = createPosts(member, "글제목", "글내용", 1L);
        postsList.add(posts);

        List<Likes> likesList = new ArrayList<>();
        Likes likes = Likes.createLikes(posts, member);
        likesList.add(likes);
        List<LikedPostsResponseDto> likedPostsResponseDtos = toLikedPostsDtos(postsList);
        //when
        postsService.updateLikedDate(likedPostsResponseDtos,likesList);
        //then
        assertThat(likedPostsResponseDtos.get(0).getLikedDate()).isEqualTo(likes.getLikedDate());
    }
```

서비스 계층의 좋아요 누른 게시물 목록을 가져오는 메소드를 SRP의 기준에서 본다면 더 작은 단위의 메소드로 나눌 수 있습니다.
    
* 좋아요한 게시물 목록을 가져오는 기능
* 좋아요 여부(하트)를 업데이트 하는 기능
* 좋아요 누른 시간을 업데이트 하는 기능

책임의 관점에서 여러가지 메소드로 분류할 수 있기 때문에 각 단위별로 나눠서 단위테스트를 진행하였습니다.


### 객체지향적인 설계

### 클린코드를 향한 노력