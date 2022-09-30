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
@Table(name="members",uniqueConstraints=
        {@UniqueConstraint(name="google_id_nickname_UNIQUE"
                ,columnNames={"google_id","nickname"})})
public class Member extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false,name="google_id")
    private String googleId;

    @Column(nullable = false,name="nickname")
    private String nickname;


    @Builder
    public Member(Long id,String googleId, String nickname) {
        this.id = id;
        this.googleId = googleId;
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public String createUserTag() {
        String userTag = "";
        if(id/10 == 0) {
            userTag = "000"+id;
        } else if (id/100 == 0) {
            userTag = "00" + id;
        } else if (id/1000 == 0) {
            userTag = "0" +id;
        }
        userTag = "#"+userTag;
        return userTag;
    }
}
