package com.livecard.front.member;

import com.livecard.front.common.transaction.ResultWrapper;
import com.livecard.front.dto.member.MemberDto;
import com.livecard.front.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberRESTController {
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

}
