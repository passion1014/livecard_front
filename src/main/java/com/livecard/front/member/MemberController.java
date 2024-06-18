package com.livecard.front.member;

import com.livecard.front.common.security.CustomUserDetails;
import com.livecard.front.common.transaction.ResultWrapper;
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
    @GetMapping("/getMemberDetail")
    public ResponseEntity<ResultWrapper<MemberDto>> getMember(@RequestBody MemberDto memberDto) {
        return memberService.getMemberDetailBySocial(memberDto.getSocialId())
                .map(ResultWrapper::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/loginUser")
    public ResponseEntity<ResultWrapper<MemberDto>> getLoginUser(@AuthenticationPrincipal UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) principal;

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

        return ResponseEntity.badRequest().build();
    }
}
