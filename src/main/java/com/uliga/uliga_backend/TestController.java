package com.uliga.uliga_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/")
    public String main() {
        return "자동 배포 성공";
    }

    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

    @GetMapping(value = "/test2")
    public String test2() {
        return "test2";
    }
}
