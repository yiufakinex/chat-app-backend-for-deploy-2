package com.franklin.chatapp.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.franklin.chatapp.annotation.GetUser;
import com.franklin.chatapp.annotation.RateLimitAPI;
import com.franklin.chatapp.entity.User;
import com.franklin.chatapp.service.UserService;
import com.franklin.chatapp.service.RateLimitService.Token;
import com.franklin.chatapp.util.Response;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimitAPI(Token.BIG_TOKEN)
    public ResponseEntity<HashMap<String, Object>> search(@GetUser User user,
            @RequestParam("username") String username) {
        return new ResponseEntity<>(Response.createBody("user", userService.findByUsername(username)), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/display_name", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimitAPI(Token.LARGE_TOKEN)
    public ResponseEntity<HashMap<String, Object>> updateDisplayName(@GetUser User user,
            @RequestParam("displayName") String displayName) {
        return new ResponseEntity<>(Response.createBody("user", userService.updateDisplayName(user, displayName)),
                HttpStatus.OK);
    }

}
