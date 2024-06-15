package com.franklin.chatapp.controller;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.franklin.chatapp.annotation.GetUser;
import com.franklin.chatapp.annotation.RateLimitAPI;
import com.franklin.chatapp.annotation.RateLimitWebSocket;
import com.franklin.chatapp.entity.Message;
import com.franklin.chatapp.entity.User;
import com.franklin.chatapp.form.AddGroupChatUserForm;
import com.franklin.chatapp.form.NewGroupChatForm;
import com.franklin.chatapp.form.RenameGroupChatForm;
import com.franklin.chatapp.service.GroupChatService;
import com.franklin.chatapp.service.UserService;
import com.franklin.chatapp.service.RateLimitService.Token;
import com.franklin.chatapp.util.Response;

@Controller
@RequestMapping("/api/groupchat")
public class GroupChatController {

    @Autowired
    private GroupChatService groupChatService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimitAPI(Token.EXPENSIVE_TOKEN)
    public ResponseEntity<HashMap<String, Object>> newGroupChat(@GetUser User user,
            @RequestBody NewGroupChatForm newGroupChatForm) {
        return new ResponseEntity<>(
                Response.createBody("groupChat", groupChatService.newGroupChat(user, newGroupChatForm)), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimitAPI(Token.DEFAULT_TOKEN)
    public ResponseEntity<HashMap<String, Object>> getGroupChats(@GetUser User user) {
        return new ResponseEntity<>(Response.createBody("groupChats", groupChatService.getGroupChats(user)),
                HttpStatus.OK);
    }

    @MessageMapping("/update/{id}/users/add")
    @SendTo("/topic/groupchat/{id}")
    @RateLimitWebSocket(Token.LARGE_TOKEN)
    public Message addUser(@DestinationVariable(value = "id") Long id, Principal principal,
            @Payload AddGroupChatUserForm form) {
        User user = userService.getUserFromWebSocket(principal);
        return groupChatService.addUser(user, form, id);
    }

    @MessageMapping("/update/{id}/users/remove")
    @SendTo("/topic/groupchat/{id}")
    @RateLimitWebSocket(Token.BIG_TOKEN)
    public Message removeUser(@DestinationVariable(value = "id") Long id, Principal principal) {
        User user = userService.getUserFromWebSocket(principal);
        return groupChatService.removeUser(user, id);
    }

    @MessageMapping("/update/{id}/rename")
    @SendTo("/topic/groupchat/{id}")
    @RateLimitWebSocket(Token.LARGE_TOKEN)
    public Message renameGroupChat(@DestinationVariable(value = "id") Long id, Principal principal,
            @Payload RenameGroupChatForm form) {
        User user = userService.getUserFromWebSocket(principal);
        return groupChatService.renameGroupChat(user, form, id);
    }
}
