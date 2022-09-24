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
public class User{

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true,nullable = false,name="google_id")
    private String googleId;

    @Column(nullable = false,name="nickname")
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @Builder
    public User(String googleId, String nickName) {
        this.googleId = googleId;
        this.nickname = nickName;
    }

    public void addBoard(Board board) {
        this.boardList.add(board);
        if (board.getUser() != this) {
            board.setUser(this);
        }
    }
}
