package com.drawhale.authenticationservice.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class DemoController {

    @GetMapping("/demo")
    public String sayDemo() {
        return "Welcome Authentication DemoController";
    }
}
