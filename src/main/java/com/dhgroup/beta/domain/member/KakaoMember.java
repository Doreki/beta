package com.dhgroup.beta.domain.member;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("K")
@Entity
public class KakaoMember extends Member{

    @Column
    private String authId;

    @Builder
    public KakaoMember(Long id,String nickname, String userTag,Provider provider, Role role, String authId) {
        super(id,nickname,userTag,provider,role);
        this.authId = authId;
    }
}
