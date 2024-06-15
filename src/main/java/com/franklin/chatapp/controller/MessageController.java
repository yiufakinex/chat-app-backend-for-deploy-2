package com.franklin.chatapp.controller;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.franklin.chatapp.annotation.GetUser;
import com.franklin.chatapp.annotation.RateLimitAPI;
import com.franklin.chatapp.annotation.RateLimitWebSocket;
import com.franklin.chatapp.entity.Message;
import com.franklin.chatapp.entity.User;
import com.franklin.chatapp.form.NewMessageForm;
import com.franklin.chatapp.form.PaginationForm;
import com.franklin.chatapp.service.MessageService;
import com.franklin.chatapp.service.UserService;
import com.franklin.chatapp.service.RateLimitService.Token;
import com.franklin.chatapp.util.Response;

@Controller
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @MessageMapping("/send/{id}")
    @SendTo("/topic/groupchat/{id}")
    @RateLimitWebSocket(Token.DEFAULT_TOKEN)
    public Message sendMessage(@DestinationVariable(value = "id") Long id, Principal principal,
            @Payload NewMessageForm newMessageForm) {
        User user = userService.getUserFromWebSocket(principal);
        return messageService.sendMessage(id, newMessageForm.getContent(), user);
    }

    @GetMapping(path = "/{id}/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimitAPI(Token.DEFAULT_TOKEN)
    public ResponseEntity<HashMap<String, Object>> getMessages(@GetUser User user,
            @PathVariable("id") Long id,
            PaginationForm paginationForm,
            @RequestParam("before") Long before) {
        return Response.page(messageService.getMessages(user, id, paginationForm, before));
    }

}
