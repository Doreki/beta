package com.dhgroup.beta.domain.member;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("B")
@Entity
public class BasicMember extends Member{
    @Column
    private String memberName;

    @Column(length = 500)
    private String password;

    @Builder
    public BasicMember(Long id,String nickname, String userTag,Provider provider,
                       Role role, String memberName, String password) {
        super(id,nickname,userTag,provider,role);
        this.memberName = memberName;
        this.password = password;
    }
}
