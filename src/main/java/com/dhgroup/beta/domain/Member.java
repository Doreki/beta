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

    @Column(nullable = false,name="nickname")
    private String nickname;


    @Builder
    public Member(String googleId, String nickName) {
        this.googleId = googleId;
        this.nickname = nickName;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
