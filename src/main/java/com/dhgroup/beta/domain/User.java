package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "google_id_nickname_unique",
        columnNames = {"google_id", "nickname"} )})
public class User extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false,name="google_id")
    private String googleId;

    @Column(nullable = false,name="nickname")
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @Builder
    public User(Long id, String googleId, String nickName) {
        this.id = id;
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
