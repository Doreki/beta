package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
        this.nickname = nickname; //엔티티가 db에 들어가서 id값을 받아와야 하기때문에 service 로직에서 처리함
    }

    public void updateNicknameAddUserTag(String nickname) {
        this.nickname = nickname+this.userTag;
    }

    public void addUserTag(){
        this.userTag = createUserTag();
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
