package com.example.guildmessenger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessengerController {
    @RequestMapping("/")
    public String home() {
        return "Hello Guild!";
    }
}
