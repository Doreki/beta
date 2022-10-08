package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //불필요한 생성자 접근을 막기 위함
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
public class Posts extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto_increment 설정
    @Column(name = "posts_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false, name="posts_content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private Integer likeCount;

    @Column
    private Integer commentCount;

    @Enumerated(EnumType.STRING)
    private PostsStatus postsStatus;

    @Transient
    private boolean isLiked;

    //updateDto에서 받은 내용으로 글을 수정해줌
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.postsStatus = PostsStatus.MODIFIED;
    }

    public void updateIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void like() {
        this.likeCount++;
    }

    public void likeRollback() {
        if(this.likeCount>0)
        this.likeCount--;
    }
}
