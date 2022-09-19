package com.dhgroup.beta.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String img_file;

    @Column(nullable = false)
    private String nickName;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @Builder
    public User(Long id, String name, String img_file) {
        this.id = id;
        this.name = name;
        this.img_file = img_file;
    }
}
