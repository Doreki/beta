package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.MemberMismatchException;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.exception.OverlapMemberException;
import com.dhgroup.beta.web.dto.MemberDto.JoinRequest.JoinRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberLoginRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {

        //userTag를 생성하기 위해 우선적으로 DB에 저장
        Member member = joinRequestDto.toEntity();
        try{
            member = memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new OverlapMemberException("중복된 회원입니다.");
        }
        member.createUserTag();
        //DB에서 받아온 id로 닉네임에 유저태그 더해줌
        return member.getId();
    }

    public MemberResponseDto login(String authId,Provider provider) {
        Member member = findMemberByAuthId(authId,provider);
        return MemberResponseDto.createMemberResponseDto(member);
    }

    public MemberResponseDto login(MemberLoginRequestDto memberLoginRequestDto) {
        BasicMember basicMember = memberRepository
                                .findByUsername(memberLoginRequestDto.getUsername())
                                .orElseThrow(() -> new MemberMismatchException("아이디 혹은 비밀번호가 일치하지 않습니다."));
        if(isPassword(memberLoginRequestDto,basicMember)){
            return MemberResponseDto.createMemberResponseDto(basicMember);
        } else {
            throw new MemberMismatchException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }
    }

    private static boolean isPassword(MemberLoginRequestDto memberLoginRequestDto,BasicMember basicMember) {
        return new BCryptPasswordEncoder().matches(memberLoginRequestDto.getPassword(),basicMember.getPassword());
    }

    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = findMemberByMemberId(memberId);
        member.updateNickname(nickname);
    }

    public boolean isDuplicated(String authId, Provider provider) {
        if(memberRepository.findByAuthId(authId,provider).isPresent()) {
            return true;
        }
        else
            return false;
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    private Member findMemberByAuthId(String authId,Provider provider) {
        return memberRepository.findByAuthId(authId,provider).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }
}
