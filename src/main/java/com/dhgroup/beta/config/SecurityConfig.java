package com.dhgroup.beta.config;

import com.dhgroup.beta.domain.member.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //이 클래스를 활성화하겠다.
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable(); //기본화면
        http.authorizeRequests() //권한에 관련된 모든 요청
                .antMatchers("/admin/**")//admin url 하위로 실행되는 모든 페이지는
                .hasAuthority(Role.ADMIN.name())//ADMIN 권한이 있어야함 시큐리티가 ROLE이 있는지 확인하고 검증함 ex)ROLE_ADMIN
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .usernameParameter("memberName")
                .defaultSuccessUrl("/admin/");
    }
}
