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
}
