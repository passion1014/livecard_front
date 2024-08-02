package com.livecard.front.member.service;

import com.livecard.front.dto.member.MemberDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
public interface MemberService {
    Long getMemberId(String socialId);

    Optional<MemberDto> getMemberDetailBySocial(String socialId);

    /**
     * 회원삭제
     * @param mbrUserId
     */
    void deleteMember(Long mbrUserId);

}
