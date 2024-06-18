package com.livecard.front.member.service;

import com.livecard.front.dto.member.MemberDto;

import java.util.Optional;

public interface MemberService {
    Long getMemberId(String socialId);

    Optional<MemberDto> getMemberDetailBySocial(String socialId);







}
