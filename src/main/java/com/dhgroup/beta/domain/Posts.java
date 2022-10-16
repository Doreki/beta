package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import com.dhgroup.beta.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //불필요한 생성자 접근을 막기 위함
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@EqualsAndHashCode
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

    @OneToMany(mappedBy = "posts")
    List<Likes> likesList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PostsStatus status;


    //updateDto에서 받은 내용으로 글을 수정해줌
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.status = PostsStatus.MODIFIED;
    }

    public void like() {
        this.likeCount++;
    }

    public void likeRollback() {
        if(this.likeCount>0)
        this.likeCount--;
    }
}
