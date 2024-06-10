package com.livecard.front.example.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ExampleController {

    @GetMapping("/example")
    public String example() {
        return "hello world!";
    }

    // MbrUserEntity로 부터 데이터를 조회하는 메소드
    @GetMapping("/example/mbrUser")
    public String exampleMbrUser() {
        return "hello world!";
    }

}
