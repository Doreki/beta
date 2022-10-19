package com.dhgroup.beta.domain.member;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Member extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false)
    private String nickname;
    @Column(unique = true)
    private String userTag;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

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
