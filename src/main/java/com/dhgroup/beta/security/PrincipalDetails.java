package com.dhgroup.beta.security;
import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private BasicMember basicMember = null;

    public PrincipalDetails(BasicMember basicMember) {
        this.basicMember = basicMember;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> basicMember.getRole().name());
        return authorities;
    }

    @Override
    public String getPassword() {
        return basicMember.getPassword();
    }

    @Override
    public String getUsername() {
        return basicMember.getMemberName();
    }

    public Role getRole() {return  basicMember.getRole();}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //비밀번호 5회초과
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}