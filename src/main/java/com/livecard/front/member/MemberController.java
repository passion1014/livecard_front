package com.livecard.front.member;

import com.livecard.front.common.security.CustomUserDetails;
import com.livecard.front.common.transaction.ResultWrapper;
import com.livecard.front.common.util.SecurityUtil;
import com.livecard.front.dto.member.MemberDto;
import com.livecard.front.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /*
     * Member의 정보를 조회하는 REST API
     */
    @GetMapping("/detail")
    public ResponseEntity<ResultWrapper<MemberDto>> getMember() {
        CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();
        return memberService.getMemberDetailBySocial(customUserDetails.getMemberId())
                .map(ResultWrapper::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/loginUser")
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
