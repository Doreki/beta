package com.dhgroup.beta.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String name;

    private String img_file;

    @Builder
    public User(Long user_id, String name, String img_file) {
        this.user_id = user_id;
        this.name = name;
        this.img_file = img_file;
    }
}
