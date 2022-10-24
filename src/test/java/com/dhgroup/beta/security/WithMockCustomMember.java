package com.dhgroup.beta.security;

import com.dhgroup.beta.domain.member.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@WithSecurityContext(factory = WithMockCustomMemberSecurityContextFactory.class)
public @interface WithMockCustomMember {

    Role role() default  Role.ADMIN;
    String username() default "idh0325";
}
