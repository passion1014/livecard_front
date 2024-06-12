package com.livecard.front.member;

import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    public String memberList() {
        return "member/memberList";
    }

    public String memberDetail() {
        return "member/memberDetail";
    }
}
