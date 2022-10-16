package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.web.dto.MemberDto.KakaoJoinRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(KakaoJoinRequestDto kakaoJoinRequestDto) {

        //userTag를 생성하기 위해 우선적으로 DB에 저장
        Member member = kakaoJoinRequestDto.toEntity();
//        try{
            member = memberRepository.save(member);
//        } catch (DataIntegrityViolationException e) {
//            throw new OverlapMemberException("중복된 회원입니다.");
//        }
        member.createUserTag();
        //DB에서 받아온 id로 닉네임에 유저태그 더해줌
        return member.getId();
    }

    public MemberResponseDto logIn(String googleId) {
        Member member = findMemberByGoogleId(googleId);
        return MemberResponseDto.createMemberResponseDto(member);
    }


    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = findMemberByMemberId(memberId);
        member.updateNickname(nickname);
    }

    public boolean isDuplicated(String googleId) {
        if(memberRepository.findByKakaoId(googleId).isPresent()) {
            return true;
        }
        else
            return false;
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    private Member findMemberByGoogleId(String googleId) {
        return memberRepository.findByKakaoId(googleId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }
}
