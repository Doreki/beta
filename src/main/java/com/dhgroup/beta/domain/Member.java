package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="member",uniqueConstraints=
        {@UniqueConstraint(name="google_id_nickname_UNIQUE"
                ,columnNames={"google_id","nickname"})})
public class Member extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false,name="google_id")
    private String googleId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String userTag;


    @Builder
    public Member(Long id,String googleId, String nickname) {
        this.id = id;
        this.googleId = googleId;
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.userTag = createUserTag(); //member객체 생성과 수정 둘 다 userTag 필요함, id값은 불변하기 때문에 userTag를 다시 생성해도 똑같은 값이 들어감
        this.nickname = nickname+createUserTag();
    }

    public String createUserTag() {
        String userTag = "";
        if(id/10 == 0) {
            userTag = "000"+id;
        } else if (id/100 == 0) {
            userTag = "00" + id;
        } else if (id/1000 == 0) {
            userTag = "0" +id;
        } else {
            userTag = ""+id;
        }
        userTag = "#"+userTag;

        this.userTag = userTag;
        return userTag;
    }
}
