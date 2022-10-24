package com.dhgroup.beta.security;

import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.domain.member.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.xmlunit.diff.Comparison;

import java.util.ArrayList;
import java.util.List;

public class WithMockCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember customMember) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        BasicMember member = BasicMember.builder().username(customMember.username()).role(customMember.role()).build();

        PrincipalDetails principalDetails = new PrincipalDetails(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());
        authentication.setDetails(principalDetails);
        context.setAuthentication(authentication);
        return context;
    }
}
