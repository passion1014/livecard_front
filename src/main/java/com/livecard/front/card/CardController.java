package com.livecard.front.card;


import com.livecard.front.common.security.CustomUserDetails;
import com.livecard.front.common.transaction.ResultWrapper;
import com.livecard.front.common.util.SecurityUtil;
import com.livecard.front.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
public class CardController {


    @PutMapping("/create")
    public ResponseEntity<ResultWrapper<MemberDto>> getLoginUser() {
        CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();

        MemberDto memberDto = MemberDto.builder()
                .id(customUserDetails.getId())
                .socialId(customUserDetails.getMemberId())
                .name(customUserDetails.getMemberName())
                .providerCd(customUserDetails.getProviderCd())
                .profileImg(customUserDetails.getProfileImg())
                .build();

        return Optional.of(memberDto)
                .map(ResultWrapper::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
