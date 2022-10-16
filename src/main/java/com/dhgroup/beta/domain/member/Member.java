package com.dhgroup.beta.domain.member;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String authId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(unique = true)
    private String userTag;

    @Enumerated(EnumType.STRING)
    private Provider provider;


    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public String createUserTag() {
        String userTag = "";
        if (id / 10 == 0) {
            userTag = "000" + id;
        } else if (id / 100 == 0) {
            userTag = "00" + id;
        } else if (id / 1000 == 0) {
            userTag = "0" + id;
        } else {
            userTag = "" + id;
        }
        userTag = "#" + userTag;
        this.userTag = userTag;

        return userTag;
    }

}
