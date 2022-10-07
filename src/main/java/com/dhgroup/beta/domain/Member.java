package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "member", uniqueConstraints =
        {@UniqueConstraint(name = "google_id_nickname_UNIQUE",
                columnNames = {"google_id", "nickname"})})
public class Member extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, name = "google_id")
    private String googleId;

    @Column(nullable = false)
    private String nickname;

    public void updateNicknameAddUserTag(String nickname) {
        this.nickname = nickname + createUserTag();
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

        return userTag;
    }
}
