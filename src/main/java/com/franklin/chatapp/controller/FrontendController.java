package com.franklin.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.franklin.chatapp.annotation.GetUser;
import com.franklin.chatapp.entity.User;

@Controller
public class FrontendController {

    @GetMapping(path = "/")
    public String index() {
        return "forward:/built/index.html";
    }

    @GetMapping(path = "/login")
    public String login(@GetUser User user) {
        if (user == null) {
            return "/login";
        }
        return "redirect:/";
    }

    @GetMapping(path = "/new_user")
    public String newUser() {
        return "newUser";
    }
}
